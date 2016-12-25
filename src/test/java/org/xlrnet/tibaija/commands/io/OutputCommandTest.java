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

package org.xlrnet.tibaija.commands.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.processor.AbstractTI83PlusTest;

import static org.mockito.Mockito.verify;

/**
 * Test cases for the output command.
 */
@RunWith(MockitoJUnitRunner.class)
public class OutputCommandTest extends AbstractTI83PlusTest {

    @Test(expected = IllegalTypeException.class)
    public void testOutput_fail_param_Type() {
        storeAndExecute(":Output(\"A\",2,\"Hello World");
    }

    @Test(expected = TIArgumentException.class)
    public void testOutput_fail_param_imaginary() {
        storeAndExecute(":Output(1i+2,1,\"Hello World");
    }

    @Test(expected = TIArgumentException.class)
    public void testOutput_fail_param_none() {
        storeAndExecute(":Output(");
    }

    @Test(expected = TIArgumentException.class)
    public void testOutput_fail_param_toomany() {
        storeAndExecute(":Output(\"9\",1,\"Hello\",\"World\"");
    }

    @Test(expected = TIArgumentException.class)
    public void testOutput_fail_screenBounds_1() {
        storeAndExecute(":Output(9,1,\"Hello World");
    }

    @Test
    public void testOutput_success_basic() {
        storeAndExecute(":Output(1,2,\"Hello World");
        verify(getEnvironment().getHomeScreen()).printAt(Value.of("Hello World"), 2, 1);
    }

}