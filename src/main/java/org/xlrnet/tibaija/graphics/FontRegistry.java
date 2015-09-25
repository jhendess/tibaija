/*
 * Copyright (c) 2015 Jakob Hendeß
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE
 */

package org.xlrnet.tibaija.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.tools.fontgen.Font;
import org.xlrnet.tibaija.tools.fontgen.FontImportException;
import org.xlrnet.tibaija.tools.fontgen.Symbol;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Main registry for storing fonts and resolving single characters to their respective {@link PixelSprite}.
 */
public class FontRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontRegistry.class);

    private Map<String, IndexedFont> fontMap = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nullable
    public PixelSprite getSpriteByHexValue(@NotNull String fontIdentifier, int hexValue) {
        return this.fontMap.get(fontIdentifier).hexMap.get(hexValue);
    }

    @Nullable
    public PixelSprite getSpriteByRepresentation(@NotNull String fontIdentifier, String representation) {
        return this.fontMap.get(fontIdentifier).characterMap.get(representation);
    }

    public void registerFont(@NotNull Path filePath, @NotNull String identifier) throws IOException {

        if (fontMap.containsKey(identifier)) {
            throw new FontImportException("Font with identifier " + identifier + " already exists");
        }

        LOGGER.info("Reading font file {} ...", filePath.toAbsolutePath());

        InputStream in = Files.newInputStream(filePath);
        Font font = objectMapper.readValue(in, Font.class);
        registerFont(font, identifier);
    }

    public void registerFont(@NotNull Font font, @NotNull String identifier) {
        if (fontMap.containsKey(identifier)) {
            throw new FontImportException("Font with identifier " + identifier + " already exists");
        }

        LOGGER.info("Importing font \"{}\" ...", font.getFontName());

        int imports = 0;
        IndexedFont indexedFont = new IndexedFont();

        for (Symbol symbol : font.getSymbols()) {
            boolean success = importSymbol(symbol, indexedFont);
            if (success) imports++;
        }

        fontMap.put(identifier, indexedFont);
        LOGGER.debug("Imported {} symbols.", imports);
    }

    private boolean importSymbol(@NotNull Symbol symbol, @NotNull IndexedFont indexedFont) {
        PixelState[][] symbolData = symbol.getData();
        PixelSprite pixelSprite = new PixelSprite(symbolData);

        int hexValue = Integer.parseInt(symbol.getHexValue(), 16);
        String representation = symbol.getRepresentation();

        if (indexedFont.hexMap.containsKey(hexValue)) {
            LOGGER.warn("Ignoring duplicate symbol with hex value 0x{} (internal identifier: '{}')", symbol.getHexValue(), symbol.getInternalIdentifier());
            return false;
        } else {
            indexedFont.hexMap.put(hexValue, pixelSprite);
        }
        if (StringUtils.isBlank(representation) && !" ".equals(representation)) {
            LOGGER.debug("Ignoring empty representation for symbol with hex value 0x{} (internal identifier: '{}')", symbol.getHexValue(), symbol.getInternalIdentifier());
        } else {
            if (indexedFont.characterMap.containsKey(representation)) {
                LOGGER.warn("Ignoring duplicate symbol with representation \"{}\" (internal identifier: '{}') - hex value is still registered", symbol.getHexValue(), symbol.getInternalIdentifier());
            } else {
                indexedFont.characterMap.put(representation, pixelSprite);
            }
        }

        return true;
    }

    static class IndexedFont {

        final Map<Integer, PixelSprite> hexMap = new HashMap<>();

        final Map<String, PixelSprite> characterMap = new HashMap<>();

    }

}

