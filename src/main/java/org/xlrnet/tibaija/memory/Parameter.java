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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.exception.UndefinedVariableException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A parameter from a parameter list. Depending on the internal value, a parameter can either be treated as a value (an
 * evaluated expression) or a reference to a variable. If the content is a variable, it may either be evaluated or
 * referenced.
 */
public class Parameter {

    private final Value value;

    private final Variable variable;

    private final ReadOnlyCalculatorMemory memory;

    private Parameter(Value value) {
        this.value = value;
        this.variable = null;
        this.memory = null;
    }

    private Parameter(Variable variable, ReadOnlyCalculatorMemory memory) {
        this.value = null;
        this.variable = variable;
        this.memory = memory;
    }

    /**
     * Create a new parameter object from an evaluated {@link Value}. The created parameter object cannot be used as an
     * variable.
     *
     * @param value
     *         An evaluated {@link Value} object.
     * @return A new parameter object with an evaluated value.
     */
    @NotNull
    public static Parameter value(Value value) {
        checkNotNull(value, "Value may not be null");

        return new Parameter(value);
    }

    /**
     * Create a new parameter object from an unevaluated {@link Variable}. The created parameter object can either be
     * used as an variable parameter or evaluated using {@link #value()} through the supplied {@link
     * ReadOnlyCalculatorMemory}.
     * variable.
     *
     * @param variable
     *         An unevaluated variable.
     * @param memory
     *         The memory which will be used for evaluating the variable's value.
     * @return A new parameter object with an evaluated value.
     */
    @NotNull
    public static Parameter variable(@NotNull Variable variable, @NotNull ReadOnlyCalculatorMemory memory) {
        checkNotNull(variable, "Variable may not be null");
        checkNotNull(memory, "Memory may not be null");

        return new Parameter(variable, memory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equal(value, parameter.value) &&
                Objects.equal(variable, parameter.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, variable);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("variable", variable)
                .add("memory", memory.toString())
                .toString();
    }

    @NotNull
    public Value value() {
        if (value != null) {
            return value;
        } else {
            return getVariableValue(variable);
        }
    }

    @NotNull
    public Variable variable() {
        if (variable == null) {
            throw new UndefinedVariableException("null");
        }

        return variable;
    }

    @NotNull
    private Value getVariableValue(@NotNull Variable variable) {
        switch (variable.getVariableType()) {
            case NUMBER:
                return memory.getNumberVariableValue((Variables.NumberVariable) variable);
            case STRING:
                return memory.getStringVariableValue((Variables.StringVariable) variable);
            case LIST:
                return memory.getListVariableValue((ListVariable) variable);
            default:
                throw new UnsupportedOperationException("Unsupported variable type: " + variable.getVariableType());
        }
    }
}
