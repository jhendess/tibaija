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

package org.xlrnet.tibaija.graphics;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PixelSprite} class.
 */
public class PixelSpriteTest {

    @Test
    public void testPixelSprite_constructor_basic() {
        PixelState[][] inputStates = new PixelState[][]{
                new PixelState[]{PixelState.ON, PixelState.OFF},
                new PixelState[]{PixelState.OFF, PixelState.ON},
                new PixelState[]{PixelState.ON, PixelState.OFF},
        };

        PixelSprite actualSprite = new PixelSprite(inputStates);

        assertEquals(2, actualSprite.getXDimension());
        assertEquals(3, actualSprite.getYDimension());

        assertArrayEquals(inputStates, actualSprite.getPixelStates());
    }

    @Test
    public void testPixelSprite_constructor_emptyDim() {
        PixelState[][] inputStates = new PixelState[][]{
                new PixelState[]{PixelState.ON, PixelState.OFF},
                new PixelState[]{},
        };

        PixelState[][] expectedStates = new PixelState[][]{
                new PixelState[]{PixelState.ON, PixelState.OFF},
                new PixelState[]{PixelState.OFF, PixelState.OFF},
        };

        PixelSprite actualSprite = new PixelSprite(inputStates);

        assertEquals(2, actualSprite.getXDimension());
        assertEquals(2, actualSprite.getYDimension());

        assertArrayEquals(expectedStates, actualSprite.getPixelStates());
    }

    @Test
    public void testPixelSprite_constructor_missingDim() {
        PixelState[][] inputStates = new PixelState[][]{
                new PixelState[]{PixelState.ON, PixelState.OFF},
                new PixelState[]{PixelState.OFF},
                new PixelState[]{PixelState.ON},
        };

        PixelState[][] expectedStates = new PixelState[][]{
                new PixelState[]{PixelState.ON, PixelState.OFF},
                new PixelState[]{PixelState.OFF, PixelState.OFF},
                new PixelState[]{PixelState.ON, PixelState.OFF},
        };

        PixelSprite actualSprite = new PixelSprite(inputStates);

        assertEquals(2, actualSprite.getXDimension());
        assertEquals(3, actualSprite.getYDimension());

        assertArrayEquals(expectedStates, actualSprite.getPixelStates());
    }

}