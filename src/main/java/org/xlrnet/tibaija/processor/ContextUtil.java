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

package org.xlrnet.tibaija.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.commons.Value;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

/**
 * Helper class for extracting values from different grammar contexts.
 */
public class ContextUtil {

    private ContextUtil() {
        // No instances allowed
    }

    /**
     * Extracts the numerical value from a given NumberContext objects. This will always create a complex value.
     *
     * @param ctx
     *         The NumberContext from the parser.
     * @return The numerical value as a complex object.
     */
    public static Value extractValueFromNumberContext(TIBasicParser.NumberContext ctx) {
        boolean isNegative = !isNull(ctx.NEGATIVE_MINUS());
        boolean isDecimal = !isNull(ctx.DOT());
        String preDecimal = (StringUtils.isNotEmpty(ctx.preDecimal)) ? ctx.preDecimal : "0";
        String decimal = (StringUtils.isNotEmpty(ctx.decimal)) ? ctx.decimal : "0";

        BigDecimal value;
        if (isDecimal) {
            value = NumberUtils.createBigDecimal(preDecimal + "." + decimal);
        } else {
            if ("0".equals(preDecimal))
                value = NumberUtils.createBigDecimal(decimal);
            else
                value = NumberUtils.createBigDecimal(preDecimal + decimal);
        }

        if (isNegative)
            value = value.multiply(BigDecimal.valueOf(-1));

        return Value.of(value);
    }
}
