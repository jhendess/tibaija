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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Variable that contains a temporary result of an expression. Make sure to run a type-check using the built-in
 * methods of this class before querying its value.
 */
public class Value extends AnswerVariable {

    public static final Value NEGATIVE_ONE = Value.of(Complex.valueOf(-1));

    private Value(Complex c) {
        super(c);
    }

    public static Value of(Complex c) {
        return new Value(c);
    }

    /**
     * Create a new Value object from a boolean. True is represented as 1, false will become 0.
     *
     * @param bool
     *         True or false.
     * @return A new Value object with the given boolean parameter represented as 1 or 0.
     */
    public static Value of(boolean bool) {
        return of(bool ? 1 : 0);
    }

    /**
     * Create a new Value object from a real number.
     *
     * @param real
     *         A real number.
     * @return A new Value object with a real number.
     */
    public static Value of(double real) {
        return of(real, 0);
    }

    /**
     * Create a new Value object from a real number and an imaginary part.
     *
     * @param real
     *         A real number.
     * @param imaginary
     *         The imaginary part.
     * @return A new Value object with a full complex number.
     */
    public static Value of(double real, double imaginary) {
        return of(Complex.valueOf(real, imaginary));
    }

    /**
     * Check the given Value object if it contains the expected type. If the actual and expected don't match, an {@link
     * IllegalTypeException} will be thrown.
     *
     * @param value
     *         The value to check.
     * @param expectedType
     *         The expected type of the value.
     * @throws org.xlrnet.tibaija.exception.IllegalTypeException
     *         Exception will be thrown if the actual and the expected type don't match.
     * @throws java.lang.NullPointerException
     *         Will be thrown if either the value or the expected type are null.
     */
    public static void checkValueType(Value value, Variables.VariableType expectedType) throws IllegalTypeException, NullPointerException {
        checkNotNull(value);
        checkNotNull(expectedType);

        if (!Objects.equals(value.getType(), expectedType))
            throw new IllegalTypeException(expectedType, value.getType());
    }

    /**
     * Set the value of this object with the given number. Note: Numbers are always represented as complex values!
     *
     * @param number
     *         A numerical value.
     */
    public void setValue(Complex number) {
        value = number;
        type = Variables.VariableType.NUMBER;
    }
}
