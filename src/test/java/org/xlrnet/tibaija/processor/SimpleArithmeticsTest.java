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

import org.junit.Ignore;
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
    @Ignore("Ambiguity of (3³)(√(54)) and 3(³√(54)) cannot be resolved - this WILL cause errors!")
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
        verifyLastResultValue(0, 3);
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

    /**
     * -> 3i² must be interpreted as 3*(i²) = -3
     */
    @Test
    public void testInterpret_validProgram_imaginary_precedence_1() throws Exception {
        calculator.interpret("3i²");
        verifyLastResultValue(-3);
    }

    /**
     * -> ii² must be interpreted as i*(i²)
     */
    @Test
    public void testInterpret_validProgram_imaginary_precedence_2() throws Exception {
        calculator.interpret("ii²");
        verifyLastResultValue(0, -1);
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
    public void testInterpret_validProgram_nCr_1() throws Exception {
        calculator.interpret("40 nCr 8");
        verifyLastResultValueWithBigTolerance(76904685L);
    }

    @Test
    public void testInterpret_validProgram_nCr_2() throws Exception {
        calculator.interpret("8 nCr 70");
        verifyLastResultValue(0);
    }

    @Test
    public void testInterpret_validProgram_nPr_1() throws Exception {
        calculator.interpret("8 nPr 4");
        verifyLastResultValue(1680);
    }

    @Test
    public void testInterpret_validProgram_nPr_2() throws Exception {
        calculator.interpret("4 nPr 80");
        verifyLastResultValue(0);
    }

    /**
     * Make sure that "8 nPr 4i" is interpreted as "(8 nPr 4)*i" i.e. no Exception may be thrown.
     */
    @Test
    public void testInterpret_validProgram_nPr_complex_precedence() throws Exception {
        calculator.interpret("8 nPr 4i");
        verifyLastResultValue(0, 1680);
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
    public void testInterpret_validProgram_squared_2() throws Exception {
        calculator.interpret("4²²");
        verifyLastResultValue(256);
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
    public void testInterpret_validProgram_squareroot_minus() throws Exception {
        calculator.interpret("√(‾1");
        verifyLastResultValue(0, 1);
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

    @Test
    public void testInterpret_validProgram_xroot_complex_lhs_normal() throws Exception {
        calculator.interpret("3i×√27");
        verifyLastResultValue(-2.964383781, 0.4608999853);
    }

    @Test
    public void testInterpret_validProgram_xroot_complex_lhs_parentheses() throws Exception {
        calculator.interpret("(3i)×√27");
        verifyLastResultValue(0.4548324228, -0.8905770417);
    }

    @Test
    public void testInterpret_validProgram_xroot_complex_rhs_normal() throws Exception {
        calculator.interpret("3×√27i");
        verifyLastResultValue(0, 3);
    }

    @Test
    public void testInterpret_validProgram_xroot_complex_rhs_parentheses() throws Exception {
        calculator.interpret("3×√(27i)");
        verifyLastResultValue(2.598076211, 1.5);
    }

    @Test
    public void testInterpret_validProgram_xroot_decimal() throws Exception {
        calculator.interpret("2.5×√5.6568542");
        verifyLastResultValue(2);
    }
}
