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

package org.xlrnet.tibaija.exception;

import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.commons.ValueType;

/**
 * An exception that indicates an illegal type. This can be thrown on mismatching type arguments and when trying to
 * access an {@link Value} object directly with the wrong type.
 */
public class IllegalTypeException extends TIRuntimeException {

    ValueType expectedType;

    ValueType actualType;

    /**
     * Throws a new illegal type exception.
     *
     * @param expectedType
     *         The expected type.
     * @param actualType
     *         The actual type.
     */
    public IllegalTypeException(ValueType expectedType, ValueType actualType) {
        this(-1, -1, "Type mismatch - ", expectedType, actualType);
    }

    /**
     * Throws a new illegal type exception.
     *
     * @param message
     *         Your message.
     * @param expectedType
     *         The expected type.
     * @param actualType
     *         The actual type.
     */
    public IllegalTypeException(String message, ValueType expectedType, ValueType actualType) {
        this(-1, -1, message, expectedType, actualType);
    }

    public IllegalTypeException(int linenumber, int startIndex, String message, ValueType expectedType, ValueType actualType) {
        super(linenumber, startIndex, message);
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public ValueType getActualType() {
        return this.actualType;
    }

    public ValueType getExpectedType() {
        return this.expectedType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " - expected: " + this.expectedType + "; actual: " + this.actualType;
    }
}
