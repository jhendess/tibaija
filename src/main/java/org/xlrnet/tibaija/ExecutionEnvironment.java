/*
 * Copyright (c) 2016 Jakob Hendeß
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

import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.graphics.DecimalDisplayMode;
import org.xlrnet.tibaija.graphics.FontRegistry;
import org.xlrnet.tibaija.graphics.HomeScreen;
import org.xlrnet.tibaija.graphics.NumberDisplayFormat;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.io.CodeProvider;
import org.xlrnet.tibaija.memory.ReadOnlyCalculatorMemory;

/**
 * Created by xolor on 26.12.16.
 */
public interface ExecutionEnvironment {

    /**
     * Starts the environment and all its components.
     */
    void boot();

    /**
     * Executes a program with the given name from memory. If the program does not exist in memory, the default {@link
     * CodeProvider} will be used for loading the file into memory.
     *
     * @param programName
     *         name of the program to execute.
     * @throws ProgramNotFoundException
     *         thrown if the program exists not in memory and can't be found using the default {@link CodeProvider}.
     */
    void executeProgram(String programName) throws ProgramNotFoundException;

    /**
     * Formats a given {@link Value} object according to the currently configured {@link DecimalDisplayMode}. The
     * current configuration can be changed through {@link #setDecimalDisplayMode(DecimalDisplayMode)}
     *
     * @param value
     *         The value to format.
     * @return The formatted value.
     */
    String formatValue(Value value);

    /**
     * Get access to the I/O device of the environment.
     *
     * @return Reference to the I/O device of the environment.
     */
    @NotNull
    CalculatorIO getCalculatorIO();

    @NotNull
    CodeProvider getCodeProvider();

    @NotNull
    DecimalDisplayMode getDecimalDisplayMode();

    /**
     * Set the current mode of how decimals should be displayed. Setting this value will affect all formatting through
     * {@link #formatValue(Value)}.
     *
     * @param decimalDisplayMode
     *         The display mode to set.
     */
    void setDecimalDisplayMode(@NotNull DecimalDisplayMode decimalDisplayMode);

    @NotNull
    FontRegistry getFontRegistry();

    /**
     * Returns the currently registered {@link HomeScreen} implementation for this environment. The home screen should
     * be used for printing out basic texts and data without any graphical components.
     *
     * @return the currently registered home screen for this environment.
     */
    @NotNull
    HomeScreen getHomeScreen();

    /**
     * Get readable access to the calculator memory.
     *
     * @return Reference to the readable memory of the calculator.
     */
    @NotNull
    ReadOnlyCalculatorMemory getMemory();

    @NotNull
    NumberDisplayFormat getNumberDisplayFormat();

    void setNumberDisplayFormat(NumberDisplayFormat numberDisplayFormat);

    /**
     * Execute and the given input string on the virtual calculator. This will execute in a stateful manner and
     * might change the internal state of the calculator. This method should be used for short interpretations and
     * not full programs.
     *
     * @param input
     *         The commands to be interpreted. The leading colon may be omitted.
     */
    void interpret(String input);

    /**
     * Load the given program with the given program code into main memory and do all neccessary steps to make it
     * executable.
     *
     * @param programName
     *         Internal name of the program, must be one to eight capital letters or numbers.
     * @param programCode
     *         Valid TI-Basic code
     */
    void loadProgram(String programName, CharSequence programCode);

    /**
     * Shuts down the execution environment and all components.
     */
    void shutdown();
}
