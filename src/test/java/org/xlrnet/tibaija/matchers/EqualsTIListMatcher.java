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

import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Precision;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.test.TestUtils;

import java.util.Arrays;

/**
 * Matcher for verifying correct behaviour of lists.
 */
public class EqualsTIListMatcher extends ArgumentMatcher<Value> {

    private final Complex[] wantedValues;

    private final Number delta;

    public EqualsTIListMatcher(Complex[] values, double defaultTolerance) {
        wantedValues = values;
        delta = defaultTolerance;
    }

    public void describeTo(Description description) {
        description.appendText("Value: " + Arrays.toString(wantedValues) + ", delta: " + delta + ")");
    }

    public boolean matches(Object actualRaw) {
        ImmutableList<Complex> actualValues = ((Value) actualRaw).list();

        if (actualValues.size() != wantedValues.length)
            return false;

        for (int i = 0; i < wantedValues.length; i++) {
            Complex actual = actualValues.get(i);
            Complex wanted = wantedValues[i];
            if (!internalMatchesSingleComplex(actual, wanted))
                return false;
        }

        return true;
    }

    private boolean internalMatchesSingleComplex(Complex actual, Complex wanted) {
        return Precision.equals(actual.getReal(), wanted.getReal(), TestUtils.DEFAULT_TOLERANCE)
                && Precision.equals(actual.getImaginary(), wanted.getImaginary(), TestUtils.DEFAULT_TOLERANCE);
    }
}
