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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Testsuite for all test concerning the interactive interpreter mode of the virtual TI-83+.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        IllegalControlFlowTests.class,      // Control flow statements are not allowed in interactive mode
        InterpretComparisonTest.class,      // Tests for correct comparisons
        InterpretLogicTest.class,           // Tests for correct logic operators
        InterpretStructuralTest.class,      // Tests for structural issues
        SimpleArithmeticsTest.class,        // Tests for simpler arithmetics
        InterpretListsTest.class            // Tests for all list logic
})
public class TI83PlusInterpretSuite extends AbstractTI83PlusTest {

}