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
 * Tests that control flow elements are not allowed in interpreted mode.
 */
@RunWith(MockitoJUnitRunner.class)
public class IllegalControlFlowTests extends AbstractTI83PlusTest {

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_Else() throws Exception {
        calculator.interpret("Else");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_End() throws Exception {
        calculator.interpret("End");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_Repeat() throws Exception {
        calculator.interpret("Repeat 1");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_Then() throws Exception {
        calculator.interpret("Then");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_While() throws Exception {
        calculator.interpret("While 1");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_decrementSkip() throws Exception {
        calculator.interpret("DS<(X,1");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_if() throws Exception {
        calculator.interpret("If 1");
    }

    @Test(expected = IllegalControlFlowException.class)
    public void testInterpret_invalidProgram_incrementSkip() throws Exception {
        calculator.interpret("IS>(X,1");
    }

}
