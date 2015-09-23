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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.OutOfScreenBoundsException;
import org.xlrnet.tibaija.memory.Value;

/**
 * Home screen which prints no output to a graphical display but only to the system log.
 */
public class NullHomeScreen implements HomeScreen {

    private static final Logger LOGGER = LoggerFactory.getLogger(NullHomeScreen.class);

    private static final int MAX_ROWS = 8;

    private static final int MAX_COLUMNS = 16;

    /**
     * Clears the home screen.
     */
    @Override
    public void clear() {
        LOGGER.info("Home screen cleared");
    }

    /**
     * Return the number of columns which can be displayed on this home screen.
     *
     * @return the number of columns which can be displayed on this home screen.
     */
    @Override
    public int getMaxColumns() {
        return MAX_COLUMNS;
    }

    /**
     * Return the number of rows which can be displayed on this home screen.
     *
     * @return the number of rows which can be displayed on this home screen.
     */
    @Override
    public int getMaxRows() {
        return MAX_ROWS;
    }

    /**
     * Displays and formats a given {@link Value} at the given coordinates on the home screen. If the formatted content
     * exceeds the maximum width of the line, the text must be hard wrapped to the next line.
     *
     * @param value
     *         The text to print.
     * @param x
     *         The X coordinate where the text print should begin. First valid coordinate is always one (1).
     * @param y
     *         The Y coordinate where the text print should begin. First valid coordinate is always one (1).
     */
    @Override
    public void printAt(Value value, int x, int y) throws OutOfScreenBoundsException {
        LOGGER.info("Printing {} to coordinates {},{} on home screen", value, x, y);
    }

    /**
     * Displays a given string at the given coordinates on the home screen. If the text exceeds the maximum width of
     * the line, the text must be hard wrapped to the next line.
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
        LOGGER.info("Printing text \"{}\" to coordinates {},{} on home screen", text, x, y);
    }

    /**
     * Prints a text string to the homescreen. This appends the content to print at the bottom of the screen. If the
     * screen does not support any more lines at the bottom, the first line must be removed and the rest be moved
     * upwards. If the text to display is larger than the width of the screen, the text content must be hard-wrapped to
     * the next line.
     * TODO: Is this correct?
     *
     * @param text
     *         The text to display.
     */
    @Override
    public void printText(String text) {
        LOGGER.info("Printing text \"{}\" on home screen", text);
    }

    /**
     * Print the content of a {@link Value} object. The output must always be right-aligned. If the screen does not
     * support any more lines at the bottom, the first line must be removed and the rest be moved upwards. If the text
     * to display is larger than the width of the screen, the text must be cut to the maximum width with an
     * ellipsis (...).
     * Numerical values must be formatted according to the last set {@link DecimalDisplayMode}.
     *
     * @param value
     *         The value to display.
     */
    @Override
    public void printValue(Value value) {
        LOGGER.info("Printing value \"{}\" on home screen", value);
    }
}
