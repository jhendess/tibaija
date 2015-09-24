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

package org.xlrnet.tibaija.tools.fontgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.graphics.PixelState;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Application class for generating the Tibaija font configuration from the font gifs on TIBasicDev.
 * <p>
 * HowTo:
 * <p>
 * <ul> <li>Download the small gif files displayed in the table on the top from {@see
 * http://tibasicdev.wikidot.com/83lgfont} and {@see http://tibasicdev.wikidot.com/83smfont} to a directory of your
 * choice. Do NOT rename any files!</li> <li> Run this application with the directory to which you downloaded the files
 * as first parameter and with the target directory for the created font files as the second parameter. </li> <li>After
 * the .json files have been generated, set all "text"-fields accordingly.</li> </ul>
 */
public class FontgenApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontgenApplication.class);

    private static final String LARGE_FONT_IDENTIFIER = "L";

    private static final String SMALL_FONT_IDENTIFIER = "S";

    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) throws IOException {
        new FontgenApplication().run(args);
    }

    private Symbol importFile(Path path, Font font, String fontIdentifier) throws IOException, ImageReadException {
        LOGGER.info("Importing file {} ...", path.toAbsolutePath());

        BufferedImage image = Imaging.getBufferedImage(Files.newInputStream(path));
        int width = image.getWidth();
        int height = image.getHeight();
        int finalWidth = width / 2;
        int finalHeight = height / 2;

        if (width % 2 != 0 || height % 2 != 0) {
            LOGGER.warn("Width and height must be multiple of 2");
            return null;
        }

        Symbol symbol = new Symbol();
        PixelState[][] pixelStates = new PixelState[finalHeight][finalWidth];
        Raster imageData = image.getData();

        for (int y = 0; y < finalHeight; y++) {
            for (int x = 0; x < finalWidth; x++) {
                int sample = imageData.getSample(x * 2, y * 2, 0);
                PixelState pixelState = sample == 0 ? PixelState.ON : PixelState.OFF;
                pixelStates[y][x] = pixelState;
            }
        }

        symbol.setData(pixelStates);
        return symbol;
    }

    @NotNull
    private Font importFont(String source, String fontIdentifier) throws IOException {
        Path sourcePath = Paths.get(source);

        Pattern filePattern = Pattern.compile("[0-9A-F]{2}h_" + fontIdentifier + "[a-zA-Z0-9]*.gif");

        List<Path> fileList = Files.list(sourcePath)
                .filter(p -> filePattern.matcher(p.getFileName().toString()).matches())
                .collect(Collectors.toList());

        Font font = new Font();
        List<Symbol> symbols = new ArrayList<>();

        for (Path path : fileList) {
            try {
                Symbol symbol = importFile(path, font, fontIdentifier);

                if (symbol == null) continue;

                String filename = path.getFileName().toString();
                String hexValue = StringUtils.substring(filename, 0, 2);
                String internalIdentifier = StringUtils.substringBetween(filename, "_" + fontIdentifier, ".gif");

                symbol.setHexValue(hexValue);
                symbol.setInternalIdentifier(internalIdentifier);

                symbols.add(symbol);
            } catch (ImageReadException e) {
                LOGGER.error("Reading image {} failed", path.toAbsolutePath(), e);
            }
        }

        Collections.sort(symbols);
        font.setSymbols(symbols);

        return font;
    }

    private void run(String[] args) throws IOException {
        checkArgument(args.length == 2, "Wrong argument count");

        String source = args[0];
        String target = args[1];

        LOGGER.info("Source directory: {}", source);
        LOGGER.info("Target directory: {}", target);

        Font largeFont = importFont(source, LARGE_FONT_IDENTIFIER);
        largeFont.setFontName("TI-83+ large");
        Font smallFont = importFont(source, SMALL_FONT_IDENTIFIER);
        smallFont.setFontName("TI-83+ small");

        Path largePath = Paths.get(target, "largeFont.json");
        Path smallPath = Paths.get(target, "smallFont.json");

        OutputStream largeOutputStream = Files.newOutputStream(largePath);
        OutputStream smallOutputStream = Files.newOutputStream(smallPath);

        objectMapper.writer().writeValue(largeOutputStream, largeFont);
        objectMapper.writer().writeValue(smallOutputStream, smallFont);

        LOGGER.info("Imported {} elements.", largeFont.getSymbols().size() + smallFont.getSymbols().size());
    }

}
