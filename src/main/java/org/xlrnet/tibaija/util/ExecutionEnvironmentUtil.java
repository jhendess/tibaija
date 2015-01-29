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

import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.commands.BinaryCommand;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.processor.Command;
import org.xlrnet.tibaija.processor.ExecutionEnvironment;
import org.xlrnet.tibaija.processor.FullTIBasicVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating ExecutionEnvironments with preconfigured commands. You own commands should use the
 * registerGlobalCommand() function of this class to register themselves.
 */
public class ExecutionEnvironmentUtil {

    private static Map<String, Command> commandMap = new HashMap<>();

    public static ExecutionEnvironment newDefaultEnvironment(VirtualCalculator virtualCalculator, FullTIBasicVisitor visitor) {
        return newDefaultEnvironment(virtualCalculator.getMemory(), virtualCalculator.getIODevice(), visitor);
    }

    private static ExecutionEnvironment newDefaultEnvironment(CalculatorMemory memory, CalculatorIO ioDevice, FullTIBasicVisitor visitor) {
        ExecutionEnvironment env = ExecutionEnvironment.newEnvironment(memory, ioDevice, visitor);
        registerCommands(env);
        return env;
    }

    private static void registerCommands(ExecutionEnvironment env) {
        env.registerCommand("+", BinaryCommand.newAddCommand());
        env.registerCommand("-", BinaryCommand.newSubtractCommand());
        env.registerCommand("*", BinaryCommand.newMultiplyCommand());
        env.registerCommand("/", BinaryCommand.newDivideCommand());
        env.registerCommand("^", BinaryCommand.newPowerCommand());
    }

}
