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
import org.xlrnet.tibaija.ExecutionEnvironment;
import org.xlrnet.tibaija.commons.ValidationUtil;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.exception.*;
import org.xlrnet.tibaija.graphics.*;
import org.xlrnet.tibaija.io.*;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.memory.ReadOnlyCalculatorMemory;
import org.xlrnet.tibaija.memory.ValueFormatUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Internal implementation of the {@link ExecutionEnvironment}.
 */
public class InternalExecutionEnvironment implements ExecutionEnvironment {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalExecutionEnvironment.class);

    private final HomeScreen homeScreen;

    private final Stack<ExecutableProgram> programStack = new Stack<>();

    private final CalculatorMemory memory;

    private final CalculatorIO calculatorIO;

    private final CodeProvider codeProvider;

    private final FontRegistry fontRegistry;

    private final Map<String, Command> commandFunctionMap = new HashMap<>();

    private final Map<String, Command> expressionFunction = new HashMap<>();

    private final Map<String, Command> commandStatementMap = new HashMap<>();

    private final Preprocessor preprocessor = new Preprocessor();

    private final Display display;

    private final KeyProvider keyProvider;

    private final KeyMapper keyMapper;

    private DecimalDisplayMode decimalDisplayMode = DecimalDisplayMode.FLOAT;

    private NumberDisplayFormat numberDisplayFormat = NumberDisplayFormat.NORMAL;

    protected InternalExecutionEnvironment(@NotNull CalculatorMemory memory, @NotNull CalculatorIO calculatorIO, @NotNull CodeProvider codeProvider, @NotNull HomeScreen homeScreen, @NotNull FontRegistry fontRegistry, @NotNull Display display, @NotNull KeyProvider keyProvider, @NotNull KeyMapper keyMapper) {
        this.memory = memory;
        this.calculatorIO = calculatorIO;
        this.codeProvider = codeProvider;
        this.homeScreen = homeScreen;
        this.fontRegistry = fontRegistry;
        this.display = display;
        this.keyProvider = keyProvider;
        this.keyMapper = keyMapper;
    }

    @Override
    public void boot() {
        LOGGER.debug("Booting environment");
        try {
            display.open();
        } catch (IOException e) {
            LOGGER.error("Opening display failed", e);
            throw new TIGraphicsException("Opening display failed", e);
        }
        homeScreen.configure(this, display);
        LOGGER.info("Environment booted");
    }

    @Override
    public void executeProgram(String programName) throws ProgramNotFoundException {
        String upperCaseProgramName = programName.toUpperCase();
        if (!getMemory().containsProgram(upperCaseProgramName)) {
            try {
                loadProgram(upperCaseProgramName, this.codeProvider.getProgramCode(upperCaseProgramName));
            } catch (IOException e) {
                LOGGER.error("Loading external code failed", e);
            }
        }

        ExecutableProgram executableProgram = getMemory().getStoredProgram(upperCaseProgramName);

        LOGGER.info("Starting program '{}'", upperCaseProgramName);

        run(executableProgram, new FullTIBasicVisitor());
    }

    @Override
    public String formatValue(Value value) {
        return ValueFormatUtils.formatValue(value, this.numberDisplayFormat, this.decimalDisplayMode);
    }

    @Override
    @NotNull
    public CalculatorIO getCalculatorIO() {
        return this.calculatorIO;
    }

    @Override
    @NotNull
    public CodeProvider getCodeProvider() {
        return this.codeProvider;
    }

    @Override
    @NotNull
    public DecimalDisplayMode getDecimalDisplayMode() {
        return this.decimalDisplayMode;
    }

    @Override
    public void setDecimalDisplayMode(@NotNull DecimalDisplayMode decimalDisplayMode) {
        this.decimalDisplayMode = decimalDisplayMode;
    }

    @Override
    @NotNull
    public FontRegistry getFontRegistry() {
        return this.fontRegistry;
    }

    @Override
    @NotNull
    public HomeScreen getHomeScreen() {
        return this.homeScreen;
    }

    /**
     * Queries the configured {@link KeyProvider} for the last pressed key and maps its value using the configured
     * {@link KeyMapper} to an integer. If no key has been pressed since the last call of this method, zero will be
     * returned.
     *
     * @return The last pressed key or zero if none has been pressed since the last method call.
     */
    public int getLastPressedKey() {
        LOGGER.trace("Fetching last pressed key");
        int lastPressedKey = 0;
        Key lastPressedKeyObject = keyProvider.getLastPressedKey();
        if (lastPressedKeyObject != null) {
            lastPressedKey = keyMapper.mapRealKey(lastPressedKeyObject);
        }
        return lastPressedKey;
    }

    @Override
    @NotNull
    public ReadOnlyCalculatorMemory getMemory() {
        return this.memory;
    }

    @Override
    @NotNull
    public NumberDisplayFormat getNumberDisplayFormat() {
        return this.numberDisplayFormat;
    }

    @Override
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
        return this.programStack;
    }

    @Override
    public void interpret(String input) {
        // Fix input without colon:
        String cleanedInput = StringUtils.prependIfMissing(input, ":");

        try {
            ExecutableProgram executableProgram = internalPreprocessCode("TMP", cleanedInput);
            run(executableProgram, new ControlflowLessTIBasicVisitor());
        } catch (PreprocessException e) {
            LOGGER.error("Preprocessing commands failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void loadProgram(String programName, CharSequence programCode) {
        checkArgument(ValidationUtil.isValidProgramName(programName), "Invalid program name: %s", programName);

        try {
            ExecutableProgram executableProgram = internalPreprocessCode(programName, programCode);
            getWritableMemory().storeProgram(programName, executableProgram);
        } catch (PreprocessException e) {
            LOGGER.error("Loading program {} failed", programName);
            throw e;
        }
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

        if (this.commandFunctionMap.get(commandName) != null) {
            throw new DuplicateCommandException("Command function is already registered: " + commandName);
        }

        if (command.getEnvironment() != null) {
            throw new DuplicateCommandException("New command instance is already registered in another environment");
        }

        command.setEnvironment(this);
        this.commandFunctionMap.put(commandName, command);

        LOGGER.trace("Registered new command function '{}'", commandName);
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

        if (this.commandStatementMap.get(commandName) != null) {
            throw new DuplicateCommandException("Command statement is already registered: " + commandName);
        }

        if (command.getEnvironment() != null) {
            throw new DuplicateCommandException("New command instance is already registered in another environment");
        }

        command.setEnvironment(this);
        this.commandStatementMap.put(commandName, command);

        LOGGER.trace("Registered new command statement '{}'", commandName);
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

        if (this.expressionFunction.get(commandName) != null) {
            throw new DuplicateCommandException("Function expression name is already registered: " + commandName);
        }

        if (command.getEnvironment() != null) {
            throw new DuplicateCommandException("New command instance is already registered in another environment");
        }

        command.setEnvironment(this);
        this.expressionFunction.put(commandName, command);

        LOGGER.trace("Registered new expression function '{}'", commandName);
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
        this.programStack.push(program);
        visitor.visit(program.getMainProgramContext());
        this.programStack.pop();
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
        Command command = this.commandFunctionMap.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException(-1, -1, commandName);
        }

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
        Command command = this.commandStatementMap.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException(-1, -1, commandName);
        }

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
        Command command = this.expressionFunction.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException(-1, -1, commandName);
        }

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

    @Override
    public void shutdown() {
        LOGGER.debug("Shutting down environment");
        try {
            display.close();
        } catch (IOException e) {
            LOGGER.error("Shutting down display failed", e);
            throw new TIGraphicsException("Shutting down display failed", e);
        }
        LOGGER.info("Environment shut down");
    }

    /**
     * Return a reference to the writable memory of this environment.
     *
     * @return A reference to the writable memory of this environment.
     */
    @NotNull
    protected CalculatorMemory getWritableMemory() {
        return this.memory;
    }

    @NotNull
    private Optional<Value> internalExecuteCommand(Command command, @NotNull Parameter[] arguments) {
        ImmutableList<Parameter> argumentList = ImmutableList.copyOf(arguments);
        command.checkArguments(argumentList);
        return command.execute(argumentList);
    }

    /**
     * Run all neccessary internal routines for preprocessing a given code.
     */
    private ExecutableProgram internalPreprocessCode(String programName, CharSequence programCode) {
        ExecutableProgram executableProgram;
        executableProgram = this.preprocessor.preprocessProgramCode(programName, programCode);
        return executableProgram;
    }
}
