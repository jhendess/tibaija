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
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.LabelNotFoundException;
import org.xlrnet.tibaija.exception.PreprocessException;
import org.xlrnet.tibaija.memory.NumberVariable;

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

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_invalidProgram_controlFlow_while_then() {
        storeAndExecute(":While 1" +
                ":Then" +
                ":End");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_invalidProgram_controlFlow_while_then_skipped() {
        storeAndExecute(":While 0" +
                ":Then" +
                ":End");
    }

    @Test(expected = IllegalTypeException.class)
    public void testExecute_invalidProgram_decrementSkip_complex() {
        storeAndExecute(":1i->A" +
                ":DS<(A,5" +
                ":2->B");
    }

    @Test(expected = PreprocessException.class)
    public void testExecute_invalidProgram_decrementSkip_listVariable() {
        storeAndExecute(":1->A" +
                ":DS<(∟A,5" +
                ":2->B");
    }

    @Test(expected = PreprocessException.class)
    public void testExecute_invalidProgram_decrementSkip_number() {
        storeAndExecute(":1i->A" +
                ":DS<(1,5" +
                ":2->B");
    }

    @Test(expected = PreprocessException.class)
    public void testExecute_invalidProgram_goto_invalidLabel() {
        storeAndExecute(":Goto ABC");
    }

    @Test(expected = LabelNotFoundException.class)
    public void testExecute_invalidProgram_goto_missing() {
        storeAndExecute(":Goto A");
    }

    @Test(expected = IllegalTypeException.class)
    public void testExecute_invalidProgram_incrementSkip_complex() {
        storeAndExecute(":1i->A" +
                ":IS>(A,5" +
                ":2->B");
    }

    @Test(expected = PreprocessException.class)
    public void testExecute_invalidProgram_incrementSkip_listVariable() {
        storeAndExecute(":1->A" +
                ":IS>(∟A,5" +
                ":2->B");
    }

    @Test(expected = PreprocessException.class)
    public void testExecute_invalidProgram_incrementSkip_number() {
        storeAndExecute(":1i->A" +
                ":IS>(1,5" +
                ":2->B");
    }

    @Test
    public void testExecute_validProgram_controlFlow_forInIf() {
        storeAndExecute(":If 0" +
                ":Then" +
                ":2" +
                ":Else" +
                ":For(X,0,10" +
                ":1" +
                ":End" +
                ":3" +
                ":End");
        verifyLastResultValue(3);
        verifyNumberVariableValue(NumberVariable.X, 11, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_decrement() {
        storeAndExecute(":5->N" +
                ":0→A" +
                ":For(X,N,1,0-1)" +
                ":A+1→A" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
        // Iteration variable must be *zero* at the end!
        verifyNumberVariableValue(NumberVariable.X, 0, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_noEnd() {
        // Program must be executed only one time when there is no end
        storeAndExecute(":0→A" +
                ":For(X,0,5)" +
                ":A+1→A");
        verifyLastResultValue(1);
        verifyNumberVariableValue(NumberVariable.X, 0, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_noExecBounds_decrement() {
        storeAndExecute(":0→A" +
                ":For(X,0-1,5,0-1)" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(0);
        verifyNumberVariableValue(NumberVariable.X, -1, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_noExecBounds_increment() {
        storeAndExecute(":0→A" +
                ":For(X,1,0-5,1)" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(0);
        verifyNumberVariableValue(NumberVariable.X, 1, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_simple() {
        storeAndExecute(":0→A" +
                ":For(X,0,5)" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(5);
        verifyNumberVariableValue(NumberVariable.X, 6, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_simple_decrement() {
        storeAndExecute(":0→A" +
                ":For(X,5,0,0-2)" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(3);
        verifyNumberVariableValue(NumberVariable.X, -1, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_simple_increment() {
        storeAndExecute(":0→A" +
                ":For(X,0,5,2)" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(3);
        verifyNumberVariableValue(NumberVariable.X, 6, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_for_startEqualsEnd() {
        storeAndExecute(":0→A" +
                ":For(X,2,2" +
                ":A+1→A" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
        verifyNumberVariableValue(NumberVariable.X, 3, 0);
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
    public void testExecute_validProgram_controlFlow_nestedFor() {
        storeAndExecute(
                ":For(A,2,4" +
                        ":For(B,2,3" +
                        ":A*B" +
                        ":End" +
                        ":End");
        verifyLastResultValue(3 * 4);
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
        verifyNumberVariableValue(NumberVariable.B, 4, 0);
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

    @Test(timeout = 500L)
    public void testExecute_validProgram_controlFlow_repeat_false_nested_stop() {
        storeAndExecute(":1→A:1→B" +
                ":Repeat 0" +
                ":Repeat 1" +
                ":B+1→B" +
                ":End" +
                ":A+1→A" +
                ":If A=5:Stop" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
        verifyNumberVariableValue(NumberVariable.B, 5, 0);
    }

    @Test(timeout = 500L)
    public void testExecute_validProgram_controlFlow_repeat_false_stop() {
        // Repeat loop must be called infinitely and then be stopped manually
        storeAndExecute(":0→A" +
                ":Repeat 0" +
                ":A+1→A" +
                ":If A=5:Stop" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_repeat_increment() {
        storeAndExecute(":0→A" +
                ":Repeat A>5" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(6);
        verifyNumberVariableValue(NumberVariable.A, 6, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_repeat_skip() {
        storeAndExecute(":1:" +
                ":If 0:Then" +
                ":Repeat 1" +
                ":2" +
                ":End" +
                ":End");
        verifyLastResultValue(1);
    }

    @Test
    public void testExecute_validProgram_controlFlow_repeat_true_stop() {
        // Repeat loop must be called exactly one time and checked at the end
        storeAndExecute(":0→A" +
                ":Repeat 1" +
                ":A+1→A" +
                ":If A=5:Stop" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_skipFor() {
        storeAndExecute(":If 1" +
                ":Then" +
                ":2" +
                ":Else" +
                ":For(X,0,10" +
                ":1" +
                ":End" +
                ":3" +
                ":End");
        verifyLastResultValue(2);
        verifyNumberVariableValue(NumberVariable.X, 0, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_while_false() {
        storeAndExecute(":1" +
                ":While 0" +
                ":2" +
                ":End");
        verifyLastResultValue(1);
    }

    @Test
    public void testExecute_validProgram_controlFlow_while_false_nested_true() {
        storeAndExecute(":1→A:1→B" +
                ":While 0" +
                ":2→A" +
                ":While 1" +
                ":2→B" +
                ":End" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
        verifyNumberVariableValue(NumberVariable.B, 1, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_while_increment() {
        storeAndExecute(":0→A" +
                ":While A<5" +
                ":A+1→A" +
                ":End");
        verifyLastResultValue(5);
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
    }

    @Test
    public void testExecute_validProgram_controlFlow_while_nested() {
        storeAndExecute(":1→A" +
                ":While A<5" +
                ":A+1→A" +
                ":0→B" +
                ":While B<A" +
                ":B+1→B" +
                ":End" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
        verifyNumberVariableValue(NumberVariable.B, 5, 0);
    }

    @Test
    public void testExecute_validProgram_decrementSkip_basic() {
        storeAndExecute(":3->A" +
                ":DS<(A,0" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 2, 0);
        verifyNumberVariableValue(NumberVariable.B, 2, 0);
    }

    @Test
    public void testExecute_validProgram_decrementSkip_decimal() {
        storeAndExecute(":2.5->A" +
                ":DS<(A,1" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 1.5, 0);
        verifyNumberVariableValue(NumberVariable.B, 2, 0);
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_validProgram_decrementSkip_missingNext_1() {
        storeAndExecute(":0->A" +
                ":DS<(A,2");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_validProgram_decrementSkip_missingNext_2() {
        storeAndExecute(":3->A" +
                ":DS<(A,2");
    }

    @Test
    public void testExecute_validProgram_decrementSkip_skip2() {
        storeAndExecute(":2->A" +
                ":DS<(A,2" +
                ":2->B" +
                ":3->C");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
        verifyNumberVariableValue(NumberVariable.C, 3, 0);
    }

    @Test
    public void testExecute_validProgram_decrementSkip_skip3() {
        storeAndExecute(":0->A" +
                ":DS<(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, -1, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
    }

    @Test
    public void testExecute_validProgram_decrementSkip_skip_1() {
        storeAndExecute(":2->A" +
                ":DS<(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
    }

    @Test
    public void testExecute_validProgram_goto_basic1() {
        storeAndExecute(":1→A" +
                ":Goto A" +
                ":2→A" +
                ":Lbl A");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
    }

    @Test(timeout = 50)
    public void testExecute_validProgram_goto_flow() {
        // If control flow or goto are not implemented correctly, the following code will cause an infinite loop
        storeAndExecute(":If 1:Then" +
                ":Goto A" +
                ":Else" +
                ":While 1" +
                ":Lbl A" +
                ":End");
    }

    @Test
    public void testExecute_validProgram_goto_loop() {
        storeAndExecute(":1→A" +
                ":Lbl A" +
                ":If A<5:Then" +
                ":A+1→A" +
                ":Goto A" +
                ":End");
        verifyNumberVariableValue(NumberVariable.A, 5, 0);
    }

    @Test
    public void testExecute_validProgram_incrementSkip_basic() {
        storeAndExecute(":0->A" +
                ":IS>(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 1, 0);
        verifyNumberVariableValue(NumberVariable.B, 2, 0);
    }

    @Test
    public void testExecute_validProgram_incrementSkip_decimal() {
        storeAndExecute(":.5->A" +
                ":IS>(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 1.5, 0);
        verifyNumberVariableValue(NumberVariable.B, 2, 0);
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_validProgram_incrementSkip_missingNext_1() {
        storeAndExecute(":0->A" +
                ":IS>(A,2");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testExecute_validProgram_incrementSkip_missingNext_2() {
        storeAndExecute(":4->A" +
                ":IS>(A,2");
    }

    @Test
    public void testExecute_validProgram_incrementSkip_skip2() {
        storeAndExecute(":2->A" +
                ":IS>(A,2" +
                ":2->B" +
                ":3->C");
        verifyNumberVariableValue(NumberVariable.A, 3, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
        verifyNumberVariableValue(NumberVariable.C, 3, 0);
    }

    @Test
    public void testExecute_validProgram_incrementSkip_skip3() {
        storeAndExecute(":3->A" +
                ":IS>(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 4, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
    }

    @Test
    public void testExecute_validProgram_incrementSkip_skip_1() {
        storeAndExecute(":2->A" +
                ":IS>(A,2" +
                ":2->B");
        verifyNumberVariableValue(NumberVariable.A, 3, 0);
        verifyNumberVariableValue(NumberVariable.B, 0, 0);
    }

}
