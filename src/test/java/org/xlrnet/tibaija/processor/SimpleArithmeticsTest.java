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
 * Tests that concern simple arithmetics. No trigonomy or logic.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleArithmeticsTest extends AbstractTI83PlusTest {

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_factorial_complex() throws Exception {
        calculator.interpret("4i!");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_factorial_minus_1() throws Exception {
        calculator.interpret("(‾.6)!");
    }

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_factorial_minus_2() throws Exception {
        calculator.interpret("(‾1)!");
    }

    /**
     * Faculty on complex numbers is not allowed!
     */
    @Test(expected = Exception.class)
    public void testInterpret_invalidProgram_faculty_complex() throws Exception {
        calculator.interpret("4i!");
    }

    @Test
    public void testInterpret_validProgram_addition_1() throws Exception {
        calculator.interpret("1+2");
        verifyLastResultValue(3.0);
    }

    @Test
    public void testInterpret_validProgram_addition_2() throws Exception {
        calculator.interpret("123+456");
        verifyLastResultValue(579);
    }

    @Test
    public void testInterpret_validProgram_addition_complex() throws Exception {
        calculator.interpret("1+2+3i+4i");
        verifyLastResultValue(3.0, 7.0);
    }

    @Test
    public void testInterpret_validProgram_complex_number() throws Exception {
        calculator.interpret("456i");
        verifyLastResultValue(0, 456);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_1() throws Exception {
        calculator.interpret("³√(27");
        verifyLastResultValue(3);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_2() throws Exception {
        calculator.interpret("³√(√(729");
        verifyLastResultValue(3);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_ambiguity() throws Exception {
        calculator.interpret("3³√(54");         // Ambiguous clause can mean (3^3)*sqrt(54) or 3*qubicroot(54) -> ???
        verifyLastResultValue(54);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_complex_1() throws Exception {
        calculator.interpret("³√(27i");
        verifyLastResultValue(2.598076211, 1.5);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_complex_2() throws Exception {
        calculator.interpret("³√(27)i");
        verifyLastResultValue(3);
    }

    @Test
    public void testInterpret_validProgram_cubicroot_power() throws Exception {
        calculator.interpret("³√(81^3");
        verifyLastResultValue(81);
    }

    @Test
    public void testInterpret_validProgram_factorial_1() throws Exception {
        calculator.interpret("4!");
        verifyLastResultValue(24);
    }

    @Test
    public void testInterpret_validProgram_factorial_2() throws Exception {
        calculator.interpret("5.5!");
        verifyLastResultValue(287.8852778);
    }

    @Test
    public void testInterpret_validProgram_factorial_minus_1() throws Exception {
        calculator.interpret("‾.5!");       //  Should be interpreted as -(0.5!)
        verifyLastResultValue(-0.8862269255);
    }

    @Test
    public void testInterpret_validProgram_factorial_minus_2() throws Exception {
        calculator.interpret("(‾.5)!");
        verifyLastResultValue(1.772453851);
    }

    @Test
    public void testInterpret_validProgram_multiplication_precedence() throws Exception {
        calculator.interpret("1+4*5");
        verifyLastResultValue(21);
    }

    @Test
    public void testInterpret_validProgram_multiplication_precedence_complex() throws Exception {
        calculator.interpret("1+4*5i+2.5");
        verifyLastResultValue(3.5, 20.0);
    }

    @Test
    public void testInterpret_validProgram_nCr() throws Exception {
        calculator.interpret("8 nCr 70");
        verifyLastResultValue(70);
    }

    @Test
    public void testInterpret_validProgram_nPr() throws Exception {
        calculator.interpret("8 nPr 4");
        verifyLastResultValue(1680);
    }

    @Test
    public void testInterpret_validProgram_negation() throws Exception {
        calculator.interpret("4-‾5");
        verifyLastResultValue(9);
    }

    @Test
    public void testInterpret_validProgram_parentheses_1() throws Exception {
        calculator.interpret("(1)");
        verifyLastResultValue(1);
    }

    @Test
    public void testInterpret_validProgram_parentheses_2() throws Exception {
        calculator.interpret("2(1+2)");
        verifyLastResultValue(6);
    }

    @Test
    public void testInterpret_validProgram_parentheses_3() throws Exception {
        calculator.interpret("((2(3))");
        verifyLastResultValue(6);
    }

    @Test
    public void testInterpret_validProgram_power() throws Exception {
        calculator.interpret("5^2*3^4");
        verifyLastResultValue(2025);
    }

    @Test
    public void testInterpret_validProgram_power_complex() throws Exception {
        calculator.interpret("5^3i*9");
        verifyLastResultValue(0, 1125);
    }

    @Test
    public void testInterpret_validProgram_simple_number() throws Exception {
        calculator.interpret("123");
        verifyLastResultValue(123);
    }

    @Test
    public void testInterpret_validProgram_squared_1() throws Exception {
        calculator.interpret("4²");
        verifyLastResultValue(16);
    }

    @Test
    public void testInterpret_validProgram_squared_imaginary_1() throws Exception {
        calculator.interpret("5i²");
        verifyLastResultValue(-5);
    }

    @Test
    public void testInterpret_validProgram_squared_imaginary_2() throws Exception {
        calculator.interpret("5²i");
        verifyLastResultValue(0, 25);
    }

    @Test
    public void testInterpret_validProgram_squareroot_1() throws Exception {
        calculator.interpret("√(4");
        verifyLastResultValue(2);
    }

    @Test
    public void testInterpret_validProgram_squareroot_2() throws Exception {
        calculator.interpret("√(√(16");
        verifyLastResultValue(2);
    }

    @Test
    public void testInterpret_validProgram_squareroot_3() throws Exception {
        calculator.interpret("√(√(16)4");
        verifyLastResultValue(4);
    }

    @Test
    public void testInterpret_validProgram_subtraction() throws Exception {
        calculator.interpret("1-2");
        verifyLastResultValue(-1.0);
    }

    @Test
    public void testInterpret_validProgram_subtraction_complex() throws Exception {
        calculator.interpret("1-2+3i-4i");
        verifyLastResultValue(-1.0, -1.0);
    }

    @Test
    public void testInterpret_validProgram_xroot() throws Exception {
        calculator.interpret("3×√27");
        verifyLastResultValue(3);
    }

}
