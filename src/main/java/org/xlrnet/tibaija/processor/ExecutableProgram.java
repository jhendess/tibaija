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

import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.exception.LabelNotFoundException;

import java.util.Map;

/**
 * A container that contains parsed TI-Basic program code.
 * <p/>
 * It provides access to a list of all commands in the whole program and must provide the list of commands after each
 * label in the code to allow fast goto-execution.
 */
public class ExecutableProgram {

    String programName;

    CharSequence originalSource;

    TIBasicParser.ProgramContext mainProgramContext;

    Map<String, Integer> internalLabelMap;

    public Integer getLabelJumpTarget(String labelName) throws LabelNotFoundException {
        Integer targetCommand = internalLabelMap.get(labelName);
        if (targetCommand == null)
            throw new LabelNotFoundException(-1, -1, programName, labelName);
        return targetCommand;
    }

    public TIBasicParser.ProgramContext getMainProgramContext() {
        return mainProgramContext;
    }

    protected void setMainProgramContext(TIBasicParser.ProgramContext mainProgramContext) {
        this.mainProgramContext = mainProgramContext;
    }

    public CharSequence getOriginalSource() {
        return originalSource;
    }

    protected void setOriginalSource(CharSequence originalSource) {
        this.originalSource = originalSource;
    }

    public CharSequence getProgramName() {
        return programName;
    }

    protected void setProgramName(String programName) {
        this.programName = programName;
    }

    protected void setInternalLabelMap(Map<String, Integer> internalLabelMap) {
        this.internalLabelMap = internalLabelMap;
    }
}
