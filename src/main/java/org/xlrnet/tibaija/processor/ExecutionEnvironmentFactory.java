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

package org.xlrnet.tibaija.processor;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.commands.io.ClearHomeCommand;
import org.xlrnet.tibaija.commands.io.DisplayCommand;
import org.xlrnet.tibaija.commands.io.OutputCommand;
import org.xlrnet.tibaija.commands.math.BinaryCommand;
import org.xlrnet.tibaija.commands.math.BinaryCommandOperator;
import org.xlrnet.tibaija.commands.math.UnaryCommand;
import org.xlrnet.tibaija.commands.math.UnaryCommandOperator;
import org.xlrnet.tibaija.graphics.*;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.io.CodeProvider;
import org.xlrnet.tibaija.io.ConsoleIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.DefaultCalculatorMemory;

import java.io.*;
import java.nio.file.Paths;

/**
 * Static factory for creating ExecutionEnvironments with preconfigured commands.
 */
public class ExecutionEnvironmentFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionEnvironmentFactory.class);

    /**
     * Creates a new {@link InternalExecutionEnvironment} using the given {@link CodeProvider} and configures it. All
     * components will be initialized with the default settings.
     *
     * @param codeProvider
     *         The code provider to use for looking up new programs.
     * @return A
     * @throws IOException
     */
    public static InternalExecutionEnvironment newDefaultEnvironment(CodeProvider codeProvider) throws IOException {
        Reader reader;
        Writer writer;

        if (System.console() != null) {
            Console console = System.console();
            reader = console.reader();
            writer = console.writer();
            LOGGER.debug("Initialised native system console");
        } else {
            reader = new InputStreamReader(System.in);
            writer = new OutputStreamWriter(System.out);
            LOGGER.debug("Initialised system I/O streams");
        }

        CalculatorIO io = new ConsoleIO(reader, writer);
        CalculatorMemory memory = new DefaultCalculatorMemory();
        HomeScreen homeScreen = new TI83PlusHomeScreen();
        FontRegistry fontRegistry = new FontRegistry();
        Display display = new LanternaDisplay();
        fontRegistry.registerFont(Paths.get("largeFont.json"), FontConstants.FONT_LARGE);
        fontRegistry.registerFont(Paths.get("smallFont.json"), FontConstants.FONT_SMALL);

        InternalExecutionEnvironment internalExecutionEnvironment = ExecutionEnvironmentFactory.newEnvironment(memory, io, codeProvider, homeScreen, fontRegistry, display);
        registerDefaultCommands(internalExecutionEnvironment);
        return internalExecutionEnvironment;
    }

    static void registerDefaultCommands(@NotNull InternalExecutionEnvironment env) {
        // Register binary arithmetic operators
        env.registerExpressionFunction("+", new BinaryCommand(BinaryCommandOperator.PLUS));
        env.registerExpressionFunction("-", new BinaryCommand(BinaryCommandOperator.MINUS));
        env.registerExpressionFunction("*", new BinaryCommand(BinaryCommandOperator.MULTIPLY));
        env.registerExpressionFunction("/", new BinaryCommand(BinaryCommandOperator.DIVIDE));
        env.registerExpressionFunction("^", new BinaryCommand(BinaryCommandOperator.POWER));

        // Register advanced binary operators
        env.registerExpressionFunction("×√", new BinaryCommand(BinaryCommandOperator.NTH_ROOT));
        env.registerExpressionFunction("nCr", new BinaryCommand(BinaryCommandOperator.NCR));
        env.registerExpressionFunction("nPr", new BinaryCommand(BinaryCommandOperator.NPR));

        // Register unary arithmetic operators
        env.registerExpressionFunction("²", new UnaryCommand(UnaryCommandOperator.SQUARED));
        env.registerExpressionFunction("³", new UnaryCommand(UnaryCommandOperator.CUBED));
        env.registerExpressionFunction("√(", new UnaryCommand(UnaryCommandOperator.SQUARE_ROOT));
        env.registerExpressionFunction("∛(", new UnaryCommand(UnaryCommandOperator.CUBIC_ROOT));
        env.registerExpressionFunction("!", new UnaryCommand(UnaryCommandOperator.FACTORIAL));

        // Register comparison operators
        env.registerExpressionFunction("=", new BinaryCommand(BinaryCommandOperator.EQUALS));
        env.registerExpressionFunction("≠", new BinaryCommand(BinaryCommandOperator.NOT_EQUALS));
        env.registerExpressionFunction("<", new BinaryCommand(BinaryCommandOperator.LESS_THAN));
        env.registerExpressionFunction("≤", new BinaryCommand(BinaryCommandOperator.LESS_EQUALS));
        env.registerExpressionFunction(">", new BinaryCommand(BinaryCommandOperator.GREATER_THAN));
        env.registerExpressionFunction("≥", new BinaryCommand(BinaryCommandOperator.GREATER_EQUALS));

        // Register logical operators
        env.registerExpressionFunction("and", new BinaryCommand(BinaryCommandOperator.AND));
        env.registerExpressionFunction("or", new BinaryCommand(BinaryCommandOperator.OR));
        env.registerExpressionFunction("xor", new BinaryCommand(BinaryCommandOperator.XOR));
        env.registerExpressionFunction("not(", new UnaryCommand(UnaryCommandOperator.NOT));

        // Register I/O commands for home screen
        env.registerCommandStatement("Disp", new DisplayCommand());
        env.registerCommandFunction("Output", new OutputCommand());
        env.registerCommandStatement("ClrHome", new ClearHomeCommand());
    }

    /**
     * Instantiate a new environment without any preconfigured commands, but with an existing code provider, home screen
     * and font registry. The returned environment is not yet started.
     *
     * @param memory
     *         The writable memory for the new environment.
     * @param calculatorIO
     *         The I/O device for the new environment.
     * @param codeProvider
     *         The code provider for the new environment.
     * @param homeScreen
     *         The home screen on which should be printed.
     * @param fontRegistry
     *         The registry with already configured fonts.
     * @param display
     * @return A new environment
     */
    @NotNull
    public static InternalExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen, @NotNull FontRegistry fontRegistry, @NotNull Display display) {
        InternalExecutionEnvironment environment = new InternalExecutionEnvironment(memory, calculatorIO, codeProvider, homeScreen, fontRegistry, display);
        return environment;
    }

    /**
     * Instantiate a new environment without any preconfigured commands, but with a code provider and a home screen.
     *
     * @param memory
     *         The writable memory for the new environment.
     * @param calculatorIO
     *         The I/O device for the new environment.
     * @param codeProvider
     *         The code provider for the new environment.
     * @param homeScreen
     *         The home screen on which should be printed.
     * @return A new environment
     */
    @NotNull
    public static InternalExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen) {
        return newEnvironment(memory, calculatorIO, codeProvider, homeScreen, new FontRegistry(), new LanternaDisplay());
    }
}
