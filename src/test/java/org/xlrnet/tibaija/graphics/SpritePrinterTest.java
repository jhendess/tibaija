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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Tests for printing sprites using {@link SpritePrinter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SpritePrinterTest {

    private static final PixelState[][] INPUT_STATES = new PixelState[][]{
            new PixelState[]{PixelState.ON, PixelState.OFF},
            new PixelState[]{PixelState.OFF, PixelState.ON},
            new PixelState[]{PixelState.ON, PixelState.OFF},
    };

    private static final PixelSprite TEST_SPRITE = new PixelSprite(INPUT_STATES);

    @Mock
    Display mockedDisplay;

    SpritePrinter spritePrinter;

    @Before
    public void setup() {
        spritePrinter = new SpritePrinter();
        when(mockedDisplay.getHorizontalDimension()).thenReturn(4);
        when(mockedDisplay.getVerticalDimension()).thenReturn(3);
    }

    @Test
    public void testPrintSprite_basic() {
        spritePrinter.printSprite(TEST_SPRITE, mockedDisplay);

        // Verify first column
        verify(mockedDisplay).setPixel(PixelState.ON, 0, 0);
        verify(mockedDisplay).setPixel(PixelState.OFF, 0, 1);
        verify(mockedDisplay).setPixel(PixelState.ON, 0, 2);

        // Verify second column
        verify(mockedDisplay).setPixel(PixelState.OFF, 1, 0);
        verify(mockedDisplay).setPixel(PixelState.ON, 1, 1);
        verify(mockedDisplay).setPixel(PixelState.OFF, 1, 2);

        verify(mockedDisplay, never()).flush();
    }

    @Test
    public void testPrintSprite_invert() {
        spritePrinter.printSprite(TEST_SPRITE, mockedDisplay, 0, 0, true);

        // Verify first column
        verify(mockedDisplay).setPixel(PixelState.OFF, 0, 0);
        verify(mockedDisplay).setPixel(PixelState.ON, 0, 1);
        verify(mockedDisplay).setPixel(PixelState.OFF, 0, 2);

        // Verify second column
        verify(mockedDisplay).setPixel(PixelState.ON, 1, 0);
        verify(mockedDisplay).setPixel(PixelState.OFF, 1, 1);
        verify(mockedDisplay).setPixel(PixelState.ON, 1, 2);

        verify(mockedDisplay, never()).flush();
    }

    @Test
    public void testPrintSprite_offset() {
        spritePrinter.printSprite(TEST_SPRITE, mockedDisplay, 1, 2);

        // Verify first column
        verify(mockedDisplay).setPixel(PixelState.ON, 1, 2);
        verify(mockedDisplay, never()).setPixel(PixelState.OFF, 1, 3);
        verify(mockedDisplay, never()).setPixel(PixelState.ON, 1, 4);

        // Verify second column
        verify(mockedDisplay).setPixel(PixelState.OFF, 2, 2);
        verify(mockedDisplay, never()).setPixel(PixelState.ON, 2, 3);
        verify(mockedDisplay, never()).setPixel(PixelState.OFF, 2, 4);

        verify(mockedDisplay, never()).flush();
    }

    @Test
    public void testPrintSprite_offset_negative() {
        spritePrinter.printSprite(TEST_SPRITE, mockedDisplay, -1, -2);

        // Verify first column
        verify(mockedDisplay, never()).setPixel(PixelState.ON, -1, -2);
        verify(mockedDisplay, never()).setPixel(PixelState.OFF, -1, -1);
        verify(mockedDisplay, never()).setPixel(PixelState.ON, -1, 0);

        // Verify second column
        verify(mockedDisplay, never()).setPixel(PixelState.OFF, 0, -2);
        verify(mockedDisplay, never()).setPixel(PixelState.ON, 0, -1);
        verify(mockedDisplay).setPixel(PixelState.OFF, 0, 0);

        verify(mockedDisplay, never()).flush();
    }

}