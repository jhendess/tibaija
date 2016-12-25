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

import org.junit.Test;
import org.xlrnet.tibaija.commons.TIMathUtils;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.test.Assertions;
import org.xlrnet.tibaija.test.TestUtils;

import static org.junit.Assert.assertEquals;

public class TIMathUtilsTest {

    @Test(expected = TIArgumentException.class)
    public void testFactorial_invalid_decimal() {
        TIMathUtils.factorial(5.4);
    }

    @Test(expected = TIArgumentException.class)
    public void testFactorial_invalid_negative_1() {
        TIMathUtils.factorial(-4);
    }

    @Test(expected = TIArgumentException.class)
    public void testFactorial_invalid_negative_2() {
        TIMathUtils.factorial(-0.4);
    }

    @Test(expected = TIArgumentException.class)
    public void testFactorial_invalid_overflow() {
        TIMathUtils.factorial(70);
    }

    @Test
    public void testFactorial_recursion_end() throws Exception {
        // Basic recursion end conditions
        assertEquals(1, TIMathUtils.factorial(1), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(1, TIMathUtils.factorial(0), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(1.772453851, TIMathUtils.factorial(-0.5), TestUtils.DEFAULT_TOLERANCE);
    }

    @Test
    public void testFactorial_valid() throws Exception {
        // Some values for testing...
        assertEquals(24, TIMathUtils.factorial(4), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(479001600, TIMathUtils.factorial(12), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(52.342777, TIMathUtils.factorial(4.5), TestUtils.DEFAULT_TOLERANCE);
    }

    @Test(expected = TIArgumentException.class)
    public void testImaginaryNthPower_invalid() {
        TIMathUtils.imaginaryNthPower(-1);
    }

    @Test
    public void testImaginaryNthPower_valid() {
        Assertions.assertComplexValue(TIMathUtils.imaginaryNthPower(0), 1, 0);
        Assertions.assertComplexValue(TIMathUtils.imaginaryNthPower(1), 0, 1);
        Assertions.assertComplexValue(TIMathUtils.imaginaryNthPower(2), -1, 0);
        Assertions.assertComplexValue(TIMathUtils.imaginaryNthPower(3), 0, -1);
    }

    @Test(expected = TIArgumentException.class)
    public void testNumberOfCombinations_invalid_decimal() throws Exception {
        TIMathUtils.numberOfCombinations(0.5, 0.5);
    }

    @Test(expected = TIArgumentException.class)
    public void testNumberOfCombinations_invalid_negative() throws Exception {
        TIMathUtils.numberOfCombinations(-1, -5);
    }

    @Test
    public void testNumberOfCombinations_valid() throws Exception {
        assertEquals(1, TIMathUtils.numberOfCombinations(0, 0), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(10, TIMathUtils.numberOfCombinations(5, 3), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(3176716400L, TIMathUtils.numberOfCombinations(80, 7), TestUtils.BIG_TOLERANCE);
    }

    @Test(expected = TIArgumentException.class)
    public void testNumberOfPermutations_invalid_decimal() throws Exception {
        TIMathUtils.numberOfPermutations(0.5, 0.5);
    }

    @Test(expected = TIArgumentException.class)
    public void testNumberOfPermutations_invalid_negative() throws Exception {
        TIMathUtils.numberOfPermutations(-1, -5);
    }

    @Test
    public void testNumberOfPermutations_valid() throws Exception {
        assertEquals(1, TIMathUtils.numberOfPermutations(0, 0), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(60, TIMathUtils.numberOfPermutations(5, 3), TestUtils.DEFAULT_TOLERANCE);
        assertEquals(1168675200L, TIMathUtils.numberOfPermutations(35, 6), TestUtils.BIG_TOLERANCE);
    }
}