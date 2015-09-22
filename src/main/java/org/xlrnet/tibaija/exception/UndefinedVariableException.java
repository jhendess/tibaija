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

import org.xlrnet.tibaija.memory.Variable;

/**
 * An exception that indicates an access to an undefined variable or a variable resolution error.
 */
public class UndefinedVariableException extends TIRuntimeException {

    private static final long serialVersionUID = 1697277001636611416L;

    private String variableName;

    /**
     * Throws a new UnknownVariableException with the given name of the unknown variable.
     *
     * @param variable
     *         The unknown or undefined variable.
     */
    public UndefinedVariableException(Variable variable) {
        this(-1, -1, "Undefined variable", variable.getVariableName());
    }

    /**
     * Throws a new UnknownVariableException with the given name of the unknown variable as a String.
     *
     * @param variableName
     *         Name of the Variable.
     */
    public UndefinedVariableException(String variableName) {
        this(-1, -1, "Undefined variable", variableName);
    }

    public UndefinedVariableException(int linenumber, int startIndex, String message, String variableName) {
        super(linenumber, startIndex, message);
        this.variableName = variableName;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " - " + variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
