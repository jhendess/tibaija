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
import org.junit.rules.Timeout;
import org.mockito.Mock;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.graphics.HomeScreen;
import org.xlrnet.tibaija.graphics.NullHomeScreen;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.io.DummyCodeProvider;
import org.xlrnet.tibaija.matchers.EqualsTIListMatcher;
import org.xlrnet.tibaija.matchers.EqualsTIStringMatcher;
import org.xlrnet.tibaija.matchers.EqualsWithComplexDeltaMatcher;
import org.xlrnet.tibaija.memory.*;
import org.xlrnet.tibaija.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Abstract class with all necessary mocks.
 */
public class AbstractTI83PlusTest {

    //@Rule
    public Timeout globalTimeout = new Timeout(1000);

    CalculatorMemory mockedMemory;

    @Mock
    CalculatorIO mockedIO;

    VirtualCalculator calculator;

    HomeScreen mockedHomeScreen;

    ExecutionEnvironment environment;

    DummyCodeProvider codeProvider;

    @Before
    public void setUp() {
        this.mockedMemory = spy(new DefaultCalculatorMemory());
        this.mockedHomeScreen = spy(new NullHomeScreen());
        this.codeProvider = new DummyCodeProvider();
        this.environment = ExecutionEnvironment.newEnvironment(this.mockedMemory, this.mockedIO, this.codeProvider, this.mockedHomeScreen);
        ExecutionEnvironmentFactory.registerDefaultCommands(this.environment);
        this.calculator = new TI83Plus(this.environment);
    }

    protected ExecutionEnvironment getEnvironment() {
        return this.environment;
    }

    protected void storeAndExecute(String snippet) {
        this.calculator.loadProgram("TEST", snippet);
        this.calculator.executeProgram("TEST");
    }

    protected void verifyElementInListVariable(String variable, int element, double realPart) {
        verifyElementInListVariable(variable, element, realPart, 0);
    }

    protected void verifyElementInListVariable(String variable, int element, double realPart, double complexPart) {
        Complex actual = this.calculator.getMemory().getListVariableValue(ListVariable.fromName(variable)).list().get(element);
        assertThat(Value.of(actual), new EqualsWithComplexDeltaMatcher(realPart, complexPart, TestUtils.DEFAULT_TOLERANCE));
    }

    protected void verifyLastResultValue(double realPart, double imaginaryPart) {
        verify(this.mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, imaginaryPart, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValue(double realPart) {
        verify(this.mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValue(String string) {
        verify(this.mockedMemory, atLeastOnce()).setLastResult(argThat(new EqualsTIStringMatcher(string)));
    }

    protected void verifyLastResultValueList(Complex... values) {
        verify(this.mockedMemory).setLastResult(argThat(new EqualsTIListMatcher(values, TestUtils.DEFAULT_TOLERANCE)));
    }

    protected void verifyLastResultValueList(Double... values) {
        Complex[] t = new Complex[values.length];
        for (int i = 0; i < values.length; i++)
            t[i] = Complex.valueOf(values[i]);
        verifyLastResultValueList(t);
    }

    protected void verifyLastResultValueWithBigTolerance(double realPart) {
        verify(this.mockedMemory).setLastResult(argThat(new EqualsWithComplexDeltaMatcher(realPart, TestUtils.BIG_TOLERANCE)));
    }

    protected void verifyListVariableSize(String variable, int expected) {
        int actual = this.calculator.getMemory().getListVariableValue(ListVariable.fromName(variable)).list().size();
        assertEquals(expected, actual);
    }

    protected void verifyListVariableValue(String variable, Complex... expectedValues) {
        Value actual = this.mockedMemory.getListVariableValue(ListVariable.fromName(variable));
        Assert.assertThat(actual, new EqualsTIListMatcher(expectedValues, TestUtils.DEFAULT_TOLERANCE));
    }

    protected void verifyNumberVariableValue(NumberVariable variable, double real, double imaginary) {
        final Complex actualComplex = this.mockedMemory.getNumberVariableValue(variable).complex();
        assertEquals("Actual real value doesn't match expected", real, actualComplex.getReal(), TestUtils.DEFAULT_TOLERANCE);
        assertEquals("Actual imaginary value doesn't match expected", imaginary, actualComplex.getImaginary(), TestUtils.DEFAULT_TOLERANCE);
    }

    protected void verifyStringVariableValue(StringVariable variable, String expected) {
        Value variableValue = this.mockedMemory.getStringVariableValue(variable);
        assertEquals(expected, variableValue.string());
    }

}
