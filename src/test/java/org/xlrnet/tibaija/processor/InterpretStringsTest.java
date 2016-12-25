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
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.PreprocessException;
import org.xlrnet.tibaija.memory.StringVariable;

/**
 * Tests for interpreting strings.
 */
@RunWith(MockitoJUnitRunner.class)
public class InterpretStringsTest extends AbstractTI83PlusTest {

    @Test(expected = IllegalTypeException.class)
    public void testInterpret_invalidProgram_string_add_number() {
        storeAndExecute(":\"Hello\"+2");
    }

    @Test(expected = Exception.class)
    public void testInterpret_invalidProgram_string_concat() {
        storeAndExecute(":\"Hello\"\"World\"");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_string_singleQuote1() {
        storeAndExecute(":'Hello World");
    }

    @Test(expected = PreprocessException.class)
    public void testInterpret_invalidProgram_string_singleQuote2() {
        storeAndExecute(":'Hello World'");
    }

    @Test
    public void testInterpret_validProgram_string_1() {
        storeAndExecute(":\"Hello World");
        verifyLastResultValue("Hello World");
    }

    @Test
    public void testInterpret_validProgram_string_2() {
        storeAndExecute(":\"Hello World\"");
        verifyLastResultValue("Hello World");
    }

    @Test
    public void testInterpret_validProgram_string_3() {
        storeAndExecute(":\"Hello World->");
        verifyLastResultValue("Hello World->");
    }

    @Test
    public void testInterpret_validProgram_string_add_1() {
        storeAndExecute(":\"Hello\"+\"World\"");
        verifyLastResultValue("HelloWorld");
    }

    @Test
    public void testInterpret_validProgram_string_add_2() {
        storeAndExecute(":\"Hello\"+\" \"+\"World\"");
        verifyLastResultValue("Hello World");
    }

    @Test
    public void testInterpret_validProgram_string_concat_save() {
        storeAndExecute(":\"Hello\"+\" \"+\"World\"->Str1");
        verifyStringVariableValue(StringVariable.Str1, "Hello World");
    }

    @Test
    public void testInterpret_validProgram_string_save() {
        storeAndExecute(":\"Hello World\"->Str1");
        verifyStringVariableValue(StringVariable.Str1, "Hello World");
    }

    @Test
    public void testInterpret_validProgram_string_variable_empty() {
        storeAndExecute(":Str1");
        verifyLastResultValue("");
    }

    @Test
    public void testInterpret_validProgram_string_variable_saved() {
        storeAndExecute(":\"Hello\"+\" \"+\"World\"->Str1" +
                ":1" +
                ":Str1");
        verifyLastResultValue("Hello World");
    }


}
