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

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Spriter printers can be used for printing the content of a {@link PixelSprite} to a {@link Display}. When printing a
 * sprite, the internal pixel states returned by {@link PixelSprite#getPixelStates()} can be printed to a display with a
 * given offset.
 */
public class SpritePrinter {

    /**
     * Print the content of a given {@link PixelSprite} to a specified {@link Display}. Any sprite contents that exceed
     * the maximum display dimensions will be truncated. An optional offset for x- and y-axis can be defined to move the
     * sprite around the display. When the parameter for inverting is set to true, each {@link PixelState} will be
     * inverted, so that {@link PixelState#OFF} will be printed as {@link PixelState#ON} and vice-versa. Printing a
     * sprite will not automatically trigger the {@link Display#flush()} method.
     *
     * @param pixelSprite
     *         The sprite to print.
     * @param targetDisplay
     *         The target display where the sprite should be printed.
     * @param offsetX
     *         Horizontal offset on the x-axis.
     * @param offsetY
     *         Vertical offset on the y-axis.
     * @param invert
     *         True, if the sprite should be printed inverted.
     */
    public void printSprite(@NotNull PixelSprite pixelSprite, @NotNull Display targetDisplay, int offsetX, int offsetY, boolean invert) {
        int xDimension = pixelSprite.getXDimension();
        int yDimension = pixelSprite.getYDimension();
        int horizontalDisplaySize = targetDisplay.getHorizontalDimension();
        int verticalDisplaySize = targetDisplay.getVerticalDimension();

        PixelState[][] pixelStates1 = pixelSprite.getPixelStates();

        for (int y = 0; y < yDimension; y++) {
            int yPixel = y + offsetY;
            for (int x = 0; x < xDimension; x++) {
                int xPixel = x + offsetX;

                if (xPixel >= 0 && yPixel >= 0 && xPixel < horizontalDisplaySize && yPixel < verticalDisplaySize) {
                    internalPrintPixel(targetDisplay, pixelStates1[y][x], yPixel, xPixel, invert);
                }
            }
        }
    }

    /**
     * Print the content of a given {@link PixelSprite} to a specified {@link Display}. Any sprite contents that exceed
     * the maximum display dimensions will be truncated. An optional offset for x- and y-axis can be defined to move the
     * sprite around the display. Printing a sprite will not automatically trigger the {@link Display#flush()} method.
     *
     * @param pixelSprite
     *         The sprite to print.
     * @param targetDisplay
     *         The target display where the sprite should be printed.
     * @param offsetX
     *         Horizontal offset on the x-axis.
     * @param offsetY
     *         Vertical offset on the y-axis.
     */
    public void printSprite(@NotNull PixelSprite pixelSprite, @NotNull Display targetDisplay, int offsetX, int offsetY) {
        printSprite(pixelSprite, targetDisplay, offsetX, offsetY, false);
    }

    /**
     * Print the content of a given {@link PixelSprite} to a specified {@link Display}. Any sprite contents that exceed
     * the maximum display dimensions will be truncated. Printing a sprite will not automatically trigger the {@link
     * Display#flush()} method.
     *
     * @param pixelSprite
     *         The sprite to print.
     * @param targetDisplay
     *         The target display where the sprite should be printed.
     */
    public void printSprite(@NotNull PixelSprite pixelSprite, @NotNull Display targetDisplay) {
        printSprite(pixelSprite, targetDisplay, 0, 0, false);
    }

    /**
     * Prints a list of sprites from left to right on the given display. Any sprite contents that exceed
     * the maximum display dimensions will be truncated. Printing a sprite will not automatically trigger the {@link
     * Display#flush()} method.
     *
     * @param pixelSprites
     *         The sprites to print.
     * @param targetDisplay
     *         The target display where the sprite should be printed.
     * @param offsetX
     *         Horizontal offset on the x-axis.
     * @param offsetY
     *         Vertical offset on the y-axis.
     * @param separation
     *         The number of pixels that should be used as divider between each sprite.
     */
    public void printSpritesHorizontally(@NotNull List<PixelSprite> pixelSprites, @NotNull Display targetDisplay, int offsetX, int offsetY, int separation) {
        int totalOffsetX = offsetX;
        for (PixelSprite pixelSprite : pixelSprites) {
            printSprite(pixelSprite, targetDisplay, totalOffsetX, offsetY);
            totalOffsetX = totalOffsetX + pixelSprite.getXDimension() + separation;
        }
    }

    private void internalPrintPixel(@NotNull Display targetDisplay, PixelState pixelState, int yPixel, int xPixel, boolean invert) {
        if (invert) {
            pixelState = invertPixelState(pixelState);
        }

        targetDisplay.setPixel(pixelState, xPixel, yPixel);
    }

    @NotNull
    private PixelState invertPixelState(PixelState pixelState) {
        PixelState invertedPixelState;
        if (pixelState == PixelState.ON) {
            invertedPixelState = PixelState.OFF;
        } else {
            invertedPixelState = PixelState.ON;
        }
        return invertedPixelState;
    }

}