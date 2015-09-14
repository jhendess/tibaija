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

import org.xlrnet.tibaija.exception.OutOfScreenBoundsException;
import org.xlrnet.tibaija.memory.Value;

/**
 * Home screen for a calculator. A home screen supports only outputting texts and inputting values. Drawing functions
 * are not available. All coordinates begin with one (1) and not with zero. Trying to output to a coordinate of zero or
 * less will cause an {@link OutOfScreenBoundsException}.
 */
public interface HomeScreen {

    /**
     * Clears the home screen.
     */
    void clear();

    /**
     * Displays and formats a given {@link Value} at the given coordinates on the home screen. If the formatted content
     * exceeds the maximum width of the line, the text must be hard wrapped to the next line.
     *
     * @param text
     *         The text to print.
     * @param x
     *         The X coordinate where the text print should begin. First valid coordinate is always one (1).
     * @param y
     *         The Y coordinate where the text print should begin. First valid coordinate is always one (1).
     */
    void printAt(Value text, int x, int y) throws OutOfScreenBoundsException;

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
    void printAt(String text, int x, int y) throws OutOfScreenBoundsException;

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
    void printText(String text);

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
    void printValue(Value value);

}
