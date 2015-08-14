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

import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.exception.UndefinedVariableException;
import org.xlrnet.tibaija.processor.ExecutableProgram;
import org.xlrnet.tibaija.util.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.xlrnet.tibaija.util.ValueUtils.checkValueType;

/**
 * Default implementation of the TI-Basic memory model.
 */
public class DefaultCalculatorMemory implements CalculatorMemory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalculatorMemory.class);

    private final Map<Variables.NumberVariable, Value> numberVariableValueMap;

    private final Map<String, Value> listVariableValueMap = new HashMap<>();

    private Value lastResult = Value.of(0);

    private Map<String, ExecutableProgram> programMap = new HashMap<>();

    /**
     * Creates a new instance of a TI-Basic capable calculator's memory model.
     */
    public DefaultCalculatorMemory() {
        numberVariableValueMap = newEnumValueMapWithDefault(Variables.NumberVariable.class, Value.ZERO);
    }

    /**
     * Returns true if the underlying memory contains a program with the given name.
     *
     * @param programName
     *         Name of the program, must consist of one to eight capital letters or digits.
     * @return true if the underlying memory contains a program with the given name.
     */
    @Override
    public boolean containsProgram(@NotNull String programName) {
        checkNotNull(programName);
        checkArgument(ValidationUtils.isValidProgramName(programName));

        return programMap.containsKey(programName);

    }

    @NotNull
    @Override
    public Value getLastResult() {
        return lastResult;
    }

    @Override
    public void setLastResult(@NotNull Value value) {
        checkNotNull(value);
        this.lastResult = value;

        LOGGER.debug("Updated ANS variable to value {} of type {}", lastResult.getValue(), lastResult.getType());
    }

    @NotNull
    @Override
    public Value getListVariableValue(@NotNull String variable) throws UndefinedVariableException {
        if (!listVariableValueMap.containsKey(variable))
            throw new UndefinedVariableException(variable);
        return listVariableValueMap.get(variable);
    }

    @NotNull
    @Override
    public Value getNumberVariableValue(@NotNull Variables.NumberVariable variable) {
        checkNotNull(variable);
        return numberVariableValueMap.get(variable);
    }

    @NotNull
    @Override
    public ExecutableProgram getStoredProgram(@NotNull String programName) throws ProgramNotFoundException {
        checkNotNull(programName);

        if (!programMap.containsKey(programName))
            throw new ProgramNotFoundException(programName);

        return programMap.get(programName);
    }

    @Override
    public void setListVariableValue(@NotNull String listName, @NotNull Value value) {
        checkNotNull(listName);
        checkNotNull(value);
        checkValueType(value, Variables.VariableType.LIST);

        listVariableValueMap.put(listName, value);
        LOGGER.debug("Changed value in list variable {} to {}", listName, value);
    }

    @Override
    public void setNumberVariableValue(@NotNull Variables.NumberVariable variable, @NotNull Value value) {
        checkNotNull(variable);
        checkValueType(value, Variables.VariableType.NUMBER);

        numberVariableValueMap.put(variable, value);
        LOGGER.debug("Changed value in numerical variable {} to {}", variable, value);
    }

    @Override
    public void storeProgram(@NotNull String programName, @NotNull ExecutableProgram programCode) throws DuplicateProgramException {
        checkNotNull(programName);
        checkNotNull(programCode);
        checkArgument(ValidationUtils.isValidProgramName(programName));

        if (programMap.containsKey(programName))
            throw new DuplicateProgramException(programName);
        else
            programMap.put(programName, programCode);

        LOGGER.debug("Stored new program {}", programName);
    }

    /**
     * Takes an Enum class and creates a new map with each enum value as key and the given default value as the value.
     *
     * @param numberVariableClass
     *         An enum class that is suppossed to become the key of maps.
     * @param defaultValue
     *         The default value for all entries.
     * @param <T>
     *         A class extending Enum.
     * @return A new map with each enum value as key and the given default value as the value.
     */
    @NotNull
    private <T extends Enum<T>> Map<T, Value> newEnumValueMapWithDefault(@NotNull Class<T> numberVariableClass, @NotNull final Value defaultValue) {
        Map<T, Value> valueMap = new HashMap<>();
        EnumUtils.getEnumMap(numberVariableClass).forEach((name, enumObject) -> valueMap.put(enumObject, defaultValue));
        return valueMap;
    }
}
