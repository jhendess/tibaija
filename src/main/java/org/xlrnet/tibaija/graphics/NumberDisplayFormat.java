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

/**
 * Format for displaying numbers
 */
public enum NumberDisplayFormat {

    /**
     * Normal number mode. Scientific notation will be used automatically for large enough numbers (10 000 000 000 or
     * higher), negative numbers large enough in absolute value (-10 000 000 000 or lower), or numbers close enough to
     * 0
     * (less than .001 and greater than -.00
     */
    NORMAL,

    /**
     * Scientific notation mode. A (possibly fractional) number between 1 and 10 (not including 10) multiplied by a
     * power of 10.
     */
    SCIENTIFIC,

    /**
     * Engineering notation mode. This is a variation on scientific notation in
     * which the exponent is restricted to be a multiple of 3 (and the mantissa can range between 1 and 1000, not
     * including 1000 itself)
     */
    ENGINEERING

}
