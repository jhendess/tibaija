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
import org.xlrnet.tibaija.commands.UnaryCommandOperator;
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
        env.registerExpressionFunction("+", new BinaryCommand(BinaryCommandOperator.PLUS));
        env.registerExpressionFunction("-", new BinaryCommand(BinaryCommandOperator.MINUS));
        env.registerExpressionFunction("*", new BinaryCommand(BinaryCommandOperator.MULTIPLY));
        env.registerExpressionFunction("/", new BinaryCommand(BinaryCommandOperator.DIVIDE));
        env.registerExpressionFunction("^", new BinaryCommand(BinaryCommandOperator.POWER));

        // Register more advances binary operators
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
    }

}
