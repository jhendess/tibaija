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

import org.apache.commons.math3.complex.Complex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;
import org.mockito.Mock;
import org.xlrnet.tibaija.DummyCodeProvider;
import org.xlrnet.tibaija.TI83Plus;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.matchers.EqualsTIListMatcher;
import org.xlrnet.tibaija.matchers.EqualsWithComplexDeltaMatcher;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.DefaultCalculatorMemory;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;
import org.xlrnet.tibaija.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Abstract class with all necessary mocks.
 */
public class AbstractTI83PlusTest {

    @Rule
    public Timeout globalTimeout = new Timeout(1000);

    CalculatorMemory mockedMemory;

    @Mock
    CalculatorIO mockedIO;

    VirtualCalculator calculator;

    @Before
    public void setUp() {
        mockedMemory = spy(new DefaultCalculatorMemory());
        calculator = new TI83Plus(mockedMemory, mockedIO, new DummyCodeProvider());
    }

    protected void verifyNumberVariableValue(Variables.NumberVariable variable, double real, double imaginary) {
        final Complex actualComplex = mockedMemory.getNumberVariableValue(variable).complex();
        assertEquals("Actual real value doesn't match expected", real, actualComplex.getReal(), TestUtils.DEFAULT_TOLERANCE);
        assertEquals("Actual imaginary value doesn't match expected", imaginary, actualComplex.getImaginary(), TestUtils.DEFAULT_TOLERANCE);
    }

    protected void storeAndExecute(String snippet) {
        calculator.loadProgram("TEST", snippet);
        calculator.executeProgram("TEST");
    }

    protected void verifyLastResultValue(double realPart, double imaginaryPart) {
        verify(mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, imaginaryPart, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValue(double realPart) {
        verify(mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValueList(Complex... values) {
        verify(mockedMemory).setLastResult(argThat(new EqualsTIListMatcher(values, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValueList(Double... values) {
        Complex[] t = new Complex[values.length];
        for (int i = 0; i < values.length; i++)
            t[i] = Complex.valueOf(values[i]);
        verifyLastResultValueList(t);
    }

    protected void verifyLastResultValueWithBigTolerance(double realPart) {
        verify(mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, TestUtils.BIG_TOLERANCE)));
    }

    protected void verifyListVariableValue(String variable, Complex... expectedValues) {
        Value actual = mockedMemory.getListVariableValue(variable);
        Assert.assertThat(actual, new EqualsTIListMatcher(expectedValues, TestUtils.DEFAULT_TOLERANCE));
    }

    protected void verifyElementInListVariable(String variable, int element, double realPart) {
        verifyElementInListVariable(variable, element, realPart, 0);
    }

    protected void verifyElementInListVariable(String variable, int element, double realPart, double complexPart) {
        Complex actual = calculator.getMemory().getListVariableValue(variable).list().get(element);
        assertThat(Value.of(actual), new EqualsWithComplexDeltaMatcher(realPart, complexPart, TestUtils.DEFAULT_TOLERANCE));
    }

    protected void verifyListVariableSize(String variable, int expected) {
        int actual = calculator.getMemory().getListVariableValue(variable).list().size();
        assertEquals(expected, actual);
    }

}
