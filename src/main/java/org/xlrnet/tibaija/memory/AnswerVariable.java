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

import org.apache.commons.math3.complex.Complex;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.TIRuntimeException;

import java.util.Objects;

/**
 * Variable that contains the result of the last top-level expression. Make sure to run a type-check using the built-in
 * methods of this class before querying its value.
 */
public class AnswerVariable {

    Object value = Complex.ZERO;

    Variables.VariableType type = Variables.VariableType.NUMBER;

    public AnswerVariable(Complex o) {
        setValue(o);
    }

    /**
     * Retrieves the internal value as a Complex object. If the internal is not a Complex, this method will throw an
     * {IllegalTypeException}. Use this method only if you know the underlying object type!
     *
     * @return The internal value as a Complex object.
     * @throws TIRuntimeException
     */
    public Complex complex() throws IllegalTypeException {

        internalTypeCheck(Variables.VariableType.NUMBER);
        return (Complex) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerVariable)) return false;

        AnswerVariable that = (AnswerVariable) o;

        if (type != that.type) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    public Variables.VariableType getType() {
        return type;
    }

    /**
     * Returns the internal value without casting as a pure object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value of this object with the given number. Note: Numbers are always represented as complex values!
     *
     * @param number
     *         A numerical value.
     */
    protected void setValue(Complex number) {
        value = number;
        type = Variables.VariableType.NUMBER;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * Check if this object contains a complex or numerical value.
     *
     * @return True if this object contains a complex or numerical value; false if not.
     */
    public boolean isNumber() {
        return isType(Variables.VariableType.NUMBER);
    }

    @Override
    public String toString() {
        return "AnswerVariable{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }

    /**
     * Internal type check. Will throw an exception if the internal type is not equal to the given parameter.
     *
     * @param checkType
     *         The type to match.
     */
    private void internalTypeCheck(Variables.VariableType checkType) throws IllegalTypeException {
        if (!isType(checkType))
            throw new IllegalTypeException(-1, -1, "Illegal type cast", checkType, type);
    }

    /**
     * Internal method for type checking.
     *
     * @param checkedType
     *         The expected type.
     * @return True if the internal type is equal to the expected type, false if not.
     */
    private boolean isType(Variables.VariableType checkedType) {
        return Objects.equals(this.getType(), checkedType);
    }
}
