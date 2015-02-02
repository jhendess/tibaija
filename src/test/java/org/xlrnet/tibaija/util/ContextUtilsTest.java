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
import org.junit.Test;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.test.TestUtils;

import static org.xlrnet.tibaija.test.Assertions.assertComplexValue;

public class ContextUtilsTest {

    @Test
    public void testExtractNumericalValueFromNumberContext_dot_two() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser(".2").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, .2, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_dot_two_negative() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("‾.2").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, -.2, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_one() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("1").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, 1, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_one_dot_two() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("1.2").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, 1.2, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_one_dot_two_negative() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("‾1.2").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, -1.2, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_ten_negative() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("‾10").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, -10, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_twelve() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("12").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, 12, 0);
    }

    @Test
    public void testExtractNumericalValueFromNumberContext_zero() throws Exception {
        TIBasicParser.NumberContext ctx = TestUtils.createParser("0").number();
        Complex value = ContextUtils.extractValueFromNumberContext(ctx).complex();
        assertComplexValue(value, 0, 0);
    }
}