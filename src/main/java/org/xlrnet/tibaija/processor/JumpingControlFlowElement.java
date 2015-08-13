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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Sub-class of {@link ControlFlowElement} that contains a target label to which can be jumped. It thus supports only
 * {@link org.xlrnet.tibaija.processor.ControlFlowElement.ControlFlowToken#GOTO} and {@link
 * org.xlrnet.tibaija.processor.ControlFlowElement.ControlFlowToken#LABEL} as tokens.
 */
public class JumpingControlFlowElement extends ControlFlowElement {

    private final String targetLabel;

    public JumpingControlFlowElement(int line, int charIndex, ControlFlowToken controlFlowToken, String targetLabel) {
        super(line, charIndex, controlFlowToken, false, false);

        checkControlFlowToken(controlFlowToken);

        this.targetLabel = targetLabel;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    private void checkControlFlowToken(ControlFlowToken controlFlowToken) {
        checkArgument(controlFlowToken == ControlFlowToken.GOTO || controlFlowToken == ControlFlowToken.LABEL);
    }
}
