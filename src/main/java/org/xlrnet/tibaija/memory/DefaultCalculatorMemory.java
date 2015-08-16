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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.exception.InvalidDimensionException;
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.exception.UndefinedVariableException;
import org.xlrnet.tibaija.processor.ExecutableProgram;
import org.xlrnet.tibaija.util.ValidationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Returns the stored value of a certain element in a given list variable. The first index is always one and not
     * zero! If a variable has not yet been written to, an UndefinedVariableException will be thrown.
     *
     * @param variable
     *         The list variable name from which value should be returned. Must be written uppercase and between one and
     *         five characters without the leading list token "∟". Digits are allowed except for the first character.
     * @param index
     *         Index of the element inside the list. First index is always one. If the dimension is either to big or too
     *         low, an {@link InvalidDimensionException} will be thrown.
     * @return Value of the selected variable.
     */
    @NotNull
    @Override
    public Value getListVariableElementValue(@NotNull String variable, int index) {
        if (!listVariableValueMap.containsKey(variable))
            throw new UndefinedVariableException(variable);
        Value listVariable = listVariableValueMap.get(variable);
        if (index <= 0 || index > listVariable.list().size())
            throw new InvalidDimensionException("Invalid index: " + index, index);

        LOGGER.debug("Accessing element at index {} of list variable {}", index, variable);

        return Value.of(listVariable.list().get(index - 1));
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

    /**
     * Sets a single element within an existing list variable. If the targetted index is exactly one higher than the
     * size of the existing list, then the element will be appended at the end of the list. The first index is always
     * one and not zero! If the target list doesn't exist, an UndefinedVariableException will be thrown unless the index
     * is one - then a new list will be created.
     *
     * @param listName
     *         The variable to which the value should be written. Must be written uppercase and between one and five
     *         characters without the leading list token "∟". Digits are allowed except for the first character.
     * @param index
     *         Index of the element inside the list. First index is always one. If the dimension is either to big or too
     *         low, an {@link org.xlrnet.tibaija.exception.InvalidDimensionException} will be thrown.
     * @param value
     *         The new value for the element at the given index.
     */
    @Override
    public void setListVariableElementValue(@NotNull String listName, int index, @NotNull Value value) {
        if (!listVariableValueMap.containsKey(listName) && index != 1)
            throw new UndefinedVariableException(listName);

        Value listValue = listVariableValueMap.get(listName);
        if (listValue == null)
            listValue = Value.EMPTY_LIST;

        if (index <= 0 || index > listValue.list().size() + 1)
            throw new InvalidDimensionException("Invalid index: " + index, index);

        Complex complex = value.complex();
        List<Complex> modifiableList = Lists.newCopyOnWriteArrayList(listValue.list());

        if (index == modifiableList.size() + 1) {
            modifiableList.add(complex);
            listVariableValueMap.put(listName, Value.of(modifiableList));
            LOGGER.debug("Appended element {} to list {} at index {}", complex, listName, index);
        } else {
            modifiableList.set(index - 1, complex);
            listVariableValueMap.put(listName, Value.of(modifiableList));
            LOGGER.debug("Set element {} at index {} of list {}", complex, index, listName);
        }
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
     * Changes the size of a list variable. If this increases the size, zero elements will be added to the end of the
     * list; if this decreases the size, elements will be removed starting from the end. The variable does not need to
     * exist for this command to work.
     *
     * @param listName
     *         The variable to which the value should be written. Must be written uppercase and between one and five
     *         characters without the leading list token "∟". Digits are allowed except for the first character.
     * @param newSize
     *          New size of the list. Must not be decimal and less than zero.
     */
    @Override
    public void setListVariableSize(@NotNull String listName, int newSize) {
        checkNotNull(listName);

        Value listValue = listVariableValueMap.get(listName);
        if (listValue == null)
            listValue = Value.EMPTY_LIST;

        if (newSize < 0)
            throw new InvalidDimensionException("Invalid new size: " + newSize, newSize);

        List<Complex> resizedList;

        if (newSize == 0) {
            resizedList = new ArrayList<>();
        } else if (newSize < listValue.list().size()) {
            resizedList = listValue.list().subList(0, newSize);
        } else {
            resizedList = Lists.newCopyOnWriteArrayList(listValue.list());
            while (resizedList.size() < newSize) {
                resizedList.add(Complex.ZERO);
            }
        }

        listVariableValueMap.put(listName, Value.of(resizedList));

        LOGGER.debug("Resized list {} to {} elements", listName, newSize);
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
