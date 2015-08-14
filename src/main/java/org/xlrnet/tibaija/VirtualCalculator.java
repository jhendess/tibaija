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

package org.xlrnet.tibaija;

import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;

/**
 * This is the basic interface for accessing the virtual hardware of a TI-Basic capable calculator.
 * It provides methods for accessing the internal memory model, I/O, calc settings and built-in functions.
 * <p/>
 * Created by xolor on 17.01.15.
 */
public interface VirtualCalculator {

    public void executeProgram(String programName) throws ProgramNotFoundException;

    /**
     * Returns a reference to the IODevice of the virtual device. The IO device provides methods for reading
     * inputs and printing output.
     */
    public CalculatorIO getIODevice();

    /**
     * Returns a reference to internal memory model of the virtual calculator. The memory model provides
     * methods for both reading from and writing to the calculator's variables and programs.
     */
    public CalculatorMemory getMemory();

    /**
     * Execute and the given input string on the virtual calculator. This will execute in a stateful manner and
     * might change the internal state of the calculator. This method should be used for short interpretations and
     * not full programs.
     *
     * @param input
     *         The commands to be interpreted. The leading colon may be omitted.
     */
    public void interpret(String input);

    /**
     * Load the given program with the given program code into main memory and do all neccessary steps to make it
     * executable.
     *
     * @param programName
     *         Internal name of the program, must be one to eight capital letters or numbers.
     * @param programCode
     *         Valid TI-Basic code
     */
    public void loadProgram(String programName, CharSequence programCode);

}