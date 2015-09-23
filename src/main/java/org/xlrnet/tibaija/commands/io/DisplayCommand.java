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

package org.xlrnet.tibaija.commands.io;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.processor.Command;

import java.util.Optional;

/**
 * Command for displaying content on the home screen.
 */
public class DisplayCommand extends Command {

    /**
     * Main method for invoking a command. Must be overwritten by the specific implementation. When this method gets
     * called by the framework, both hasValidArgumentValues() and hasValidNumberOfArguments() have already been called.
     *
     * @param arguments
     *         The arguments for the command.
     * @return An optional return value.
     */
    @NotNull
    @Override
    protected Optional<Value> execute(@NotNull ImmutableList<Parameter> arguments) {
        for (Parameter argument : arguments) {
            getEnvironment().getHomeScreen().printValue(argument.value());
        }

        return Optional.empty();
    }

    /**
     * <b>Can be overwritten</b> by the concrete commands if something must be checked.
     * Checks if the number of parameters is in range.
     *
     * @param numberOfParametersEntered
     *         Number of parameters passed by the caller. Value is never below zero.
     * @return True if the number of parameters is within the expected range, otherwise false.
     */
    @Override
    protected boolean hasValidNumberOfArguments(int numberOfParametersEntered) {
        return numberOfParametersEntered > 0;
    }
}
