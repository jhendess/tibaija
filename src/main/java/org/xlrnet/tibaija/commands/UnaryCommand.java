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

package org.xlrnet.tibaija.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;
import org.xlrnet.tibaija.processor.Command;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.xlrnet.tibaija.util.LogicUtils.not;
import static org.xlrnet.tibaija.util.ValueUtils.checkIfAnyValueIsImaginary;

/**
 * Generic implementation for arithmetic operations with one operand like √(, ², ! or "not(". Uses a functional enum
 * pattern
 * for instantiation.
 */
public class UnaryCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryCommand.class);

    /**
     * A function with two parameters that will be used to calculate the result of an operation
     */
    private final UnaryOperator<Value> evaluationFunction;

    private UnaryCommandOperator operator;

    public UnaryCommand(UnaryCommandOperator operator) {
        this(operator.getOperatorFunction());
        this.operator = operator;
    }

    protected UnaryCommand(UnaryOperator<Value> evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    @Override
    protected Optional<Value> execute(ImmutableList<Value> arguments) {
        final Value operand = arguments.get(0);
        final Value result = evaluationFunction.apply(operand);

        LOGGER.debug("({}) {} -> {}", operator, operand.getValue(), result.getValue());

        return Optional.of(result);
    }

    /**
     * Check if both arguments are of a numerical type and not null.
     *
     * @param parameters
     *         The arguments for the command.
     * @return True if both arguments are of a numerical type and not null.
     */
    @Override
    protected boolean hasValidArgumentValues(ImmutableList<Value> parameters) {
        final Value operand = parameters.get(0);
        checkNotNull(operand);

        if (!operand.isNumber())
            throw new IllegalTypeException("Operand is not a Number: " + operand.getValue(), Variables.VariableType.NUMBER, operand.getType());

        return true;
    }

    /**
     * Check if exactly one argument was passed.
     *
     * @param numberOfParametersEntered
     *         Number of parameters passed by the caller.
     * @return True if exactly two arguments were passed.
     */
    @Override
    protected boolean hasValidNumberOfArguments(int numberOfParametersEntered) {
        return numberOfParametersEntered == 1;
    }

    public static enum UnaryCommandOperator {

        NOT((Value operand) -> {
            checkIfAnyValueIsImaginary(operand);
            return Value.of(not(operand.complex().getReal()));
        });

        private final UnaryOperator<Value> operatorFunction;

        UnaryCommandOperator(UnaryOperator<Value> operatorFunction) {
            this.operatorFunction = operatorFunction;
        }

        public UnaryOperator<Value> getOperatorFunction() {
            return operatorFunction;
        }

    }

}
