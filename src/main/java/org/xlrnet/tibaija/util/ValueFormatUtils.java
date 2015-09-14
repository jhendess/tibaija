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

package org.xlrnet.tibaija.util;

import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.graphics.DecimalDisplayMode;
import org.xlrnet.tibaija.memory.Value;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper functions for formatting {@link Value} objects.
 */
public class ValueFormatUtils {

    private static final char IMAGINARY_SYMBOL = 'i';

    private static final String LIST_BEGIN = "{";

    private static final String LIST_SEPARATOR = ",";

    private static final String LIST_END = "}";

    /**
     * Format a {@link Value} object according to the selected {@link DecimalDisplayMode}.
     *
     * @param value
     *         The value to format
     * @param decimalDisplayMode
     *         The display mode used for formatting decimal numbers.
     * @return A formatted string representation of the value.
     */
    @NotNull
    public static String formatValue(@NotNull Value value, @NotNull DecimalDisplayMode decimalDisplayMode) {
        checkNotNull(value, "Value may not be null");
        checkNotNull(decimalDisplayMode, "Display mode may not be null");

        switch (value.getType()) {
            case NUMBER:
                return formatComplex(value.complex(), decimalDisplayMode);
            case STRING:
                return value.string();
            case LIST:
                return formatListValue(value, decimalDisplayMode);
            default:
                throw new UnsupportedOperationException("Unsupported value type: " + value.getType());
        }
    }

    @NotNull
    private static String formatListValue(@NotNull Value value, @NotNull DecimalDisplayMode decimalDisplayMode) {
        StringBuilder stringBuilder = new StringBuilder(LIST_BEGIN);

        Iterator<Complex> iterator = value.list().iterator();
        while (iterator.hasNext()) {
            Complex complex = iterator.next();
            stringBuilder.append(formatComplex(complex, decimalDisplayMode));

            if (iterator.hasNext()) {
                stringBuilder.append(LIST_SEPARATOR);
            }
        }

        stringBuilder.append(LIST_END);

        return stringBuilder.toString();
    }

    @NotNull
    private static String formatComplex(@NotNull Complex complex, @NotNull DecimalDisplayMode decimalDisplayMode) {
        StringBuilder stringBuilder = new StringBuilder();

        if (complex.getReal() != 0 || complex.getImaginary() == 0) {
            stringBuilder.append(formatNumber(complex.getReal(), decimalDisplayMode));
            if (complex.getImaginary() != 0) {
                stringBuilder.append("+");
            }
        }

        if (complex.getImaginary() != 0) {
            stringBuilder.append(formatNumber(complex.getImaginary(), decimalDisplayMode));
            stringBuilder.append(IMAGINARY_SYMBOL);
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String formatNumber(double number, @NotNull DecimalDisplayMode decimalDisplayMode) {
        if (decimalDisplayMode.getDecimalsToDisplay() < 0) {
            switch (decimalDisplayMode) {
                case FLOAT:
                    if (number % 1 == 0) {
                        return String.valueOf((long) number);
                    } else {
                        return String.valueOf(number);
                    }
                default:
                    throw new UnsupportedOperationException("Unsupported display mode: " + decimalDisplayMode);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported display mode: " + decimalDisplayMode);
        }
    }
}
