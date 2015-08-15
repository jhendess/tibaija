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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.InvalidDimensionException;
import org.xlrnet.tibaija.exception.UndefinedVariableException;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;

import static org.mockito.Mockito.doReturn;

/**
 * Tests for storing values.
 */
@RunWith(MockitoJUnitRunner.class)
public class StoreStatementTest extends AbstractTI83PlusTest {

    @Test(expected = IllegalTypeException.class)
    public void testInterpret_invalidProgram_store_listvalue_numbervariable() {
        calculator.interpret(":{A->A");
    }

    @Test(expected = IllegalTypeException.class)
    public void testInterpret_invalidProgram_store_numberValue_listvariable() {
        doReturn(Value.of(ImmutableList.of(Complex.ONE))).when(mockedMemory).getListVariableValue("A");
        calculator.interpret(":∟A->A");
    }

    @Test
    public void testInterpret_validProgram_store_listvalue_listvariable() {
        calculator.interpret(":{1,2->∟A");
        verifyListVariableValue("A", Complex.valueOf(1), Complex.valueOf(2));
        verifyLastResultValueList(1d, 2d);
    }

    @Test
    public void testInterpret_validProgram_store_numbervalue_numbervariable_1() {
        calculator.interpret("123->A");
        verifyNumberVariableValue(Variables.NumberVariable.A, 123, 0);
        verifyLastResultValue(123);
    }

    @Test
    public void testInterpret_validProgram_store_numbervalue_numbervariable_complex() {
        calculator.interpret("123i+512.1024->B");
        verifyNumberVariableValue(Variables.NumberVariable.B, 512.1024, 123);
        verifyLastResultValue(512.1024, 123);
    }

    @Test
    public void testInterpret_validProgram_store_list_element() {
        storeAndExecute(":{1->∟A" +
                ":2->∟A(1");
        verifyElementInListVariable("A", 0, 2);
        verifyLastResultValue(2);
    }

    @Test
    public void testInterpret_validProgram_store_list_element_calc_index() {
        storeAndExecute(":{1,2->∟A" +
                ":3->∟A(1+1");
        verifyElementInListVariable("A", 1, 3);
        verifyLastResultValue(3);
    }

    @Test(expected = IllegalTypeException.class)
    public void testInterpret_invalidProgram_store_list_in_list_element() {
        storeAndExecute(":{1->∟A" +
                ":{2->∟A(1");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_store_list_element_index_tooHigh() {
        storeAndExecute(":{1->∟A" +
                ":2->∟A(12");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_store_list_element_index_tooLow() {
        storeAndExecute(":{1->∟A" +
                ":2->∟A(0-1");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_store_list_element_index_zero() {
        storeAndExecute(":{1->∟A" +
                ":2->∟A(0");
    }

    @Test(expected = InvalidDimensionException.class)
    public void testInterpret_invalidProgram_store_list_element_index_decimal() {
        storeAndExecute(":{1->∟A" +
                ":2->∟A(1.5");
    }

    @Test(expected = UndefinedVariableException.class)
    public void testInterpret_invalidProgram_store_list_element_missingList_laterElement() {
        storeAndExecute(":1->∟AZY(2");
    }

    @Test
    public void testInterpret_invalidProgram_store_list_element_missingList_firstElement() {
        storeAndExecute(":1->∟AZY(1");
        verifyElementInListVariable("AZY", 0, 1);
    }

    @Test(expected = IllegalTypeException.class)
    public void testInterpret_invalidProgram_store_list_element_index_list() {
        storeAndExecute(":{1->∟A" +
                ":1->∟A(∟A");
    }

    @Test
    public void testInterpret_validProgram_store_list_element_index_add() {
        storeAndExecute(":{1->∟A" +
                ":9->∟A(2");
        verifyElementInListVariable("A", 1, 9);
    }

    // TODO: Write negative tests when other data types have been implemented
}
