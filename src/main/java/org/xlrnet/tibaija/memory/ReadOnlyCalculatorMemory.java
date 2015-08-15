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
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.processor.ExecutableProgram;

/**
 * This interface defines a read-only access for the internal calculator memory.
 */
public interface ReadOnlyCalculatorMemory {

    /**
     * Returns true if the underlying memory contains a program with the given name.
     *
     * @param programName
     *         Name of the program, must consist of one to eight capital letters or digits.
     * @return true if the underlying memory contains a program with the given name.
     */
    public boolean containsProgram(@NotNull String programName);

    /**
     * Returns the result of the last top-level expression. This is identical to the Ans-Variable on TI calculators.
     * Since there  may be every kind of value in this variable, you should query the type first to avoid type
     * mismatches.
     *
     * @return The result of the last top-level expression.
     */
    @NotNull
    public Value getLastResult();

    /**
     * Returns the stored value of a given list variable. If a variable has not yet been written to, an
     * UndefinedVariableException will be thrown.
     *
     * @param variable
     *         The list variable name from which value should be returned. Must be written uppercase and between one and
     *         five characters without the leading list token "∟". Digits are allowed except for the first character.
     * @return Value of the selected variable.
     */
    @NotNull
    public Value getListVariableValue(@NotNull String variable);

    /**
     * Returns the stored value of a certain element in a given list variable. The first index is always one and not
     * zero! If a variable has not yet been written to, an UndefinedVariableException will be thrown.
     *
     * @param variable
     *         The list variable name from which value should be returned. Must be written uppercase and between one and
     *         five characters without the leading list token "∟". Digits are allowed except for the first character.
     * @param index
     *         Index of the element inside the list. First index is always one. If the dimension is either to big or too
     *         low, an {@link org.xlrnet.tibaija.exception.InvalidDimensionException} will be thrown.
     * @return Value of the selected variable.
     */
    @NotNull
    public Value getListVariableElementValue(@NotNull String variable, int index);

    /**
     * Returns the stored value of a given number variable. If a variable has not yet been written to, the value is
     * zero.
     *
     * @param variable
     *         The number variable name from which value should be returned.
     * @return Value of the selected variable.
     */
    @NotNull
    public Value getNumberVariableValue(@NotNull Variables.NumberVariable variable);

    /**
     * Get a reference to a read-only executable program that was stored in the virtual calculator's memory.
     *
     * @param programName
     *         Name of the program. See program name spec.
     * @return The requested ExecutableProgram.
     * @throws ProgramNotFoundException
     *         Will be thrown if the requested program could not be found.
     */
    @NotNull
    public ExecutableProgram getStoredProgram(@NotNull String programName) throws ProgramNotFoundException;

}
