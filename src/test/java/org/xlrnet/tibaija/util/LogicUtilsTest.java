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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogicUtilsTest {

    @Test
    public void testAnd_false() throws Exception {
        assertFalse(LogicUtils.and(0, 0));
        assertFalse(LogicUtils.and(0, 1));
        assertFalse(LogicUtils.and(-1, 0));
    }

    @Test
    public void testAnd_true() throws Exception {
        assertTrue(LogicUtils.and(1, 1));
        assertTrue(LogicUtils.and(-1, 1));
        assertTrue(LogicUtils.and(1, 123));
    }

    @Test
    public void testNot_false() throws Exception {
        assertFalse(LogicUtils.not(1));
        assertFalse(LogicUtils.not(-1234));
        assertFalse(LogicUtils.not(56454651651.31241));
        assertFalse(LogicUtils.not(0.000000001));
    }

    @Test
    public void testNot_true() throws Exception {
        assertTrue(LogicUtils.not(0));
    }

    @Test
    public void testOr_false() throws Exception {
        assertFalse(LogicUtils.or(0, 0));
    }

    @Test
    public void testOr_true() throws Exception {
        assertTrue(LogicUtils.or(1, 0));
        assertTrue(LogicUtils.or(12, 123));
        assertTrue(LogicUtils.or(0, 4));
        assertTrue(LogicUtils.or(-1, 0));
        assertTrue(LogicUtils.or(-2, -45));
    }

    @Test
    public void testXor_false() throws Exception {
        assertFalse(LogicUtils.xor(12, 12));
        assertFalse(LogicUtils.xor(0, 0));
        assertFalse(LogicUtils.xor(-5, -1));
    }

    @Test
    public void testXor_true() throws Exception {
        assertTrue(LogicUtils.xor(1, 0));
        assertTrue(LogicUtils.xor(0, 1));
        assertTrue(LogicUtils.xor(123456, 0));
        assertTrue(LogicUtils.xor(0, 123456));
        assertTrue(LogicUtils.xor(-1, 0));
    }
}