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
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Variable that contains a temporary result of an expression. Make sure to run a type-check using the built-in
 * methods of this class before querying its value.
 */
public class Value extends AnswerVariable {

    public static final Value NEGATIVE_ONE = Value.of(Complex.valueOf(-1));

    private Value(Complex c) {
        super(c);
    }

    @NotNull
    public static Value of(@NotNull Complex c) {
        return new Value(c);
    }

    /**
     * Create a new Value object from a BigDecimal. This is recommended, since future versions might work with
     * BigDecimal implementations.
     *
     * @param bigDecimal
     *         A big decimal.
     * @return A new Value object with a real number.
     */
    @NotNull
    public static Value of(@NotNull BigDecimal bigDecimal) {
        return Value.of(bigDecimal.doubleValue());           // We don't support BigDecimal yet :( -> casting to double
    }

    /**
     * Create a new Value object from a boolean. True is represented as 1, false will become 0.
     *
     * @param bool
     *         True or false.
     * @return A new Value object with the given boolean parameter represented as 1 or 0.
     */
    @NotNull
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
    @NotNull
    public static Value of(Number real) {
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
    @NotNull
    public static Value of(@NotNull Number real, @NotNull Number imaginary) {
        return of(Complex.valueOf(real.doubleValue(), imaginary.doubleValue()));
    }

    /**
     * Set the value of this object with the given number. Note: Numbers are always represented as complex values!
     *
     * @param number
     *         A numerical value.
     */
    public void setValue(@NotNull Complex number) {
        value = number;
        type = Variables.VariableType.NUMBER;
    }

    @Override
    public String toString() {
        return "Value{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }
}
