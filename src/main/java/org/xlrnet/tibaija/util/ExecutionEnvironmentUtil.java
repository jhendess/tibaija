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

package org.xlrnet.tibaija.util;

import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.commands.BinaryCommand;
import org.xlrnet.tibaija.commands.BinaryCommandOperator;
import org.xlrnet.tibaija.commands.UnaryCommand;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.processor.ExecutionEnvironment;

/**
 * Static factory for creating ExecutionEnvironments with preconfigured commands.
 */
public class ExecutionEnvironmentUtil {

    @NotNull
    public static ExecutionEnvironment newDefaultEnvironment(@NotNull VirtualCalculator virtualCalculator) {
        return newDefaultEnvironment(virtualCalculator.getMemory(), virtualCalculator.getIODevice());
    }

    @NotNull
    private static ExecutionEnvironment newDefaultEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO ioDevice) {
        ExecutionEnvironment env = ExecutionEnvironment.newEnvironment(memory, ioDevice);
        registerCommands(env);
        return env;
    }

    private static void registerCommands(@NotNull ExecutionEnvironment env) {
        // Register binary arithmetic operators
        env.registerCommand("+", new BinaryCommand(BinaryCommandOperator.PLUS));
        env.registerCommand("-", new BinaryCommand(BinaryCommandOperator.MINUS));
        env.registerCommand("*", new BinaryCommand(BinaryCommandOperator.MULTIPLY));
        env.registerCommand("/", new BinaryCommand(BinaryCommandOperator.DIVIDE));
        env.registerCommand("^", new BinaryCommand(BinaryCommandOperator.POWER));

        // Register more advances binary operators
        env.registerCommand("×√", new BinaryCommand(BinaryCommandOperator.NTH_ROOT));
        env.registerCommand("nCr", new BinaryCommand(BinaryCommandOperator.NCR));
        env.registerCommand("nPr", new BinaryCommand(BinaryCommandOperator.NPR));

        // Register unary arithmetic operators
        env.registerCommand("²", new UnaryCommand(UnaryCommand.UnaryCommandOperator.SQUARED));
        env.registerCommand("³", new UnaryCommand(UnaryCommand.UnaryCommandOperator.CUBED));
        env.registerCommand("√(", new UnaryCommand(UnaryCommand.UnaryCommandOperator.SQUARE_ROOT));
        env.registerCommand("∛(", new UnaryCommand(UnaryCommand.UnaryCommandOperator.CUBIC_ROOT));
        env.registerCommand("!", new UnaryCommand(UnaryCommand.UnaryCommandOperator.FACTORIAL));
        
        // Register comparison operators
        env.registerCommand("=", new BinaryCommand(BinaryCommandOperator.EQUALS));
        env.registerCommand("≠", new BinaryCommand(BinaryCommandOperator.NOT_EQUALS));
        env.registerCommand("<", new BinaryCommand(BinaryCommandOperator.LESS_THAN));
        env.registerCommand("≤", new BinaryCommand(BinaryCommandOperator.LESS_EQUALS));
        env.registerCommand(">", new BinaryCommand(BinaryCommandOperator.GREATER_THAN));
        env.registerCommand("≥", new BinaryCommand(BinaryCommandOperator.GREATER_EQUALS));

        // Register logical operators
        env.registerCommand("and", new BinaryCommand(BinaryCommandOperator.AND));
        env.registerCommand("or", new BinaryCommand(BinaryCommandOperator.OR));
        env.registerCommand("xor", new BinaryCommand(BinaryCommandOperator.XOR));
        env.registerCommand("not(", new UnaryCommand(UnaryCommand.UnaryCommandOperator.NOT));
    }

}
