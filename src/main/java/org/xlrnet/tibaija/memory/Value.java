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

import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.exception.TIRuntimeException;
import org.xlrnet.tibaija.util.ComplexComparator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Holds any value Make sure to run a type-check using the built-in
 * methods of this class before querying its value.
 */
public class Value implements Comparable<Value> {

    public static final Value ZERO = Value.of(Complex.ZERO);

    public static final Value ONE = Value.of(Complex.ONE);

    public static final Value NEGATIVE_ONE = Value.of(Complex.valueOf(-1));

    public static final Value EMPTY_LIST = Value.of(new ArrayList<>());

    public static final Value EMPTY_STRING = Value.of("");

    private static final Comparator<Complex> complexComparator = new ComplexComparator();

    private final Object value;

    private final Variables.VariableType type;

    /**
     * Create a new Value object from a complex number and set the according type. Note: Numbers are always represented
     * as complex values!
     *
     * @param number
     *         The complex number.
     */
    private Value(@NotNull Complex number) {
        value = number;
        type = Variables.VariableType.NUMBER;
    }

    /**
     * Create a new Value object from a string and set the according type.
     *
     * @param string
     *         The string value.
     */
    private Value(@NotNull String string) {
        value = string;
        type = Variables.VariableType.STRING;
    }

    /**
     * Create a new Value object from an immutable list of complex numbers and set the according type. Note: Numbers
     * are  always represented as complex values!
     *
     * @param complexImmutableList
     *         The immutable list of complex numbers.
     */
    private Value(@NotNull ImmutableList<Complex> complexImmutableList) {
        value = complexImmutableList;
        type = Variables.VariableType.LIST;
    }

    /**
     * Create a new Value object from an immutable list of complex numbers.
     *
     * @param complexImmutableList
     *         The immutable list of complex numbers.
     */
    @NotNull
    public static Value of(@NotNull ImmutableList<Complex> complexImmutableList) {
        return new Value(complexImmutableList);
    }

    /**
     * Create a new string Value object from a {@link String}.
     *
     * @param string
     *         The string to store inside the new value. May not be null.
     */
    @NotNull
    public static Value of(@NotNull String string) {
        return new Value(string);
    }

    /**
     * Create a new Value object from a mutable list of complex numbers. The given objects will be transferred to a
     * separate immutable list.
     *
     * @param complexMutableList
     *         The immutable list of complex numbers.
     */
    @NotNull
    public static Value of(@NotNull List<Complex> complexMutableList) {
        return of(ImmutableList.copyOf(complexMutableList));
    }

    /**
     * Create a new Value object from a mutable array of complex numbers. The given objects will be transferred to a
     * separate immutable list.
     *
     * @param complexArray
     *         The immutable list of complex numbers.
     */
    @NotNull
    public static Value of(@NotNull Complex... complexArray) {
        return of(ImmutableList.copyOf(complexArray));
    }

    /**
     * Create a new Value object from a complex number. Note: Numbers are always represented as complex values!
     *
     * @param c
     *         The complex number.
     * @return A new Value object with the given complex number.
     */
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
        return of(bigDecimal.doubleValue());           // We don't support BigDecimal yet :( -> casting to double
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
     * Retrieves the internal value as a boolean value. If the internal is not a number, this method will throw an
     * {IllegalTypeException}. Use this method only if you know the underlying object type!
     *
     * @return The internal value as a boolean. If the numerical value is not zero, true will be returned. Otherwise
     * false.
     * @throws TIRuntimeException
     */
    public boolean bool() throws IllegalTypeException {
        Complex c = complex();
        return c.getReal() != 0 || c.getImaginary() != 0;
    }

    @Override
    public int compareTo(@NotNull Value o) {
        checkNotNull(o);

        if (Objects.equals(this, o))
            return 0;

        try {
            if (!(this.isNumber() || o.isNumber()))
                throw new IllegalTypeException("Comparison not supported for right type", Variables.VariableType.NUMBER, type);
            return complexComparator.compare(this.complex(), o.complex());
        } catch (UnsupportedOperationException u) {
            throw new TIArgumentException("Illegal operation: " + u.getMessage(), this, o);
        }
    }

    /**
     * Retrieves the internal value as a Complex object. If the internal is not a Complex, this method will throw an
     * {IllegalTypeException}. Use this method only if you know the underlying object type!
     *
     * @return The internal value as a Complex object.
     * @throws TIRuntimeException
     */
    @NotNull
    public Complex complex() throws IllegalTypeException {
        internalTypeCheck(Variables.VariableType.NUMBER);
        return (Complex) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value that = (Value) o;

        if (type != that.type) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @NotNull

    public Variables.VariableType getType() {
        return type;
    }

    /**
     * Returns the internal value without casting as a pure object.
     */
    @NotNull
    public Object getValue() {
        return value;
    }

    /**
     * Checks if the value is complex and has an imaginary value.
     *
     * @return True if the value is complex and has an imaginary value. False otherwise.
     */
    public boolean hasImaginaryValue() {
        return isType(Variables.VariableType.NUMBER) && complex().getImaginary() != 0;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * Check if this object contains a list.
     *
     * @return True if this object contains a list; false otherwise.
     */
    public boolean isList() {
        return isType(Variables.VariableType.LIST);
    }

    /**
     * Check if this object contains a complex or numerical value.
     *
     * @return True if this object contains a complex or numerical value; false otherwise.
     */
    public boolean isNumber() {
        return isType(Variables.VariableType.NUMBER);
    }

    /**
     * Check if this object contains a string value.
     *
     * @return True if this object contains a string value; false otherwise.
     */
    public boolean isString() {
        return isType(Variables.VariableType.STRING);
    }

    /**
     * Retrieves the internal value as a list of Complex objects. If the internal is not a Complex, this method will
     * throw an
     * {IllegalTypeException}. Use this method only if you know the underlying object type!
     *
     * @return The internal value as a Complex object.
     * @throws TIRuntimeException
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public ImmutableList<Complex> list() throws IllegalTypeException {
        internalTypeCheck(Variables.VariableType.LIST);
        return (ImmutableList<Complex>) value;
    }

    /**
     * Retrieves the internal value as a boolean value. If the internal is not a number, this method will throw an
     * {IllegalTypeException}. Use this method only if you know the underlying object type!
     *
     * @return The internal value as a boolean. If the numerical value is not zero, true will be returned. Otherwise
     * false.
     * @throws TIRuntimeException
     */
    public String string() throws IllegalTypeException {
        internalTypeCheck(Variables.VariableType.STRING);
        return (String) value;
    }

    @Override
    public String toString() {
        return "Value{" +
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
     * @return True if the internal type is equal to the expected type. False otherwise.
     */
    private boolean isType(Variables.VariableType checkedType) {
        return Objects.equals(this.getType(), checkedType);
    }
}
