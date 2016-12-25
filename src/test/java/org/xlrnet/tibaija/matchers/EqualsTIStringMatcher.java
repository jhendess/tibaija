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

package org.xlrnet.tibaija.matchers;

import org.apache.commons.lang3.StringUtils;
import org.mockito.ArgumentMatcher;
import org.xlrnet.tibaija.commons.Value;

/**
 * Matcher for verifying string values in TI-Basic.
 */
public class EqualsTIStringMatcher extends ArgumentMatcher<Value> {

    private final String expectedString;

    public EqualsTIStringMatcher(String expectedString) {
        this.expectedString = expectedString;
    }

    /**
     * Returns whether this matcher accepts the given argument.
     * <p/>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     *
     * @param argument
     *         the argument
     * @return whether this matcher accepts the given argument.
     */
    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof Value)) {
            return false;
        }

        Value value = (Value) argument;

        if (!value.isString()) {
            return false;
        }

        String actualString = value.string();

        return StringUtils.equals(this.expectedString, actualString);
    }
}
