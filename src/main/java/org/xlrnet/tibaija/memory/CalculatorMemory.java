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
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.processor.ExecutableProgram;

/**
 * Interface for accessing the internal memory model of a virtual calculator. This interface provides methods for
 * reading and writing variables and programs to the internal memory.
 */
public interface CalculatorMemory extends ReadOnlyCalculatorMemory {

    /**
     * Sets the internal Ans-variable to the value of the given temporary value. This should only be called after the
     * evaluation of top-level
     * expression. Otherwise inconsistencies may occur.
     *
     * @param value
     *         New value of the last result variable.
     */
    public void setLastResult(@NotNull Value value);

    /**
     * Sets the internal value of the given variable.
     *
     * @param variable
     *         The variable to which the value should be written.
     * @param value
     *         The new value of the selected variable.
     */
    public void setNumberVariableValue(@NotNull Variables.NumberVariable variable, @NotNull Value value);

    /**
     * Stores the raw code of a program in internal memory.
     *
     * @param programName
     *         Name of the program, must consist of one to eight capital letters or digits.
     * @param programCode
     *         A preprocessed and executable TI-Basic program.
     */
    public void storeProgram(String programName, @NotNull ExecutableProgram programCode) throws DuplicateProgramException;

}
