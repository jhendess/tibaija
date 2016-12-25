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
import org.xlrnet.tibaija.commons.ValidationUtil;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.commons.ValueType;
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.exception.InvalidDimensionException;
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.exception.UndefinedVariableException;
import org.xlrnet.tibaija.processor.ExecutableProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.xlrnet.tibaija.commons.Preconditions.checkValueType;

/**
 * Default implementation of the TI-Basic memory model.
 */
public class DefaultCalculatorMemory implements CalculatorMemory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalculatorMemory.class);

    private final Map<NumberVariable, Value> numberVariableValueMap;

    private final Map<ListVariable, Value> listVariableValueMap = new HashMap<>();

    private Value lastResult = Value.of(0);

    private Map<String, ExecutableProgram> programMap = new HashMap<>();

    private Map<StringVariable, Value> stringVariableValueMap = new HashMap<>();

    /**
     * Creates a new instance of a TI-Basic capable calculator's memory model.
     */
    public DefaultCalculatorMemory() {
        this.numberVariableValueMap = newEnumValueMapWithDefault(NumberVariable.class, Value.ZERO);
        this.stringVariableValueMap = newEnumValueMapWithDefault(StringVariable.class, Value.EMPTY_STRING);
    }

    @Override
    public boolean containsProgram(@NotNull String programName) {
        checkNotNull(programName);
        checkArgument(ValidationUtil.isValidProgramName(programName));

        return this.programMap.containsKey(programName);
    }

    @NotNull
    @Override
    public Value getLastResult() {
        return this.lastResult;
    }

    @Override
    public void setLastResult(@NotNull Value value) {
        checkNotNull(value);
        this.lastResult = value;

        LOGGER.trace("Updated ANS variable to value {} of type {}", this.lastResult.getValue(), this.lastResult.getType());
    }

    @NotNull
    @Override
    public Value getListVariableElementValue(@NotNull ListVariable listVariable, int index) {
        if (!this.listVariableValueMap.containsKey(listVariable))
            throw new UndefinedVariableException(listVariable);
        Value value = this.listVariableValueMap.get(listVariable);
        if (index <= 0 || index > value.list().size())
            throw new InvalidDimensionException("Invalid index: " + index, index);

        LOGGER.trace("Accessing element at index {} of list variable {}", index, listVariable);

        return Value.of(value.list().get(index - 1));
    }

    @NotNull
    @Override
    public Value getListVariableValue(@NotNull ListVariable listVariable) throws UndefinedVariableException {
        if (!this.listVariableValueMap.containsKey(listVariable))
            throw new UndefinedVariableException(listVariable);
        return this.listVariableValueMap.get(listVariable);
    }

    @NotNull
    @Override
    public Value getNumberVariableValue(@NotNull NumberVariable variable) {
        checkNotNull(variable);
        return this.numberVariableValueMap.get(variable);
    }

    @NotNull
    @Override
    public ExecutableProgram getStoredProgram(@NotNull String programName) throws ProgramNotFoundException {
        checkNotNull(programName);

        if (!this.programMap.containsKey(programName))
            throw new ProgramNotFoundException(programName);

        return this.programMap.get(programName);
    }

    @NotNull
    @Override
    public Value getStringVariableValue(@NotNull StringVariable variable) {
        checkNotNull(variable);
        return this.stringVariableValueMap.get(variable);
    }

    @Override
    public void setListVariableElementValue(@NotNull ListVariable listVariable, int index, @NotNull Value value) {
        if (!this.listVariableValueMap.containsKey(listVariable) && index != 1) {
            throw new UndefinedVariableException(listVariable);
        }

        Value listValue = this.listVariableValueMap.get(listVariable);
        if (listValue == null)
            listValue = Value.EMPTY_LIST;

        if (index <= 0 || index > listValue.list().size() + 1)
            throw new InvalidDimensionException("Invalid index: " + index, index);

        Complex complex = value.complex();
        List<Complex> modifiableList = Lists.newCopyOnWriteArrayList(listValue.list());

        if (index == modifiableList.size() + 1) {
            modifiableList.add(complex);
            this.listVariableValueMap.put(listVariable, Value.of(modifiableList));
            LOGGER.trace("Appended element {} to list {} at index {}", complex, listVariable, index);
        } else {
            modifiableList.set(index - 1, complex);
            this.listVariableValueMap.put(listVariable, Value.of(modifiableList));
            LOGGER.trace("Set element {} at index {} of list {}", complex, index, listVariable);
        }
    }

    @Override
    public void setListVariableSize(@NotNull ListVariable listVariable, int newSize) {
        checkNotNull(listVariable);

        Value listValue = this.listVariableValueMap.get(listVariable);
        if (listValue == null)
            listValue = Value.EMPTY_LIST;

        if (newSize < 0) {
            throw new InvalidDimensionException("Invalid new size: " + newSize, newSize);
        }
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

        this.listVariableValueMap.put(listVariable, Value.of(resizedList));

        LOGGER.trace("Resized list {} to {} elements", listVariable, newSize);
    }

    @Override
    public void setListVariableValue(@NotNull ListVariable listVariable, @NotNull Value value) {
        checkNotNull(listVariable);
        checkNotNull(value);
        checkValueType(value, ValueType.LIST);

        this.listVariableValueMap.put(listVariable, value);
        LOGGER.trace("Changed value in list variable {} to {}", listVariable, value);
    }

    @Override
    public void setNumberVariableValue(@NotNull NumberVariable variable, @NotNull Value value) {
        checkNotNull(variable);
        checkValueType(value, ValueType.NUMBER);

        this.numberVariableValueMap.put(variable, value);
        LOGGER.trace("Changed value in numerical variable {} to {}", variable, value);
    }

    @Override
    public void setStringVariableValue(@NotNull StringVariable variable, @NotNull Value value) {
        checkNotNull(variable);
        checkNotNull(value);
        checkValueType(value, ValueType.STRING);

        this.stringVariableValueMap.put(variable, value);
        LOGGER.trace("Changed value in string variable {} to {}", variable, value);
    }

    @Override
    public void storeProgram(@NotNull String programName, @NotNull ExecutableProgram programCode) throws DuplicateProgramException {
        checkNotNull(programName);
        checkNotNull(programCode);
        checkArgument(ValidationUtil.isValidProgramName(programName));

        if (this.programMap.containsKey(programName))
            throw new DuplicateProgramException(programName);
        else
            this.programMap.put(programName, programCode);

        LOGGER.trace("Stored new program {}", programName);
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
