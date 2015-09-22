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

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A list variable can be used for accessing variable lists. Each list is guaranteed to contain at least up to 999
 * number elements.
 */
public class ListVariable implements Variable {

    private final static Map<String, ListVariable> listVariableMap = new HashMap<>();

    public static final ListVariable DEFAULT_1 = fromName("₁");

    public static final ListVariable DEFAULT_2 = fromName("₂");

    public static final ListVariable DEFAULT_3 = fromName("₃");

    public static final ListVariable DEFAULT_4 = fromName("₄");

    public static final ListVariable DEFAULT_5 = fromName("₅");

    public static final ListVariable DEFAULT_6 = fromName("₆");

    private final String variableName;

    private ListVariable(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Create a new {@link ListVariable} object from a given variable name. If an object with the same name (i.e.
     * variable name) already exists, that one will be returned.
     *
     * @param variableName
     *         Name of the variable.  Must be written uppercase and between one and five
     *         characters without the leading list token "∟". Digits are allowed except for the first character.
     * @return A ListVariable object with the given variable name.
     */
    public static ListVariable fromName(@NotNull String variableName) {
        checkArgument(StringUtils.isNotBlank(variableName), "List name may not be blank");

        if (!listVariableMap.containsKey(variableName)) {
            listVariableMap.put(variableName, new ListVariable(variableName));
        }

        return listVariableMap.get(variableName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListVariable)) return false;
        ListVariable that = (ListVariable) o;
        return Objects.equal(variableName, that.variableName);
    }

    /**
     * Returns the name of the variable,
     *
     * @return the name of the variable,
     */
    @Override
    public String getVariableName() {
        return variableName;
    }

    /**
     * Returns the type of the variable (e.g. number, string, ...).
     *
     * @return the type of the variable .
     */
    @Override
    public Variables.VariableType getVariableType() {
        return Variables.VariableType.LIST;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(variableName);
    }
}
