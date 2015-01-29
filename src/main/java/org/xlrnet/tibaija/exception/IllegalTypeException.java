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

import org.xlrnet.tibaija.memory.Variables;

/**
 * An exception that indicates an illegal type. This can be thrown on mismatching type arguments and when trying to
 * access an {@link org.xlrnet.tibaija.memory.Value} object directly with the wrong type.
 */
public class IllegalTypeException extends TIRuntimeException {

    Variables.VariableType expectedType;

    Variables.VariableType actualType;

    /**
     * Throws a new illegal type exception.
     *
     * @param expectedType
     *         The expected type.
     * @param actualType
     *         The actual type.
     */
    public IllegalTypeException(Variables.VariableType expectedType, Variables.VariableType actualType) {
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
    public IllegalTypeException(String message, Variables.VariableType expectedType, Variables.VariableType actualType) {
        this(-1, -1, message, expectedType, actualType);
    }

    public IllegalTypeException(int linenumber, int startIndex, String message, Variables.VariableType expectedType, Variables.VariableType actualType) {
        super(linenumber, startIndex, message);
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Variables.VariableType getActualType() {
        return actualType;
    }

    public Variables.VariableType getExpectedType() {
        return expectedType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " expected: " + expectedType + "; actual: " + actualType;
    }
}
