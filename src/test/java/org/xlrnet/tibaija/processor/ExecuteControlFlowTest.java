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
import org.xlrnet.tibaija.exception.IllegalControlFlowException;

/**
 * Tests for control flow logic.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExecuteControlFlowTest extends AbstractTI83PlusTest {

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_invalidProgram_controlFlow_else_without_then() {
        storeAndExecute(":If 0" +
                ":1" +
                ":Else" +
                ":2" +
                ":End");
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_else_false() {
        storeAndExecute(":If 0:Then" +
                ":2" +
                ":Ans + 3" +
                ":Else" +
                ":Ans + 7" +
                ":End");
        verifyLastResultValue(7);
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_else_true() {
        storeAndExecute(":If 1:Then" +
                ":2" +
                ":Ans + 3" +
                ":Else" +
                ":Ans + 7" +
                ":End");
        verifyLastResultValue(5);
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_single_false() {
        storeAndExecute(":If 0" +
                ":2" +
                ":Ans + 3");
        verifyLastResultValue(3);
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_single_true() {
        storeAndExecute(":If 1" +
                ":2" +
                ":Ans + 3");
        verifyLastResultValue(5);
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_then_false() {
        storeAndExecute(":9" +
                ":If 0:Then" +
                ":2" +
                ":Ans + 3" +
                ":End");
        verifyLastResultValue(9);
    }

    @Test
    public void testExecute_validProgram_controlFlow_if_then_true() {
        storeAndExecute(":If 1:Then" +
                ":2" +
                ":Ans + 3" +
                ":End");
        verifyLastResultValue(5);
    }

    @Test
    public void testExecute_validProgram_controlFlow_nested_single_false() {
        storeAndExecute(":If 0:Then" +
                ":If 1" +
                ":1" +
                ":Else" +
                ":2" +
                ":End");
        verifyLastResultValue(2);
    }

    @Test
    public void testExecute_validProgram_controlFlow_nested_then_false() {
        storeAndExecute(":If 0:Then" +
                ":If 1:Then" +
                ":1" +
                ":Else" +
                ":2" +
                ":End" +
                ":Else" +
                ":3" +
                ":End");
        verifyLastResultValue(3);
    }
}
