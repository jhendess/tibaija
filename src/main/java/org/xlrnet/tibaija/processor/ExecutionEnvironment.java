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

import com.google.common.collect.ImmutableList;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.exception.CommandNotFoundException;
import org.xlrnet.tibaija.exception.DuplicateCommandException;
import org.xlrnet.tibaija.exception.TIRuntimeException;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.ReadOnlyCalculatorMemory;
import org.xlrnet.tibaija.memory.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides the main environment where programs and functions get executed.
 */
public class ExecutionEnvironment {

    CalculatorMemory memory;

    CalculatorIO calculatorIO;

    FullTIBasicVisitor visitor;

    Map<String, Command> commandMap = new HashMap<>();

    private ExecutionEnvironment(CalculatorMemory memory, CalculatorIO calculatorIO, FullTIBasicVisitor visitor) {
        this.memory = memory;
        this.calculatorIO = calculatorIO;
        this.visitor = visitor;
        visitor.setEnvironment(this);
    }

    /**
     * Instantiate a new environment without any preconfigured commands.
     *
     * @param memory
     *         The writable memory for the new environment.
     * @param calculatorIO
     *         The I/O device for the new environment.
     * @param visitor
     *         The visitor that should be used for running programs.
     * @return A new environment
     */
    public static ExecutionEnvironment newEnvironment(CalculatorMemory memory, CalculatorIO calculatorIO, FullTIBasicVisitor visitor) {
        return new ExecutionEnvironment(memory, calculatorIO, visitor);
    }

    /**
     * Instantiate a new environment without any preconfigured commands.
     *
     * @param virtualCalculator
     *         The virtual calculator with a configured I/O device and memory.
     * @param visitor
     *         The visitor that should be used for running programs.
     * @return A new environment
     */
    public static ExecutionEnvironment newEnvironment(VirtualCalculator virtualCalculator, FullTIBasicVisitor visitor) {
        return ExecutionEnvironment.newEnvironment(virtualCalculator.getMemory(), virtualCalculator.getIODevice(), visitor);
    }

    /**
     * Get access to the I/O device of the environment.
     *
     * @return Reference to the I/O device of the environment.
     */
    public CalculatorIO getCalculatorIO() {
        return calculatorIO;
    }

    /**
     * Get readable access to the calculator memory.
     *
     * @return Reference to the readable memory of the calculator.
     */
    public ReadOnlyCalculatorMemory getMemory() {
        return memory;
    }

    /**
     * Register a command or function in the execution environment. All programs and other commands or functions can
     * run the new command once it has been registered. Every command may only be associated with at most one execution
     * environment.
     *
     * @param commandName
     *         Name of the new command under which it can be accessed.
     * @param command
     *         An instance of the concrete command.
     */
    public void registerCommand(String commandName, Command command) throws TIRuntimeException {
        if (commandMap.get(commandName) != null)
            throw new DuplicateCommandException("Command already exists: " + commandName);

        if (command.getEnvironment() != null)
            throw new DuplicateCommandException("New command instance is already registered in another environment");

        command.setEnvironment(this);
        commandMap.put(commandName, command);
    }

    /**
     * Run a given {@link org.xlrnet.tibaija.processor.ExecutableProgram} inside this environment. The program will be
     * visited by the visitor with which the environment was instantiated.
     *
     * @param program
     *         The program to run.
     * @throws TIRuntimeException
     *         Will be thrown on errors while executing the program
     */
    public void run(ExecutableProgram program) throws TIRuntimeException {
        visitor.visit(program.getMainProgramContext());
    }

    /**
     * Run a previously registered function with the given arguments.
     *
     * @param commandName
     *         Internal name of the previously registered function to execute.
     * @param arguments
     *         The arguments with which the command will be called.
     * @return An optional return value.
     * @throws TIRuntimeException
     *         Can be thrown on type errors, internal problems or illegal parameters.
     */
    public Optional<Value> runRegisteredCommand(String commandName, Value... arguments) throws TIRuntimeException {
        Command command = commandMap.get(commandName);
        if (command == null)
            throw new CommandNotFoundException(-1, -1, commandName);

        ImmutableList<Value> argumentList = ImmutableList.copyOf(arguments);
        command.checkArguments(argumentList);

        return command.execute(argumentList);
    }

    /**
     * Return a reference to the writable memory of this environment.
     *
     * @return A reference to the writable memory of this environment.
     */
    protected CalculatorMemory getWritableMemory() {
        return memory;
    }

}
