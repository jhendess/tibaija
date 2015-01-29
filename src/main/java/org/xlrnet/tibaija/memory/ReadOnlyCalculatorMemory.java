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

package org.xlrnet.tibaija.memory;

import org.jetbrains.annotations.NotNull;

/**
 * This interface defines a read-only access for the internal calculator memory.
 */
public interface ReadOnlyCalculatorMemory {

    /**
     * Returns the result of the last top-level expression. This is identical to the Ans-Variable on TI calculators.
     * Since there  may be every kind of value in this variable, you should query the type first to avoid type
     * mismatches.
     *
     * @return The result of the last top-level expression.
     */
    @NotNull
    public AnswerVariable getLastResult();

    /**
     * Returns the stored value of a given variable. If a variable has not yet been written to, the value is zero.
     *
     * @param variable
     *         The variable from which value should be returned.
     * @return Value of the selected variable.
     */
    @NotNull
    public Value getNumberVariableValue(Variables.NumberVariable variable);

}
