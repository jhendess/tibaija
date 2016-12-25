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

import org.junit.Test;
import org.xlrnet.tibaija.commons.CompareUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompareUtilTest {

    @Test
    public void testIsEqual() throws Exception {
        assertTrue(CompareUtil.isEqual(1, 1));
        assertFalse(CompareUtil.isEqual(2, 1));
    }

    @Test
    public void testIsGreaterOrEqual() throws Exception {
        assertTrue(CompareUtil.isGreaterOrEqual(1, 1));
        assertTrue(CompareUtil.isGreaterOrEqual(2, 1));
        assertFalse(CompareUtil.isGreaterOrEqual(1, 2));
    }

    @Test
    public void testIsGreaterThan() throws Exception {
        assertTrue(CompareUtil.isGreaterThan(2, 1));
        assertFalse(CompareUtil.isGreaterThan(1, 1));
        assertFalse(CompareUtil.isGreaterThan(1, 2));
    }

    @Test
    public void testIsLessOrEqual() throws Exception {
        assertTrue(CompareUtil.isLessOrEqual(1, 1));
        assertTrue(CompareUtil.isLessOrEqual(1, 2));
        assertFalse(CompareUtil.isLessOrEqual(2, 1));
    }

    @Test
    public void testIsLessThan() throws Exception {
        assertTrue(CompareUtil.isLessThan(1, 2));
        assertFalse(CompareUtil.isLessThan(1, 1));
        assertFalse(CompareUtil.isLessThan(2, 1));
    }

    @Test
    public void testIsNotEqual() throws Exception {
        assertTrue(CompareUtil.isNotEqual(1, 2));
        assertFalse(CompareUtil.isNotEqual(1, 1));
    }
}