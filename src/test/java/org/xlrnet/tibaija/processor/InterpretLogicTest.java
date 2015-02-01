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
 * Tests for interpreting logical operators (and, or, xor, not)
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretLogicTest extends AbstractTI83PlusTest {

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_and_complex_1() {
        calculator.interpret("1i and 1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_and_complex_2() {
        calculator.interpret("1 and 1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_not_complex() {
        calculator.interpret("not(1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_or_complex_1() {
        calculator.interpret("1i or 1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_or_complex_2() {
        calculator.interpret("1 or 1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_xor_complex_1() {
        calculator.interpret("1i xor 1i");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_logic_xor_complex_2() {
        calculator.interpret("1 xor 1i");
    }

    @Test
    public void testInterpret_validProgram_logic_and_false_1() {
        calculator.interpret("0 and 1");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_and_false_2() {
        calculator.interpret("0 and 0");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_and_true_1() {
        calculator.interpret("1 and 1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_and_true_2() {
        calculator.interpret("1 and 2");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_and_true_3() {
        calculator.interpret("1 and ‾1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_not_false_1() {
        calculator.interpret("not(1");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_not_false_2() {
        calculator.interpret("not(1");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_not_true_1() {
        calculator.interpret("not(0");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_not_true_2() {
        calculator.interpret("not(0)");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_or_false_1() {
        calculator.interpret("0 or 1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_or_false_2() {
        calculator.interpret("0 or 0");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_or_true_1() {
        calculator.interpret("1 or 1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_or_true_2() {
        calculator.interpret("1 or 2");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_or_true_3() {
        calculator.interpret("1 or ‾1");
        verifyLastResultValue(1);
    }

    /**
     * Test if "not(1 and 0" is interpreted correctly as "not((1 and 0))" and not as "not(1) and 0"
     */
    @Test
    public void testInterpret_validProgram_logic_precedence_not_and() {
        calculator.interpret("not(1 and 0");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_xor_false_1() {
        calculator.interpret("0 xor 1");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_logic_xor_false_2() {
        calculator.interpret("0 xor 0");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_xor_true_1() {
        calculator.interpret("1 xor 1");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_xor_true_2() {
        calculator.interpret("1 xor 2");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_logic_xor_true_3() {
        calculator.interpret("1 xor ‾1");
        verifyLastResultValue(0);
    }

}
