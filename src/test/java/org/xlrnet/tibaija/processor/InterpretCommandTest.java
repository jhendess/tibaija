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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xlrnet.tibaija.commands.DummyCommand;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.CommandNotFoundException;
import org.xlrnet.tibaija.memory.Parameter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for registering command logic.
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretCommandTest extends AbstractTI83PlusTest {

    Command dummyCommand;

    ImmutableList<Parameter> expectedArgumentList = ImmutableList.of(Parameter.value(Value.of(123)), Parameter.value(Value.of(456)));

    @Before
    public void setup() {
        this.dummyCommand = spy(new DummyCommand());
    }

    @Test(expected = CommandNotFoundException.class)
    public void testCommand_invalidProgram_callFunctionCommandAsExpression() {
        getEnvironment().registerCommandFunction("Test", this.dummyCommand);
        storeAndExecute(":Test(123)+1");
    }

    @Test(expected = CommandNotFoundException.class)
    public void testCommand_invalidProgram_callFunctionCommandAsStatement() {
        getEnvironment().registerCommandStatement("Test", this.dummyCommand);
        storeAndExecute(":Test(123)");
    }

    @Test
    public void testCommand_validProgram_registerCommandFunction() {
        getEnvironment().registerCommandFunction("Test", this.dummyCommand);
        storeAndExecute(":Test(123,456)");
        verify(this.dummyCommand).execute(this.expectedArgumentList);
        verify(this.mockedMemory, never()).setLastResult(any());
    }

    @Test
    public void testCommand_validProgram_registerCommandStatement() {
        getEnvironment().registerCommandStatement("Test", this.dummyCommand);
        storeAndExecute(":Test 123,456");
        verify(this.dummyCommand).execute(this.expectedArgumentList);
        verify(this.mockedMemory, never()).setLastResult(any());
    }

    @Test
    public void testCommand_validProgram_registerExpressionFunction() {
        getEnvironment().registerExpressionFunction("testi", this.dummyCommand);
        storeAndExecute(":testi(123, 456)+1");
        verify(this.dummyCommand).execute(this.expectedArgumentList);
        verifyLastResultValue(124);
    }

}
