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

import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.exception.TIArgumentException;

import static org.apache.commons.math3.util.CombinatoricsUtils.factorialDouble;
import static org.xlrnet.tibaija.util.NumberUtils.isInteger;

/**
 * Collection of helper methods for mathematical functions in TI-Basic.
 */
public class TIMathUtils {

    /**
     * This is the standalone factorial function of TI-Basic, where n! = n*(n-1)! and 0! = 1, n an nonnegative integer.
     * The function also works for arguments that are half an odd integer and greater than -1/2. (−1/2)! is defined as
     * the
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
            throw new TIArgumentException("Value must be dividable by 0.5", value);

        if (value < -0.5)
            throw new TIArgumentException("Value must be greater than -0.5", value);

        if (value > 69.5)
            throw new TIArgumentException("Value must be less or equal to 69.5", value);

        if (value == -0.5) {       // See specification in JavaDoc
            return Math.sqrt(Math.PI);
        } else if (value == 0) {  // End of recursion
            return 1;
        } else {                  // Recursive invocation
            return value * factorial(value - 1);
        }
    }

    /**
     * Calculates the n-th root of two complex values. Calculation is based on x = d√(r) = r^(1/d).
     *
     * @param degree
     *         The degree i.e. left side of the root expression
     * @param radicand
     *         The radicand i.e. right side of the root expression
     * @return The n-th complex root.
     */
    @NotNull
    public static Complex complexNthRoot(@NotNull Complex degree, @NotNull Complex radicand) throws TIArgumentException {
        return radicand.pow(Complex.ONE.divide(degree));
    }

    /**
     * Calculates the n-th power of i.
     * I.e.: i^0 = 1; i^1 = i i^2 = -1
     *
     * @param imaginaryCount
     *         The power with which i should be multiplied. Must be greater or equal to zero.
     * @return The n-th power of i.
     */
    @NotNull
    public static Complex imaginaryNthPower(int imaginaryCount) throws TIArgumentException {
        if (imaginaryCount < 0)
            throw new TIArgumentException("Power must be zero or greater", imaginaryCount);

        if (imaginaryCount == 0)
            return Complex.ONE;
        else
            return Complex.I.multiply(imaginaryNthPower(imaginaryCount - 1));
    }

    /**
     * Calculates the number of permutations, defined as a nPr b = a!/(a-b)!, where a and b are nonnegative
     * integers. If a-b is less than zero, zero will be returned in total.
     *
     * @param lhs
     *         Left side of the expression.
     * @param rhs
     *         Right side of the expression.
     * @return Number of permutations.
     * @throws org.xlrnet.tibaija.exception.TIArgumentException
     *         Will be thrown if either side is negative or if any is a non-integer.
     */
    public static double numberOfPermutations(double lhs, double rhs) throws TIArgumentException {
        if (lhs < 0 || rhs < 0)
            throw new TIArgumentException("Arguments may not be less than zero", lhs, rhs);
        if (!isInteger(lhs) || !isInteger(rhs))
            throw new TIArgumentException("Arguments must be integers", lhs, rhs);
        if (lhs - rhs < 0)
            return 0;       // Default behaviour of TI-Basic

        return factorialDouble((int) lhs) / factorialDouble((int) (lhs - rhs));
    }

    /**
     * Calculates the number of combinations (or binomial coefficient), defined as a nCr b = a!/(b!*(a-b)!), where a
     * and b are nonnegative integers. If a-b is less than zero, zero will be returned in total.
     *
     * @param lhs
     *         Left side of the expression.
     * @param rhs
     *         Right side of the expression.
     * @return Number of permutations.
     * @throws org.xlrnet.tibaija.exception.TIArgumentException
     *         Will be thrown if either side is negative or if any is a non-integer.
     */
    public static double numberOfCombinations(double lhs, double rhs) {
        if (lhs < 0 || rhs < 0)
            throw new TIArgumentException("Arguments may not be less than zero", lhs, rhs);
        if (!isInteger(lhs) || !isInteger(rhs))
            throw new TIArgumentException("Arguments must be integers", lhs, rhs);
        if (lhs - rhs < 0)
            return 0;       // Default behaviour of TI-Basic

        return factorialDouble((int) lhs) / (factorialDouble((int) rhs) * factorialDouble((int) (lhs - rhs)));
    }
}
