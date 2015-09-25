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
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.CodeProvider;
import org.xlrnet.tibaija.DummyCodeProvider;
import org.xlrnet.tibaija.VirtualCalculator;
import org.xlrnet.tibaija.exception.CommandNotFoundException;
import org.xlrnet.tibaija.exception.DuplicateCommandException;
import org.xlrnet.tibaija.exception.TIRuntimeException;
import org.xlrnet.tibaija.graphics.*;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.memory.ReadOnlyCalculatorMemory;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.util.ValueFormatUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides the main environment where programs and functions get executed.
 */
public class ExecutionEnvironment {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionEnvironment.class);

    private final HomeScreen homeScreen;

    private final Stack<ExecutableProgram> programStack = new Stack<>();

    private final CalculatorMemory memory;

    private final CalculatorIO calculatorIO;

    private final CodeProvider codeProvider;

    private final FontRegistry fontRegistry;

    Map<String, Command> commandFunctionMap = new HashMap<>();

    Map<String, Command> expressionFunction = new HashMap<>();

    Map<String, Command> commandStatementMap = new HashMap<>();

    private DecimalDisplayMode decimalDisplayMode;

    private NumberDisplayFormat numberDisplayFormat;

    private ExecutionEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen, @NotNull FontRegistry fontRegistry) {
        this.memory = memory;
        this.calculatorIO = calculatorIO;
        this.codeProvider = codeProvider;
        this.homeScreen = homeScreen;
        this.fontRegistry = fontRegistry;
    }

    /**
     * Instantiate a new environment without any preconfigured commands and no font registry.
     *
     * @param memory
     *         The writable memory for the new environment.
     * @param calculatorIO
     *         The I/O device for the new environment.
     * @return A new environment
     */
    @NotNull
    public static ExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO) {
        return new ExecutionEnvironment(memory, calculatorIO, new DummyCodeProvider(), new NullHomeScreen(), new FontRegistry());
    }

    /**
     * Instantiate a new environment without any preconfigured commands, no code provider and no font registry.
     *
     * @param memory
     *         The writable memory for the new environment.
     * @param calculatorIO
     *         The I/O device for the new environment.
     * @param codeProvider
     *         The code provider for the new environment.
     * @return A new environment
     */
    @NotNull
    public static ExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider) {
        return new ExecutionEnvironment(memory, calculatorIO, codeProvider, new NullHomeScreen(), new FontRegistry());
    }

    /**
     * Instantiate a new environment without any preconfigured commands, but with an existing code provider, home screen
     * and font registry.
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
     * @return A new environment
     */
    @NotNull
    public static ExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen, @NotNull FontRegistry fontRegistry) {
        return new ExecutionEnvironment(memory, calculatorIO, codeProvider, homeScreen, fontRegistry);
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
    public static ExecutionEnvironment newEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen) {
        return new ExecutionEnvironment(memory, calculatorIO, codeProvider, homeScreen, new FontRegistry());
    }

    /**
     * Instantiate a new environment without any preconfigured commands.
     *
     * @param virtualCalculator
     *         The virtual calculator with a configured I/O device and memory.
     * @return A new environment
     */
    @NotNull
    public static ExecutionEnvironment newEnvironment(@NotNull VirtualCalculator virtualCalculator) {
        return ExecutionEnvironment.newEnvironment(virtualCalculator.getMemory(), virtualCalculator.getIODevice());
    }

    /**
     * Formats a given {@link Value} object according to the currently configured {@link DecimalDisplayMode}. The
     * current configuration can be changed through {@link #setDecimalDisplayMode(DecimalDisplayMode)}
     *
     * @param value
     *         The value to format.
     * @return The formatted value.
     */
    public String formatValue(Value value) {
        return ValueFormatUtils.formatValue(value, numberDisplayFormat, decimalDisplayMode);
    }

    /**
     * Get access to the I/O device of the environment.
     *
     * @return Reference to the I/O device of the environment.
     */
    @NotNull
    public CalculatorIO getCalculatorIO() {
        return calculatorIO;
    }

    public CodeProvider getCodeProvider() {
        return codeProvider;
    }

    public DecimalDisplayMode getDecimalDisplayMode() {
        return decimalDisplayMode;
    }

    /**
     * Set the current mode of how decimals should be displayed. Setting this value will affect all formatting through
     * {@link #formatValue(Value)}.
     *
     * @param decimalDisplayMode
     *         The display mode to set.
     */
    public void setDecimalDisplayMode(@NotNull DecimalDisplayMode decimalDisplayMode) {
        this.decimalDisplayMode = decimalDisplayMode;
    }

    public FontRegistry getFontRegistry() {
        return fontRegistry;
    }

    /**
     * Returns the currently registered {@link HomeScreen} implementation for this environment. The home screen should
     * be used for printing out basic texts and data without any graphical components.
     *
     * @return the currently registered home screen for this environment.
     */
    @NotNull
    public HomeScreen getHomeScreen() {
        return homeScreen;
    }

    /**
     * Get readable access to the calculator memory.
     *
     * @return Reference to the readable memory of the calculator.
     */
    @NotNull
    public ReadOnlyCalculatorMemory getMemory() {
        return memory;
    }

    @NotNull
    public NumberDisplayFormat getNumberDisplayFormat() {
        return numberDisplayFormat;
    }

    public void setNumberDisplayFormat(NumberDisplayFormat numberDisplayFormat) {
        this.numberDisplayFormat = numberDisplayFormat;
    }

    /**
     * Get the stack of current program hierarchy. Whenever a new subprogram is executed, it must be placed on this
     * stack to provide a context for accessing the program's labels. When the execution of a program has finished, it
     * must be removed from this stack.
     *
     * @return the stack of current program hierarchy.
     */
    @NotNull
    public Stack<ExecutableProgram> getProgramStack() {
        return programStack;
    }

    /**
     * Register a command as a function command in the execution environment. All programs and other commands can use
     * the new function once it has been registered. Every command may only be associated with at most one execution
     * environment. If a command is registered as a command function (i.e. through this function) it cannot be used as
     * an expression and won't return any value but will probably modify the system state (e.g. display output).
     *
     * @param commandName
     *         Name of the new command under which it can be accessed.
     * @param command
     *         An instance of the concrete command.
     */
    public void registerCommandFunction(@NotNull String commandName, @NotNull Command command) throws TIRuntimeException {
        checkNotNull(commandName);
        checkNotNull(command);

        checkArgument(StringUtils.isNotBlank(commandName), "Function command name may not be blank");
        checkArgument(StringUtils.isAllUpperCase(commandName.substring(0, 1)), "Function command name must begin with a uppercase letter");

        if (commandFunctionMap.get(commandName) != null)
            throw new DuplicateCommandException("Command function is already registered: " + commandName);

        if (command.getEnvironment() != null)
            throw new DuplicateCommandException("New command instance is already registered in another environment");

        command.setEnvironment(this);
        commandFunctionMap.put(commandName, command);

        LOGGER.debug("Registered new command function '{}'", commandName);
    }

    /**
     * Register a command as a statement in the execution environment. All programs and other commands can use the new
     * statement once it has been registered. Every command may only be associated with at most one execution
     * environment. If a command is registered as a command statement (i.e. through this function) it will not be
     * available in expressions and should not be called with parentheses. E.g. "DISP 123". Command statements should be
     * used when the system state must be manipulated (e.g. display output).
     *
     * @param commandName
     *         Name of the new command under which it can be accessed. Must begin with an uppercase letter.
     * @param command
     *         An instance of the concrete command.
     */
    public void registerCommandStatement(@NotNull String commandName, @NotNull Command command) throws TIRuntimeException {
        checkNotNull(commandName);
        checkNotNull(command);

        checkArgument(StringUtils.isNotBlank(commandName), "Command statement name may not be blank");
        checkArgument(StringUtils.isAllUpperCase(commandName.substring(0, 1)), "Command statement name must begin with a uppercase letter");

        if (commandStatementMap.get(commandName) != null)
            throw new DuplicateCommandException("Command statement is already registered: " + commandName);

        if (command.getEnvironment() != null)
            throw new DuplicateCommandException("New command instance is already registered in another environment");

        command.setEnvironment(this);
        commandStatementMap.put(commandName, command);

        LOGGER.debug("Registered new command statement '{}'", commandName);
    }

    /**
     * Register a command as an expression function in the execution environment. All programs and other commands can
     * use the new function once it has been registered. Every command may only be associated with at most one execution
     * environment. If a command is registered as a function expression (i.e. through this function) it can be used in
     * or as an expression and must return a value.
     *
     * @param commandName
     *         Name of the new command under which it can be accessed.
     * @param command
     *         An instance of the concrete command.
     */
    public void registerExpressionFunction(@NotNull String commandName, @NotNull Command command) throws TIRuntimeException {
        checkNotNull(commandName);
        checkNotNull(command);

        checkArgument(StringUtils.isNotBlank(commandName), "Expression function name may not be blank");
        Character firstChar = commandName.charAt(0);
        checkArgument(CharUtils.isAsciiAlphaLower(firstChar) || !CharUtils.isAsciiAlphanumeric(firstChar), "Expression function name must begin with a lowercase letter or be non-alphanumeric");

        if (expressionFunction.get(commandName) != null)
            throw new DuplicateCommandException("Function expression name is already registered: " + commandName);

        if (command.getEnvironment() != null)
            throw new DuplicateCommandException("New command instance is already registered in another environment");

        command.setEnvironment(this);
        expressionFunction.put(commandName, command);

        LOGGER.debug("Registered new expression function '{}'", commandName);
    }

    /**
     * Run a given {@link org.xlrnet.tibaija.processor.ExecutableProgram} inside this environment. The program will be
     * visited with the given visitor.
     *
     * @param program
     *         The program to run.
     * @param visitor
     *         The visitor implementation that should run this program.
     * @throws TIRuntimeException
     *         Will be thrown on errors while executing the program
     */
    public void run(@NotNull ExecutableProgram program, @NotNull FullTIBasicVisitor visitor) throws TIRuntimeException {
        visitor.setEnvironment(this);
        programStack.push(program);
        visitor.visit(program.getMainProgramContext());
        programStack.pop();
    }

    /**
     * Run a previously registered command function with the given arguments. The command to run must be registered as a
     * function through {@link #registerCommandFunction(String, Command)}. The return value of the function will be
     * returned and as an {@link Optional}.
     *
     * @param commandName
     *         Internal name of the previously registered function to execute.
     * @param arguments
     *         The arguments with which the command will be called.
     * @return An optional return value.
     * @throws TIRuntimeException
     *         Can be thrown on type errors, internal problems or illegal parameters.
     */
    @NotNull
    public Optional<Value> runRegisteredCommandFunction(@NotNull String commandName, @NotNull Parameter... arguments) throws TIRuntimeException {
        Command command = commandFunctionMap.get(commandName);
        if (command == null)
            throw new CommandNotFoundException(-1, -1, commandName);

        return internalExecuteCommand(command, arguments);
    }

    /**
     * Run a previously registered command statement with the given arguments. The command to run must be registered as
     * a statement command through {@link #registerCommandStatement(String, Command)}. The return value of the function
     * will be <i>not</i> returned.
     *
     * @param commandName
     *         Internal name of the previously registered statement to execute.
     * @param arguments
     *         The arguments with which the command will be called.
     * @throws TIRuntimeException
     *         Can be thrown on type errors, internal problems or illegal parameters.
     */
    public void runRegisteredCommandStatement(@NotNull String commandName, @NotNull Parameter... arguments) throws TIRuntimeException {
        Command command = commandStatementMap.get(commandName);
        if (command == null)
            throw new CommandNotFoundException(-1, -1, commandName);

        internalExecuteCommand(command, arguments);
    }

    /**
     * Run a previously registered command function with the given arguments. The command to run must be registered as a
     * function through {@link #registerExpressionFunction(String, Command)}. The return value of the function will be
     * returned and as an {@link Optional}.
     *
     * @param commandName
     *         Internal name of the previously registered function to execute.
     * @param arguments
     *         The arguments with which the command will be called.
     * @return An optional return value.
     * @throws TIRuntimeException
     *         Can be thrown on type errors, internal problems or illegal parameters.
     */
    @NotNull
    public Optional<Value> runRegisteredExpressionFunction(@NotNull String commandName, @NotNull Parameter... arguments) throws TIRuntimeException {
        Command command = expressionFunction.get(commandName);
        if (command == null)
            throw new CommandNotFoundException(-1, -1, commandName);

        return internalExecuteCommand(command, arguments);
    }

    /**
     * Run a previously registered command function with the given arguments. The command to run must be registered as a
     * function through {@link #registerExpressionFunction(String, Command)}. The return value of the function will be
     * returned and as an {@link Optional}.
     *
     * @param commandName
     *         Internal name of the previously registered function to execute.
     * @param arguments
     *         The arguments with which the command will be called.
     * @return An optional return value.
     * @throws TIRuntimeException
     *         Can be thrown on type errors, internal problems or illegal parameters.
     */
    @NotNull
    public Optional<Value> runRegisteredExpressionFunction(@NotNull String commandName, @NotNull Value... arguments) throws TIRuntimeException {
        Parameter[] parameters = new Parameter[arguments.length];

        for (int i = 0, argumentsLength = arguments.length; i < argumentsLength; i++) {
            Value argument = arguments[i];
            parameters[i] = Parameter.value(argument);
        }

        return runRegisteredExpressionFunction(commandName, parameters);
    }

    /**
     * Return a reference to the writable memory of this environment.
     *
     * @return A reference to the writable memory of this environment.
     */
    @NotNull
    protected CalculatorMemory getWritableMemory() {
        return memory;
    }

    @NotNull
    private Optional<Value> internalExecuteCommand(Command command, @NotNull Parameter[] arguments) {
        ImmutableList<Parameter> argumentList = ImmutableList.copyOf(arguments);
        command.checkArguments(argumentList);
        return command.execute(argumentList);
    }
}
