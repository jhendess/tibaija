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

import java.io.IOException;

/**
 * A low-level output device which supports enabling and disabling single pixels and various other low-level
 * operations. A pixel is always represented as a single dot on the screen and smallest visible unit.
 * The screen begins always in the top left corner (0,0), The total amount of pixels depends on the concrete
 * implementation.
 */
public interface Display {

    /**
     * Resets the screen and turns all pixels off. Flushing is not done automatically afterwards.
     */
    void clearScreen();

    /**
     * Close this screen and don't display it to the user anymore.
     */
    void close() throws IOException;

    /**
     * Flushes all output from the internal buffer and draws it onto the screen.
     */
    void flush();

    /**
     * Returns the amount of pixels on the horizontal x-axis.
     *
     * @return the amount of pixels on the horizontal x-axis.
     */
    int getHorizontalDimension();

    /**
     * Returns the amount of pixels on the vertical y-axis.
     *
     * @return the amount of pixels on the vertical y-axis.
     */
    int getVerticalDimension();

    /**
     * Inverts the selected pixel. If the pixel is turned on before, it will be turned off afterwards.
     *
     * @param x
     *         The x coordinate of the pixel to update.
     * @param y
     *         The y coordinate of the pixel to update.
     */
    void invertPixel(int x, int y);

    /**
     * Checks if the pixel at the given coordinates is on.
     *
     * @param x
     *         The x coordinate of the pixel to query.
     * @param y
     *         The y coordinate of the pixel to query.
     * @return True if the requested pixel is on, otherwise false.
     */
    boolean isPixelOn(int x, int y);

    /**
     * Make this screen initially visible to the user. This method needs to be called always at least one time before
     * anything can be displayed on the screen.
     * @throws IOException Thrown if an error occurred during initialization of the screen.
     */
    void open() throws IOException;

    /**
     * Sets the status of a selected pixel either to on (visible) or off (invisible).
     *
     * @param pixelState
     *         True if the pixel should be visible, false otherwise.
     * @param x
     *         The x coordinate of the pixel to draw.
     * @param y
     *         The y coordinate of the pixel to draw.
     */
    void setPixel(PixelState pixelState, int x, int y);

}
