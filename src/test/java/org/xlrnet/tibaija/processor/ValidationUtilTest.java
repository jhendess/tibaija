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

package org.xlrnet.tibaija.processor;

import org.junit.Test;
import org.xlrnet.tibaija.commons.ValidationUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilTest {

    @Test
    public void testIsValidLabelIdentifier_false() throws Exception {
        assertFalse(ValidationUtil.isValidLabelIdentifier("123"));
        assertFalse(ValidationUtil.isValidLabelIdentifier("AAA"));
        assertFalse(ValidationUtil.isValidLabelIdentifier("-"));
        assertFalse(ValidationUtil.isValidLabelIdentifier("*"));
        assertFalse(ValidationUtil.isValidLabelIdentifier("Ö"));
        assertFalse(ValidationUtil.isValidLabelIdentifier("ab"));
    }

    @Test
    public void testIsValidLabelIdentifier_true() throws Exception {
        assertTrue(ValidationUtil.isValidLabelIdentifier("A"));
        assertTrue(ValidationUtil.isValidLabelIdentifier("AA"));
        assertTrue(ValidationUtil.isValidLabelIdentifier("1"));
        assertTrue(ValidationUtil.isValidLabelIdentifier("A1"));
        assertTrue(ValidationUtil.isValidLabelIdentifier("θ"));
        assertTrue(ValidationUtil.isValidLabelIdentifier("θθ"));
    }

    @Test
    public void testIsValidListVariable_false() throws Exception {
        assertFalse(ValidationUtil.isValidListName("∟1"));
        assertFalse(ValidationUtil.isValidListName("∟ABCDEFGHI"));
        assertFalse(ValidationUtil.isValidListName("∟1AEFG"));
        assertFalse(ValidationUtil.isValidListName("∟a1234567"));
        assertFalse(ValidationUtil.isValidListName("∟abc"));
        assertFalse(ValidationUtil.isValidListName("A"));
        assertFalse(ValidationUtil.isValidListName("ABC12"));
    }

    @Test
    public void testIsValidListVariable_true() throws Exception {
        assertTrue(ValidationUtil.isValidListName("∟A"));
        assertTrue(ValidationUtil.isValidListName("∟A1"));
        assertTrue(ValidationUtil.isValidListName("∟A1234"));
        assertTrue(ValidationUtil.isValidListName("∟θ"));
        assertTrue(ValidationUtil.isValidListName("∟θθθθθ"));
    }

    @Test
    public void testIsValidProgramName_false() throws Exception {
        assertFalse(ValidationUtil.isValidProgramName("1"));
        assertFalse(ValidationUtil.isValidProgramName("ABCDEFGHI"));
        assertFalse(ValidationUtil.isValidProgramName("1ABCDEFG"));
        assertFalse(ValidationUtil.isValidProgramName("a1234567"));
        assertFalse(ValidationUtil.isValidProgramName("abc"));
    }

    @Test
    public void testIsValidProgramName_true() throws Exception {
        assertTrue(ValidationUtil.isValidProgramName("A"));
        assertTrue(ValidationUtil.isValidProgramName("A1"));
        assertTrue(ValidationUtil.isValidProgramName("A1234567"));
        assertTrue(ValidationUtil.isValidProgramName("θ"));
        assertTrue(ValidationUtil.isValidProgramName("θθθθθθθθ"));
    }
}