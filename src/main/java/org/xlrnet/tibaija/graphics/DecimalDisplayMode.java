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
 * Configuration values of how to display decimals on the screen.
 */
public enum DecimalDisplayMode {

    /**
     * Scientific notation mode. A (possibly fractional) number between 1 and 10 (not including 10) multiplied by a
     * power of 10.
     */
    SCIENTIFIC(-1),

    /**
     * Engineering notation mode. This is a variation on scientific notation in
     * which the exponent is restricted to be a multiple of 3 (and the mantissa can range between 1 and 1000, not
     * including 1000 itself)
     */
    ENGINEERING(-1),

    /**
     * Float makes the calculator display numbers with a "floating decimal point" — only as many digits
     * after the decimal as needed are displayed (so whole numbers, for example, are shown without any decimal points).
     * This is the default mode, and usually the most useful.
     */
    FLOAT(-1),

    /**
     * Show always zero decimals.
     */
    FIX_0(0),

    /**
     * Show always one decimal.
     */
    FIX_1(1),

    /**
     * Show always two decimals.
     */
    FIX_2(2),

    /**
     * Show always three decimals.
     */
    FIX_3(3),

    /**
     * Show always four decimals.
     */
    FIX_4(4),

    /**
     * Show always five decimals.
     */
    FIX_5(5),

    /**
     * Show always six decimals.
     */
    FIX_6(6),

    /**
     * Show always seven decimals.
     */
    FIX_7(7),

    /**
     * Show always eight decimals.
     */
    FIX_8(8),

    /**
     * Show always nine decimals.
     */
    FIX_9(9);

    int decimalsToDisplay;

    DecimalDisplayMode(int decimalsToDisplay) {
        this.decimalsToDisplay = decimalsToDisplay;
    }

    public int getDecimalsToDisplay() {
        return decimalsToDisplay;
    }
}
