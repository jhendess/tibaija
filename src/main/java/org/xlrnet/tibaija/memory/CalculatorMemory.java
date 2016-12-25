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
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.processor.ExecutableProgram;

/**
 * Interface for accessing the internal memory model of a virtual calculator. This interface provides methods for
 * reading and writing variables and programs to the internal memory.
 */
public interface CalculatorMemory extends ReadOnlyCalculatorMemory {

    /**
     * Sets the internal Ans-variable to the value of the given temporary value. This should only be called after the
     * evaluation of top-level expression. Otherwise inconsistencies may occur.
     *
     * @param value
     *         New value of the last result variable.
     */
    void setLastResult(@NotNull Value value);

    /**
     * Sets a single element within an existing list variable. If the targetted index is exactly one higher than the
     * size of the existing list, then the element will be appended at the end of the list. The first index is always
     * one and not zero! If the target list doesn't exist, an UndefinedVariableException will be thrown unless the
     * index
     * is one - then a new list will be created.
     *
     * @param listVariable
     *         The variable to which the value should be written.
     * @param index
     *         Index of the element inside the list. First index is always one. If the dimension is either to big or too
     *         low, an {@link org.xlrnet.tibaija.exception.InvalidDimensionException} will be thrown.
     * @param value
     *         The new value for the element at the given index.
     */
    void setListVariableElementValue(@NotNull ListVariable listVariable, int index, @NotNull Value value);

    /**
     * Changes the size of a list variable. If this increases the size, zero elements will be added to the end of the
     * list; if this decreases the size, elements will be removed starting from the end. The variable does not need to
     * exist for this command to work.
     *
     * @param listVariable
     *         The variable to which the value should be written.
     * @param newSize
     *         New size of the list. Must not be decimal and less than zero.
     */
    void setListVariableSize(@NotNull ListVariable listVariable, int newSize);

    /**
     * Sets the internal value of the given list variable.
     *
     * @param listVariable
     *         The variable to which the value should be written.
     * @param value
     *         The new value of the selected variable.
     */
    void setListVariableValue(@NotNull ListVariable listVariable, @NotNull Value value);

    /**
     * Sets the internal value of the given number variable.
     *
     * @param variable
     *         The variable to which the value should be written.
     * @param value
     *         The new value of the selected variable.
     */
    void setNumberVariableValue(@NotNull NumberVariable variable, @NotNull Value value);

    /**
     * Sets the internal value of the given string variable.
     *
     * @param variable
     *         The variable to which the value should be written.
     * @param value
     *         The new value of the selected variable.
     */
    void setStringVariableValue(@NotNull StringVariable variable, @NotNull Value value);

    /**
     * Stores the raw code of a program in internal memory.
     *
     * @param programName
     *         Name of the program, must consist of one to eight capital letters or digits.
     * @param programCode
     *         A preprocessed and executable TI-Basic program.
     */
    void storeProgram(String programName, @NotNull ExecutableProgram programCode) throws DuplicateProgramException;

}
