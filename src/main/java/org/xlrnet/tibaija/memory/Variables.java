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

import org.apache.commons.lang3.EnumUtils;
import org.xlrnet.tibaija.exception.UnknownVariableException;

/**
 * Class with enums for accessing various variables.
 */
public class Variables {

    public static NumberVariable resolveNumberVariable(String variableName) {
        NumberVariable result = internalResolveVariableName(NumberVariable.class, variableName);
        return result;
    }

    private static <E extends Enum<E>> E internalResolveVariableName(final Class<E> clazz, String variableName) {
        E result = EnumUtils.getEnum(clazz, variableName);
        if (result == null)
            throw new UnknownVariableException(-1, -1, "Unknown variable name", variableName);
        return result;
    }

    public enum NumberVariable {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
    }

    public enum VariableType {
        NUMBER,
        STRING,
        LIST,
        MATRIX
    }

}
