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

import org.junit.Before;
import org.mockito.Mock;
import org.xlrnet.tibaija.TI83Plus;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.matchers.EqualsWithComplexDeltaMatcher;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.test.TestUtils;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * Abstract class with all necessary mocks.
 */
public class AbstractTI83PlusTest {

    @Mock
    CalculatorMemory mockedMemory;

    @Mock
    CalculatorIO mockedIO;

    VirtualCalculator calculator;

    @Before
    public void setUp() {
        calculator = new TI83Plus(mockedMemory, mockedIO);
    }

    protected void verifyLastResultValue(double realPart, double imaginaryPart) {
        verify(mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, imaginaryPart, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValue(double realPart) {
        verify(mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, TestUtils.DEFAULT_TOLERANCE)));
    }
}
