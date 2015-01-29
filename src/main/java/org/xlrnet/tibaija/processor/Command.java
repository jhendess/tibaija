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
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.memory.Value;

import java.util.Optional;


/**
 * Abstract class for all commands. Derive your own commands from this class and implement at least execute(). You
 * should also implement hasValidNumberOfArguments() and hasValidArgumentValues() to simplify development.
 * Make sure to implement all derived class stateless to avoid side effects.
 */
public abstract class Command {

    private ExecutionEnvironment environment;

    public ExecutionEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(ExecutionEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Checks the passed arguments for correctness. This method should be called only by the framework.
     *
     * @param arguments
     *         The arguments to check
     * @return True if number and values of the arguments are correct. Execute() may use the arguments afterwards
     * unchecked. <br> False if the number is below or above excepted range or if any value is incorrect.
     * @throws TIArgumentException
     *         Will be thrown if either the count or the value of any argument is invalid.
     */
    protected final boolean checkArguments(ImmutableList<Value> arguments) throws TIArgumentException {
        if (!hasValidNumberOfArguments(arguments.size())) {
            throw new TIArgumentException(-1, -1, "Invalid argument count", arguments);
        }
        if (!hasValidArgumentValues(arguments)) {
            throw new TIArgumentException(-1, -1, "Invalid argument value", arguments);
        }

        return true;
    }

    /**
     * Main method for invoking a command. Must be overwritten by the specific implementation. When this method gets
     * called by the framework, both hasValidArgumentValues() and hasValidNumberOfArguments() have already been called.
     *
     * @param arguments
     *         The arguments for the command.
     * @return An optional return value.
     */
    protected abstract Optional<Value> execute(ImmutableList<Value> arguments);

    /**
     * Can be overwritten by the concrete commands if at least the value of one parameter must be checked.
     * Checks all values of all passed parameters.
     * An explaining error message must be output by the concrete command.<br>
     *
     * @param parameters
     *         The immutable list of parameters to check.
     * @return True if all values of all passed parameters are correct. False if at least one value of one parameter is
     * incorrect.
     */
    protected boolean hasValidArgumentValues(ImmutableList<Value> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("parameters are null");
        }
        return true;
    }

    /**
     * <b>Can be overwritten</b> by the concrete commands if something must be checked.
     * Checks if the number of parameters is in range.
     *
     * @param numberOfParametersEntered
     *         Number of parameters passed by the caller.
     * @return True if the number of parameters is within the expected range, otherwise false.
     */
    protected boolean hasValidNumberOfArguments(int numberOfParametersEntered) {
        return true;
    }

}
