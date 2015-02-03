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

package org.xlrnet.tibaija.io;

import org.xlrnet.tibaija.memory.Value;

import java.io.IOException;

/**
 * Interface for all kinds of calculator I/O.
 */
public interface CalculatorIO {

    /**
     * Print a set of characters to the main non-graphic output.
     *
     * @param charSequence
     *         A set of characters, e.g. a java String.
     */
    public void print(String charSequence);

    /**
     * Print a given string to the main non-graphic output and finish it with an newLine.
     *
     * @param text
     *         the String that should be outputted
     */
    public void printLine(String text);

    /**
     * Print the content of all given values on the main non-graphic output. After each value, a new-line must be
     * printed. Except for string-values, all values must should be printed right-aligned.
     * Each value must be formatted according to the currently configured modes.
     *
     * @param values
     *         One or more values. Note: Values are derived from AnswerVariables; so they are of course valid, too.
     */
    public void printLine(Value... values);

    /**
     * Reads a CharSequence from the input and returns it. This methods must block the main execution until the return
     * value is ready.
     *
     * @return A CharSequence from the user input.
     */
    public String readInput() throws IOException;

}
