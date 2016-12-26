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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.ExecutionEnvironment;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.OutOfScreenBoundsException;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of a TI-83 HomeScreen using any {@link Display}.
 */
public class TI83PlusHomeScreen implements HomeScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger(TI83PlusHomeScreen.class);

    private static final int COLUMN_WIDTH = 5;

    private static final int ROW_HEIGHT = 7;

    private static final int SEPARATION = 1;

    private final SpritePrinter spritePrinter = new SpritePrinter();

    private final Deque<List<PixelSprite>> spriteBuffer = new LinkedList<>();

    private ExecutionEnvironment environment;

    private Display display;

    /**
     * Clears the home screen.
     */
    @Override
    public void clear() {
        display.clearScreen();
        display.flush();
    }

    @Override
    public void configure(ExecutionEnvironment environment, Display display) {
        this.environment = environment;
        this.display = display;
    }

    /**
     * Return the number of columns which can be displayed on this home screen.
     *
     * @return the number of columns which can be displayed on this home screen.
     */
    @Override
    public int getMaxColumns() {
        return 16;
    }

    /**
     * Return the number of rows which can be displayed on this home screen.
     *
     * @return the number of rows which can be displayed on this home screen.
     */
    @Override
    public int getMaxRows() {
        return 8;
    }

    /**
     * Displays a given string at the given coordinates on the home screen. If the text exceeds the maximum width of the
     * line, the text must be hard wrapped to the next line. If the content goes past the last column of the row, the
     * text will be truncated.
     *
     * @param text
     *         The text to print.
     * @param x
     *         The X coordinate where the text print should begin. First valid coordinate is always one (1).
     * @param y
     *         The Y coordinate where the text print should begin. First valid coordinate is always one (1).
     */
    @Override
    public void printAt(String text, int x, int y) throws OutOfScreenBoundsException {
        checkScreenBounds(x, y);
        LOGGER.trace("Printing text {} at {},{}", text, x, y);

        int printBegin = 0;
        while (printBegin < text.length()) {
            int printableCharsOnCurrentLine = getMaxRows() - x - 1;
            String textOnCurrentLine = StringUtils.substring(text, printBegin, printableCharsOnCurrentLine);
            printBegin += textOnCurrentLine.length();
            List<PixelSprite> spritesForText = environment.getFontRegistry().getSpritesForText(FontConstants.FONT_LARGE, text);
            int effectiveOffsetX = (x - 1) * getColumnWidth();
            int effectiveOffsetY = (y - 1) * getRowHeight();
            spritePrinter.printSpritesHorizontally(spritesForText, display, effectiveOffsetX, effectiveOffsetY, SEPARATION);
            y++;
        }
    }

    /**
     * Displays and formats a given {@link Value} at the given coordinates on the home screen. If the formatted content
     * exceeds the maximum width of the line, the text must be hard wrapped to the next line. If the content goes past
     * the last column of the row, the text will be truncated.
     *
     * @param text
     *         The text to print.
     * @param x
     *         The X coordinate where the text print should begin. First valid coordinate is always one (1).
     * @param y
     *         The Y coordinate where the text print should begin. First valid coordinate is always one (1).
     */
    @Override
    public void printAt(Value text, int x, int y) throws OutOfScreenBoundsException {
        checkScreenBounds(x, y);
        String formatted = environment.formatValue(text);
        printAt(formatted, x, y);
    }

    /**
     * Prints a text string to the homescreen. This appends the content to print at the bottom of the screen. If the
     * screen does not support any more lines at the bottom, the first line must be removed and the rest be moved
     * upwards. If the text to display is larger than the width of the screen, the text content must be hard-wrapped to
     * the next line. TODO: Is this correct?
     *
     * @param text
     *         The text to display.
     */
    @Override
    public void printText(String text) {
        boolean forceRedraw = false;
        // Remove first line from buffer if size is exceeded
        if (spriteBuffer.size() >= getMaxRows()) {
            spriteBuffer.pop();
            forceRedraw = true;
        }

        LOGGER.trace("Printing {} on home screen", text);

        String textToPrint = text;

        while (StringUtils.isNotEmpty(textToPrint)) {
            String currentLine = StringUtils.substring(textToPrint, 0, getMaxColumns());
            textToPrint = StringUtils.substring(textToPrint, getMaxColumns());
            List<PixelSprite> spritesForText = environment.getFontRegistry().getSpritesForText(FontConstants.FONT_LARGE, currentLine);
            spriteBuffer.offer(spritesForText);
            if (forceRedraw) {
                redrawFullScreen();
            } else {
                redrawLastLine();
            }
            //printText(textToPrint);
        }
    }

    /**
     * Print the content of a {@link Value} object. The output must always be right-aligned unless the value is a
     * string. In this case, printing a string should behave identical to {@link #printText(String)}. If the screen does
     * not  support any more lines at the bottom, the first line must be removed and the rest be moved upwards. If the
     * text to display is larger than the width of the screen, the text must be cut to the maximum width with an
     * ellipsis (...). Numerical values must be formatted according to the last set {@link DecimalDisplayMode} and
     * {@link NumberDisplayFormat}.
     *
     * @param value
     *         The value to display.
     */
    @Override
    public void printValue(Value value) {
        String formatted = environment.formatValue(value);

        LOGGER.trace("Printing value {} formatted as {}", value, formatted);

        if (formatted.length() > getMaxColumns()) {
            formatted = formatted.substring(0, getMaxColumns() - 1) + FontConstants.ELLIPSIS;
        } else if (!value.isString()) {
            formatted = StringUtils.repeat(FontConstants.BLANK, getMaxColumns() - formatted.length()) + formatted;
        }

        printText(formatted);
    }

    protected int getColumnWidth() {
        return COLUMN_WIDTH;
    }

    protected int getRowHeight() {
        return ROW_HEIGHT;
    }

    private void checkScreenBounds(int x, int y) {
        if (x > getMaxColumns() || y > getMaxRows() || x < 1 || y < 1) {
            throw new OutOfScreenBoundsException("Illegal home screen coordinates", x, y, getMaxRows(), getMaxColumns());
        }
    }

    private void redrawFullScreen() {
        LOGGER.trace("Begin redrawing full home screen");
        display.clearScreen();
        int y = 0;
        for (List<PixelSprite> pixelSprites : spriteBuffer) {
            int effectiveOffsetY = y * getRowHeight() + y;
            spritePrinter.printSpritesHorizontally(pixelSprites, display, 0, effectiveOffsetY, SEPARATION);
            y++;
        }
        display.flush();
        LOGGER.trace("Finished redrawing full home screen");
    }

    private void redrawLastLine() {
        int bufferedLines = spriteBuffer.size() - 1;
        int effectiveOffsetY = bufferedLines * getRowHeight() + bufferedLines;
        LOGGER.trace("Drawing buffer line {}", bufferedLines);
        spritePrinter.printSpritesHorizontally(spriteBuffer.getLast(), display, 0, effectiveOffsetY, SEPARATION);
        display.flush();
    }
}
