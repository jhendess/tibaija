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

import java.util.Arrays;

/**
 * A {@link PixelSprite} represents a set of pixels that can be printed on a {@link Display}. Each pixel in the sprite
 * can either be enabled or disabled when printing the value.
 */
public class PixelSprite {

    private final PixelState[][] pixelStates;

    private final int xDimension;

    private final int yDimension;

    public PixelSprite(PixelState[][] pixelStates) {
        this.yDimension = pixelStates.length;

        int t = 0;
        for (PixelState[] pixelState : pixelStates) {
            if (t < pixelState.length) {
                t = pixelState.length;
            }
        }
        this.xDimension = t;
        this.pixelStates = expandArray(pixelStates, xDimension, yDimension);
    }

    /**
     * Returns a two-dimensional map with the pixels within this sprite. The first index of the returned array contains
     * the Y-coordinate and the second index contains the X-coordinate of the sprite.
     *
     * @return a two-dimensional map with the pixels within this sprite.
     */
    public PixelState[][] getPixelStates() {
        return this.pixelStates;
    }

    /**
     * Returns the horizontal size on the x-axis of this sprite.
     *
     * @return the horizontal size on the x-axis of this sprite.
     */
    public int getXDimension() {
        return this.xDimension;
    }

    /**
     * Returns the vertical size on the y-axis of this sprite.
     *
     * @return the vertical size on the y-axis of this sprite.
     */
    public int getYDimension() {
        return this.yDimension;
    }

    /**
     * Copy the content of the source array to a new array and make sure that all indices in the y-dimension have the
     * same length. All missing elements will be filled with {@link PixelState#OFF}.
     *
     * @param sourceArray
     *         The source array.
     * @param xDimension
     *         Horizontal size on the x-axis.
     * @param yDimension
     *         Vertical size on the y-axis
     * @return A two-dimensional array with the exact dimensions as specified.
     */
    private PixelState[][] expandArray(PixelState[][] sourceArray, int xDimension, int yDimension) {
        PixelState[][] expandedArray = new PixelState[yDimension][xDimension];

        for (int i = 0, pixelStatesLength = sourceArray.length; i < pixelStatesLength; i++) {
            PixelState[] sourceX = sourceArray[i];
            Arrays.fill(expandedArray[i], PixelState.OFF);
            System.arraycopy(sourceX, 0, expandedArray[i], 0, sourceX.length);
        }

        return expandedArray;
    }

}
