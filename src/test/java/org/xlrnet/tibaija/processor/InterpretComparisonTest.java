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

package org.xlrnet.tibaija.processor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xlrnet.tibaija.exception.TIArgumentException;

/**
 * Tests for interpreting logical expressions
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretComparisonTest extends AbstractTI83PlusTest {

    /**
     * Equals with complex/real mix is not allowed! *
     */
    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_equals_complex_left() {
        calculator.interpret("123456.789i=123456.789");
    }

    /**
     * Equals with complex/real mix is not allowed! *
     */
    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_equals_complex_right() {
        calculator.interpret("123.789=123456.789i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_greater_equals_complex() {
        calculator.interpret("456i≥456i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_greater_than_complex() {
        calculator.interpret("456>123i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_less_equals_complex() {
        calculator.interpret("123i≤123i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_less_than_complex() {
        calculator.interpret("123i<456");
    }

    /**
     * Not-Equals with complex/real mix is not allowed! *
     */
    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_comparison_not_equals_complex() {
        calculator.interpret("12345.678i≠3.14");
    }

    @Test
    public void testInterpret_validProgram_comparison_equals_1() {
        calculator.interpret("1=1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_equals_2() {
        calculator.interpret("0=0");
        verifyLastResultValue(1);
    }

    /**
     * Equals with both complex is allowed! *
     */
    @Test
    public void testInterpret_validProgram_comparison_equals_complex() {
        calculator.interpret("123456.789i=123456.789i");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_equals_1() {
        calculator.interpret("123≥456");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_equals_2() {
        calculator.interpret("123≥123");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_equals_3() {
        calculator.interpret("456≥123");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_than_1() {
        calculator.interpret("123>456");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_than_2() {
        calculator.interpret("123>123");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_greater_than_3() {
        calculator.interpret("456>123");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_equals_1() {
        calculator.interpret("123≤456");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_equals_2() {
        calculator.interpret("123≤123");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_equals_3() {
        calculator.interpret("456≤123");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_than_1() {
        calculator.interpret("123<456");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_than_2() {
        calculator.interpret("123<123");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_less_than_3() {
        calculator.interpret("456<123");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_not_equals_1() {
        calculator.interpret("1≠0");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_comparison_not_equals_2() {
        calculator.interpret("1≠1");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_comparison_not_equals_3() {
        calculator.interpret("1234.567≠1234.567");
        verifyLastResultValue(0);
    }

    /**
     * Equals with both complex is not allowed! *
     */
    @Test
    public void testInterpret_validProgram_comparison_not_equals_complex() {
        calculator.interpret("12345.678i≠3.14i");
        verifyLastResultValue(1);
    }

}
