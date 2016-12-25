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
import org.xlrnet.tibaija.commons.NumberUtil;
import org.xlrnet.tibaija.commons.Preconditions;
import org.xlrnet.tibaija.commons.Value;
import org.xlrnet.tibaija.commons.ValueType;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.processor.Command;

import java.util.Optional;

import static org.xlrnet.tibaija.commons.Preconditions.checkArgument;

/**
 * Command for outputting content at specific coordinates on the home screen.
 * <p/>
 * According to TI-Basic Developer {@see http://tibasicdev.wikidot.com/output}:
 * The Output( command is the fastest way to display text on the home screen. It takes three arguments: the row (1-8)
 * at
 * which you want to display something, the column (1-16), and whatever it is you want to display. It allows for more
 * freedom than the Disp command.
 */
public class OutputCommand extends Command {

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
        double rowIndex = arguments.get(0).value().realPart();
        double columnIndex = arguments.get(1).value().realPart();
        Value printValue = arguments.get(2).value();

        getEnvironment().getHomeScreen().printAt(printValue, (int) columnIndex, (int) rowIndex);

        return Optional.empty();
    }

    /**
     * Can be overwritten by the concrete commands if at least the value of one parameter must be checked.
     * Checks all values of all passed parameters.
     * An explaining error message must be output by the concrete command.<br>
     *
     * @param arguments
     *         The immutable list of parameters to check.
     * @return True if all values of all passed parameters are correct. False if at least one value of one parameter is
     * incorrect.
     */
    @Override
    protected boolean hasValidArgumentValues(@NotNull ImmutableList<Parameter> arguments) {
        boolean valid = true;
        Value rowIndex = arguments.get(0).value();
        Value columnIndex = arguments.get(1).value();

        Preconditions.checkValueType(rowIndex, ValueType.NUMBER);
        Preconditions.checkValueType(columnIndex, ValueType.NUMBER);

        checkArgument(!rowIndex.hasImaginaryValue(), "Row index may not be imaginary");
        checkArgument(!columnIndex.hasImaginaryValue(), "Column index may not be imaginary");

        // Check for value bounds on row index
        if (!NumberUtil.isInteger(rowIndex.realPart()) || rowIndex.realPart() < 0 || rowIndex.realPart() > getEnvironment().getHomeScreen().getMaxRows()) {
            valid = false;
        }

        // Check for value bounds on column index
        if (!NumberUtil.isInteger(columnIndex.realPart()) || columnIndex.realPart() < 0 || columnIndex.realPart() > getEnvironment().getHomeScreen().getMaxColumns()) {
            valid = false;
        }

        return valid;
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
        return numberOfParametersEntered == 3;
    }
}
