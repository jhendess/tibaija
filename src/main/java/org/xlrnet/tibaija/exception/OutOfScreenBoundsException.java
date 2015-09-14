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

package org.xlrnet.tibaija.exception;

/**
 * An exception which indicates that a draw call outside of the valid coordinates was invoked.
 */
public class OutOfScreenBoundsException extends TIGraphicsException {

    private static final long serialVersionUID = -3533307179918262755L;

    private final int drawX;

    private final int drawY;

    private final int maxValidX;

    private final int maxValidY;

    public OutOfScreenBoundsException(String message, int drawX, int drawY, int maxValidX, int maxValidY) {
        super("Illegal draw operation at coordinates " + drawX + "," + drawY + ". Maximum supported is " + maxValidX + "," + maxValidY + ".");
        this.drawX = drawX;
        this.drawY = drawY;
        this.maxValidX = maxValidX;
        this.maxValidY = maxValidY;
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public int getMaxValidX() {
        return maxValidX;
    }

    public int getMaxValidY() {
        return maxValidY;
    }
}
