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

import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.complex.Complex;
import org.junit.Test;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.graphics.DecimalDisplayMode;
import org.xlrnet.tibaija.graphics.NumberDisplayFormat;
import org.xlrnet.tibaija.memory.ValueFormatUtils;

import static org.junit.Assert.assertEquals;

/**
 * Tests for value formatting.
 */
public class ValueFormatUtilsTest {

    // TODO: Write more tests

    @Test
    public void testFormatValue_normal_fix1_complex() throws Exception {
        Value testValue = Value.of(1.234, 5.678);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FIX_1);
        assertEquals("1.2+5.7ℑ", actual);
    }

    @Test
    public void testFormatValue_normal_fix2_zero() throws Exception {
        Value testValue = Value.of(0);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FIX_2);
        assertEquals("0.00", actual);
    }

    @Test
    public void testFormatValue_normal_float_complex_1() throws Exception {
        Value testValue = Value.of(0, 1);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("1ℑ", actual);     // TODO: Use different "i" for imaginary printing
    }

    @Test
    public void testFormatValue_normal_float_complex_2() throws Exception {
        Value testValue = Value.of(15, 1);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("15+1ℑ", actual);     // TODO: Use different "i" for imaginary printing
    }

    @Test
    public void testFormatValue_normal_float_list_1() throws Exception {
        Value testValue = Value.of(ImmutableList.of(Complex.valueOf(123)));
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("{123}", actual);
    }

    @Test
    public void testFormatValue_normal_float_list_2() throws Exception {
        Value testValue = Value.of(Complex.valueOf(123), Complex.valueOf(456), Complex.valueOf(789));
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("{123,456,789}", actual);
    }

    @Test
    public void testFormatValue_normal_float_number_1() throws Exception {
        Value testValue = Value.of(123456);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("123456", actual);
    }

    @Test
    public void testFormatValue_normal_float_number_2() throws Exception {
        Value testValue = Value.of(1234.56789);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("1234.56789", actual);
    }

    @Test
    public void testFormatValue_normal_float_string() throws Exception {
        Value testValue = Value.of("HelloWorld");
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("HelloWorld", actual);
    }

    @Test
    public void testFormatValue_normal_float_zero() throws Exception {
        Value testValue = Value.of(0);
        String actual = ValueFormatUtils.formatValue(testValue, NumberDisplayFormat.NORMAL, DecimalDisplayMode.FLOAT);
        assertEquals("0", actual);
    }
}