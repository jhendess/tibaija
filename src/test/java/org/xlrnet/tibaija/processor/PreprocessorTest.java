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
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.exception.PreprocessException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PreprocessorTest {

    private static final String VALID_PRGM_NAME = "TEST";

    public Preprocessor preprocessor = new Preprocessor();

    @Test
    public void testComplexLabel() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":If X:Then:Goto B:Lbl A:End:While 1:Lbl B:End");
        List<TIBasicParser.CommandContext> labelA = executableProgram.getLabelCommands("A");
        assertNotNull(labelA);
        assertEquals(5, labelA.size());
        List<TIBasicParser.CommandContext> labelB = executableProgram.getLabelCommands("B");
        assertNotNull(labelB);
        assertEquals(2, labelB.size());
    }

    @Test
    public void testEmptyLineProgram() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":");
        assertEquals(VALID_PRGM_NAME, executableProgram.getProgramName());
        assertEquals(":", executableProgram.getOriginalSource());
        assertNotNull(executableProgram.getMainProgramContext());
    }

    @Test
    public void testEmptyProgram() {
        preprocessor.preprocessProgramCode(VALID_PRGM_NAME, "");
    }

    @Test
    public void testMultiLabelProgram() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":Lbl A:Lbl B:0");
        List<TIBasicParser.CommandContext> labelA = executableProgram.getLabelCommands("A");
        assertNotNull(labelA);
        assertEquals(3, labelA.size());
        List<TIBasicParser.CommandContext> labelB = executableProgram.getLabelCommands("B");
        assertNotNull(labelB);
        assertEquals(2, labelB.size());
    }

    @Test
    public void testOverdefinedLabel() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":Lbl A:Lbl A");
        List<TIBasicParser.CommandContext> labelA = executableProgram.getLabelCommands("A");
        assertNotNull(labelA);
        assertEquals(2, labelA.size());
    }

    @Test
    public void testRecursiveLabel() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":If 1:Lbl A:Goto B:Lbl B");
        List<TIBasicParser.CommandContext> labelA = executableProgram.getLabelCommands("A");
        assertNotNull(labelA);
        assertEquals(3, labelA.size());
        List<TIBasicParser.CommandContext> labelB = executableProgram.getLabelCommands("B");
        assertNotNull(labelB);
        assertEquals(1, labelB.size());
    }

    @Test
    public void testRecursiveLabelBlock() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":If 1:Then:Lbl A:Goto B:End:Lbl B");
        List<TIBasicParser.CommandContext> labelA = executableProgram.getLabelCommands("A");
        assertNotNull(labelA);
        assertEquals(4, labelA.size());
        List<TIBasicParser.CommandContext> labelB = executableProgram.getLabelCommands("B");
        assertNotNull(labelB);
        assertEquals(1, labelB.size());
    }

    @Test
    public void testSimpleCommandProgram() {
        preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":0");
    }

    @Test
    public void testSimpleLabelProgram() {
        ExecutableProgram executableProgram = preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":Lbl A:0");
        List<TIBasicParser.CommandContext> labelCommands = executableProgram.getLabelCommands("A");
        assertNotNull(labelCommands);
        assertEquals(2, labelCommands.size());
    }

    @Test(expected = PreprocessException.class)
    public void testSyntaxError() {
        preprocessor.preprocessProgramCode(VALID_PRGM_NAME, ":Label A");
    }

}