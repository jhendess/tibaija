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

package org.xlrnet.tibaija.util;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Several helper methods for Value handling. Note: all methods beginning with "check" throw an exception if a required
 * condition is not met!
 */
public class ValueUtils {

    /**
     * Check the given Value object if it contains the expected type. If the actual and expected don't match, an {@link
     * org.xlrnet.tibaija.exception.IllegalTypeException} will be thrown.
     *
     * @param value
     *         The value to check.
     * @param expectedType
     *         The expected type of the value.
     * @throws org.xlrnet.tibaija.exception.IllegalTypeException
     *         Exception will be thrown if the actual and the expected type don't match.
     * @throws NullPointerException
     *         Will be thrown if either the value or the expected type are null.
     */
    public static void checkValueType(@NotNull Value value, @NotNull Variables.VariableType expectedType) throws IllegalTypeException, NullPointerException {
        checkNotNull(value);
        checkNotNull(expectedType);

        if (!Objects.equals(value.getType(), expectedType))
            throw new IllegalTypeException(expectedType, value.getType());
    }

    /**
     * Checks if any of the given Values has an complex value with imaginary part. If this is the case, a new {@link
     * org.xlrnet.tibaija.exception.TIArgumentException} will be thrown.
     *
     * @param values
     *         The values to check.
     * @throws org.xlrnet.tibaija.exception.TIArgumentException
     *         Will be thrown if any of the arguments has an imaginary part.
     */
    public static void checkIfAnyValueIsImaginary(@NotNull Value... values) throws TIArgumentException {
        for (Value v : values)
            if (v.hasImaginaryValue())
                throw new TIArgumentException("Cannot operate on imaginary values", ImmutableList.copyOf(values));
    }
}
