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

import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for structural issues.
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretStructuralTest extends AbstractTI83PlusTest {

    @Test(expected = Exception.class)
    public void testInterpret_invalidProgram_parentheses_1() throws Exception {
        calculator.interpret("()");
    }

    @Test(expected = Exception.class)
    public void testInterpret_invalidProgram_parentheses_2() throws Exception {
        calculator.interpret("()()");
    }

    @Test
    public void testInterpret_validProgram_emptyProgram() throws Exception {
        calculator.interpret(":");
        verifyZeroInteractions(mockedMemory);
    }

    @Test
    public void testInterpret_validProgram_emptyProgram_noColon() throws Exception {
        calculator.interpret("");
        verifyZeroInteractions(mockedMemory);
    }

    @Test
    public void testInterpret_validProgram_one() throws Exception {
        calculator.interpret(":1");
        verifyLastResultValue(1.0);
    }

    @Test
    public void testInterpret_validProgram_one_noColon() throws Exception {
        calculator.interpret("1");
        verifyLastResultValue(1.0);
    }

}
