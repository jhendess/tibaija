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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.DuplicateProgramException;
import org.xlrnet.tibaija.processor.ExecutableProgram;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.xlrnet.tibaija.util.ValueUtils.checkValueType;

/**
 * Default implementation of the TI-Basic memory model.
 */
public class DefaultCalculatorMemory implements CalculatorMemory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalculatorMemory.class);

    private final Map<Variables.NumberVariable, Value> numberVariableValueMap = new HashMap<>();

    private AnswerVariable lastResult = Value.of(0);

    private Map<String, ExecutableProgram> programMap = new HashMap<>();

    @NotNull
    @Override
    public AnswerVariable getLastResult() {
        return lastResult;
    }

    @Override
    public void setLastResult(@NotNull Value value) {
        checkNotNull(value);
        this.lastResult = value;
    }

    @NotNull
    @Override
    public Value getNumberVariableValue(@NotNull Variables.NumberVariable variable) {
        checkNotNull(variable);
        return numberVariableValueMap.get(variable);
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

        if (programMap.containsKey(programName))
            throw new DuplicateProgramException(programName);
        else
            programMap.put(programName, programCode);

        LOGGER.debug("Stored new program {}", programName);
    }
}
