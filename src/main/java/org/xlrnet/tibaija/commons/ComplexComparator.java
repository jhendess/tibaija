/*
 * Copyright (c) 2016 Jakob Hendeß
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

package org.xlrnet.tibaija.commons;

import org.apache.commons.math3.complex.Complex;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Compares two Complex values. Throws an UnsupportedOperationException if you try to compare complex values with
 * imaginary parts.
 */
public class ComplexComparator implements Comparator<Complex>, Serializable {

    private static final long serialVersionUID = -2757714182421392977L;

    @Override
    public int compare(Complex o1, Complex o2) {
        boolean isNoneImaginary = o1.getImaginary() == 0 && o2.getImaginary() == 0;
        boolean isNoneReal = o1.getReal() == 0 && o2.getReal() == 0;

        if (isNoneImaginary && isNoneReal)      // Comparison of zero and zero -> equal
            return 0;

        if (!isNoneImaginary && !isNoneReal)      // Comparison of complex and imaginary 
            throw new UnsupportedOperationException("Comparison of complex numbers with imaginary part is not allowed");
        if (isNoneImaginary)
            return Objects.compare(o1.getReal(), o2.getReal(), Comparator.naturalOrder());
        else
            return Objects.compare(o1.getImaginary(), o2.getImaginary(), Comparator.naturalOrder());
    }
}

