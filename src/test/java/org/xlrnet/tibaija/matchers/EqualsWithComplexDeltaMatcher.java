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

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Precision;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.test.TestUtils;

import java.io.Serializable;


/**
 * Custom matcher for mockito framework. This allows us to match wrapped Complex values inside a {@link
 * Value} object with a certain precision delta.
 */
public class EqualsWithComplexDeltaMatcher extends ArgumentMatcher<Value> implements Serializable {

    private static final long serialVersionUID = 5066980489920383664L;

    private final Value wanted;

    private final Number delta;

    public EqualsWithComplexDeltaMatcher(double real, Number delta) {
        this(real, 0, delta);
    }

    public EqualsWithComplexDeltaMatcher(double real, double imaginary, Number delta) {
        this.wanted = Value.of(real, imaginary);
        this.delta = delta;
    }

    public void describeTo(Description description) {
        description.appendText("Value: " + this.wanted + ", delta: " + this.delta + ")");
    }

    public boolean matches(Object actual) {
        if (!((Value) actual).isNumber()) {
            return false;
        }

        Complex actualComplex = ((Value) actual).complex();
        return Precision.equals(this.wanted.complex().getReal(), actualComplex.getReal(), TestUtils.DEFAULT_TOLERANCE)
                && Precision.equals(this.wanted.complex().getImaginary(), actualComplex.getImaginary(), TestUtils.DEFAULT_TOLERANCE);
    }
}
