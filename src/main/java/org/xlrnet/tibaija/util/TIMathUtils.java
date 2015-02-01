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

package org.xlrnet.tibaija.util;

import com.google.common.collect.ImmutableList;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.memory.Value;

/**
 * Collection of helper methods for mathematical functions in TI-Basic.
 */
public class TIMathUtils {

    /**
     * This is the factorial function of TI-Basic, where n! = n*(n-1)! and 0! = 1, n an nonnegative integer. The
     * function also works for arguments that are half an odd integer and greater than -1/2. (−1/2)! is defined as the
     * square root of pi.
     * <p/>
     * Specification according to <a>http://tibasicdev.wikidot.com/factorial</a>.
     *
     * @param value
     *         The number from which a factorial should be calculated.
     * @return The result of the factorial operation.
     * @throws TIArgumentException
     *         Will be thrown if the input is out of range. This happens on values greater than 69.5, values less than
     *         -0.5 and values that are not dividable by 0.5.
     */
    public static double factorial(double value) throws TIArgumentException {
        // Check if value is dividable by 0.5
        if ((value % 0.5) != 0)
            throw new TIArgumentException("Value must be dividable by 0.5", ImmutableList.of(Value.of(value)));

        if (value < -0.5)
            throw new TIArgumentException("Value must be greater than -0.5", ImmutableList.of(Value.of(value)));

        if (value > 69.5)
            throw new TIArgumentException("Value must be less or equal to 69.5", ImmutableList.of(Value.of(value)));

        if (value == -0.5) {       // See specification in JavaDoc
            return Math.sqrt(Math.PI);
        } else if (value == 0) {  // End of recursion
            return 1;
        } else {                  // Recursive invocation
            return value * factorial(value - 1);
        }
    }

}
