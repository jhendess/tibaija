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

import org.xlrnet.tibaija.exception.IllegalControlFlowException;

/**
 * Container class for control flow elements. Instances are supposed to be used in connection with the control flow
 * logic stack.
 */
public class ControlFlowElement {

    private final ControlFlowToken token;

    private final int line;

    private final int charIndex;

    private final boolean repeatable;

    private int commandIndex;

    private boolean lastEvaluation;

    public ControlFlowElement(int line, int charIndex, ControlFlowToken token, boolean lastEvaluation, boolean repeatable) {
        this.token = token;
        this.lastEvaluation = lastEvaluation;
        this.line = line;
        this.charIndex = charIndex;
        this.repeatable = repeatable;
    }

    public int getCharIndex() {
        return charIndex;
    }

    public int getCommandIndex() {
        if (commandIndex < 0)
            throw new IllegalControlFlowException(line, charIndex, "Internal error - illegal command index: " + commandIndex);
        return commandIndex;
    }

    public void setCommandIndex(int commandIndex) {
        this.commandIndex = commandIndex;
    }

    public boolean getLastEvaluation() {
        return lastEvaluation;
    }

    public void setLastEvaluation(boolean lastEvaluation) {
        this.lastEvaluation = lastEvaluation;
    }

    public int getLine() {
        return line;
    }

    public ControlFlowToken getToken() {
        return token;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    static enum ControlFlowToken {
        IF, THEN, ELSE, END, WHILE, REPEAT, GOTO, LABEL, FOR
    }

}
