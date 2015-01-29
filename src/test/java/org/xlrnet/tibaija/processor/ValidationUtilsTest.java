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
import org.xlrnet.tibaija.util.ValidationUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTest {

    @Test
    public void testIsValidLabelIdentifier_false() throws Exception {
        assertFalse(ValidationUtils.isValidLabelIdentifier("123"));
        assertFalse(ValidationUtils.isValidLabelIdentifier("AAA"));
        assertFalse(ValidationUtils.isValidLabelIdentifier("-"));
        assertFalse(ValidationUtils.isValidLabelIdentifier("*"));
        assertFalse(ValidationUtils.isValidLabelIdentifier("Ö"));
        assertFalse(ValidationUtils.isValidLabelIdentifier("ab"));
    }

    @Test
    public void testIsValidLabelIdentifier_true() throws Exception {
        assertTrue(ValidationUtils.isValidLabelIdentifier("A"));
        assertTrue(ValidationUtils.isValidLabelIdentifier("AA"));
        assertTrue(ValidationUtils.isValidLabelIdentifier("1"));
        assertTrue(ValidationUtils.isValidLabelIdentifier("A1"));
        assertTrue(ValidationUtils.isValidLabelIdentifier("θ"));
        assertTrue(ValidationUtils.isValidLabelIdentifier("θθ"));
    }

    @Test
    public void testIsValidProgramName_false() throws Exception {
        assertFalse(ValidationUtils.isValidProgramName("1"));
        assertFalse(ValidationUtils.isValidProgramName("ABCDEFGHI"));
        assertFalse(ValidationUtils.isValidProgramName("1ABCDEFG"));
        assertFalse(ValidationUtils.isValidProgramName("a1234567"));
        assertFalse(ValidationUtils.isValidProgramName("abc"));
    }

    @Test
    public void testIsValidProgramName_true() throws Exception {
        assertTrue(ValidationUtils.isValidProgramName("A"));
        assertTrue(ValidationUtils.isValidProgramName("A1"));
        assertTrue(ValidationUtils.isValidProgramName("A1234567"));
        assertTrue(ValidationUtils.isValidProgramName("θ"));
        assertTrue(ValidationUtils.isValidProgramName("θθθθθθθθ"));
    }
}