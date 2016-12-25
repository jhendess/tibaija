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

import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.complex.Complex;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.InvalidDimensionException;
import org.xlrnet.tibaija.exception.PreprocessException;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.exception.UndefinedVariableException;
import org.xlrnet.tibaija.memory.ListVariable;
import org.xlrnet.tibaija.memory.NumberVariable;

import static org.mockito.Mockito.doReturn;

/**
 * All tests regarding list interpretation.
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretListsTest extends AbstractTI83PlusTest {

    @Test(expected = TIArgumentException.class)
    public void testInterpret_invalidProgram_add_mismatch() {
        this.calculator.interpret("{1} + {1,2}");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_list_index_complex() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(4i");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_list_index_decimal() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(1.5");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_list_index_list() {
        storeAndExecute("{A->∟A" +
                ":∟A(∟A");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_list_index_tooHigh() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(4");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_list_index_tooLow() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(0-1");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_list_index_zero() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(0");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_listname_digit() {
        this.calculator.interpret("∟1");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_listname_toolong() {
        this.calculator.interpret("∟ABCDEF");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_nestedlist() {
        this.calculator.interpret("{2, {2}");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_nestedlist_variable() {
        doReturn(Value.of(ImmutableList.of(Complex.ONE))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("A"));
        this.calculator.interpret("{2, ∟A");
    }

    @Test
    public void testInterpret_validProgram_add_two_lists() {
        this.calculator.interpret("{2+i, 3+2i} + {1,2");
        verifyLastResultValueList(Complex.valueOf(3, 1), Complex.valueOf(5, 2));
    }

    @Test
    @Ignore
    public void testInterpret_validProgram_ans_list_element_access() {
        // Very nasty bug :/
        storeAndExecute(":{1,2,3" +
                ":Ans(2");
        verifyLastResultValue(2);
    }

    @Test
    public void testInterpret_validProgram_list() {
        this.calculator.interpret("{1,2,3,456");
        verifyLastResultValueList(1d, 2d, 3d, 456d);
    }

    @Test
    public void testInterpret_validProgram_listVariable_element_access() {
        storeAndExecute(":{1,2->∟A" +
                ":∟A(2->A");
        verifyNumberVariableValue(NumberVariable.A, 2, 0);
    }

    @Test
    public void testInterpret_validProgram_list_complex() {
        this.calculator.interpret("{123.456i, ‾12.34");
        verifyLastResultValueList(Complex.valueOf(0, 123.456), Complex.valueOf(-12.34));
    }

    @Test
    public void testInterpret_validProgram_list_element_access_variable() {
        storeAndExecute(":2->A" +
                ":{1,2,3}->∟A" +
                ":∟A(A+1");
        verifyLastResultValue(3);
    }

    @Test
    public void testInterpret_validProgram_list_index_listValue() {
        storeAndExecute(":{1,2,3->∟A" +
                ":∟A(∟A(2");
        verifyLastResultValue(2);
    }

    @Test(expected = UndefinedVariableException.class)
    public void testInterpret_validProgram_list_notExisting() {
        storeAndExecute(":∟ABCDE");
    }

    @Test
    public void testInterpret_validProgram_list_single() {
        this.calculator.interpret("{1}");
        verifyLastResultValueList(Complex.valueOf(1));
    }

    /**
     * Test if any postfix operators work with lists.
     */
    @Test
    public void testInterpret_validProgram_list_variable_postfix() {
        doReturn(Value.of(ImmutableList.of(Complex.valueOf(2), Complex.valueOf(3)))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("ABC"));
        this.calculator.interpret("∟ABC²");
        verifyLastResultValueList(4d, 9d);
    }

    /**
     * Test if any prefix operators work with lists.
     */
    @Test
    public void testInterpret_validProgram_list_variable_prefix() {
        doReturn(Value.of(ImmutableList.of(Complex.valueOf(4), Complex.valueOf(9)))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("ABC"));
        this.calculator.interpret("√(∟ABC");
        verifyLastResultValueList(2d, 3d);
    }

    @Test
    public void testInterpret_validProgram_listname_default() {
        doReturn(Value.of(ImmutableList.of(Complex.ONE))).when(this.mockedMemory).getListVariableValue(ListVariable.DEFAULT_2);
        this.calculator.interpret("∟₂");
        verifyLastResultValueList(1d);
    }

    @Test
    public void testInterpret_validProgram_listname_digits() {
        doReturn(Value.of(ImmutableList.of(Complex.ONE))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("A1234"));
        this.calculator.interpret("∟A1234");
        verifyLastResultValueList(1d);
    }

    @Test
    public void testInterpret_validProgram_listname_theta() {
        doReturn(Value.of(ImmutableList.of(Complex.ONE))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("θ1θ2A"));
        this.calculator.interpret("∟θ1θ2A");
        verifyLastResultValueList(1d);
    }

    @Test
    public void testInterpret_validProgram_multiply_implicit_numbervariable() {
        doReturn(Value.of(2)).when(this.mockedMemory).getNumberVariableValue(NumberVariable.A);
        this.calculator.interpret("{1, 2}A");
        verifyLastResultValueList(Complex.valueOf(2), Complex.valueOf(4));
    }

    @Test
    public void testInterpret_validProgram_multiply_implicit_numbervariable_left() {
        doReturn(Value.of(ImmutableList.of(Complex.valueOf(2)))).when(this.mockedMemory).getListVariableValue(ListVariable.fromName("A"));
        doReturn(Value.of(2)).when(this.mockedMemory).getNumberVariableValue(NumberVariable.A);
        this.calculator.interpret("A∟A");
        verifyLastResultValueList(4d);
    }

    @Test
    public void testInterpret_validProgram_multiply_implicit_single_right_list() {
        this.calculator.interpret("{1, 2}(2+3.5i)");
        verifyLastResultValueList(Complex.valueOf(2, 3.5), Complex.valueOf(4, 7));
    }

    @Test
    public void testInterpret_validProgram_multiply_single_right_list() {
        this.calculator.interpret("(2+3.5i) * {1, 2");
        verifyLastResultValueList(Complex.valueOf(2, 3.5), Complex.valueOf(4, 7));
    }

}
