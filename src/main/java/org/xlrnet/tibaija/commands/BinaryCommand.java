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
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;
import org.xlrnet.tibaija.processor.Command;
import org.xlrnet.tibaija.util.CompareUtils;

import java.util.Optional;
import java.util.function.BinaryOperator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic implementation for arithmetic operations with two operands like + , - , *  and /. Uses a functional pattern
 * for combined with static factory methods. Do not instantiate directy. Use the provided static factory methods.
 */
public class BinaryCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryCommand.class);

    /**
     * A function with two parameters that will be used to calculate the result of an operation
     */
    private final BinaryOperator<Value> evaluationFunction;

    private Operator operator;

    public BinaryCommand(Operator operator) {
        this(operator.getOperatorFunction());
        this.operator = operator;
    }

    protected BinaryCommand(BinaryOperator<Value> evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    @Override
    protected Optional<Value> execute(ImmutableList<Value> arguments) {
        final Value lhs = arguments.get(0);
        final Value rhs = arguments.get(1);
        final Value result = evaluationFunction.apply(lhs, rhs);

        LOGGER.debug("({}) {} {} -> {}", operator, lhs.getValue(), rhs.getValue(), result.getValue());

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
        final Value lhs = parameters.get(0);
        final Value rhs = parameters.get(1);
        checkNotNull(lhs);
        checkNotNull(rhs);

        if (!lhs.isNumber())
            throw new IllegalTypeException("Left hand side of expression is not a Number: " + lhs.getValue(), Variables.VariableType.NUMBER, lhs.getType());
        if (!rhs.isNumber())
            throw new IllegalTypeException("Right hand side of expression is not a Number: " + rhs.getValue(), Variables.VariableType.NUMBER, rhs.getType());

        return true;
    }

    /**
     * Checks if exactly two arguments were passed.
     *
     * @param numberOfParametersEntered
     *         Number of parameters passed by the caller.
     * @return True if exactly two arguments were passed.
     */
    @Override
    protected boolean hasValidNumberOfArguments(int numberOfParametersEntered) {
        return numberOfParametersEntered == 2;
    }

    /**
     * Static enum that defines several functions for evaluating arithmetic operations with two operands.
     */
    public static enum Operator {

        PLUS((lhs, rhs) -> Value.of(lhs.complex().add(rhs.complex()))),

        MINUS((lhs, rhs) -> Value.of(lhs.complex().subtract(rhs.complex()))),

        MULTIPLY((lhs, rhs) -> Value.of(lhs.complex().multiply(rhs.complex()))),

        DIVIDE((lhs, rhs) -> Value.of(lhs.complex().divide(rhs.complex()))),

        POWER((lhs, rhs) -> Value.of(lhs.complex().pow(rhs.complex()))),

        EQUALS((lhs, rhs) -> Value.of(CompareUtils.isEqual(lhs, rhs))),

        NOT_EQUALS((lhs, rhs) -> Value.of(CompareUtils.isNotEqual(lhs, rhs))),

        GREATER_THAN((lhs, rhs) -> {
            checkIfAnyComplexIsImaginary(lhs, rhs);
            return Value.of(CompareUtils.isGreaterThan(lhs, rhs));
        }),

        LESS_THAN((lhs, rhs) -> {
            checkIfAnyComplexIsImaginary(lhs, rhs);
            return Value.of(CompareUtils.isLessThan(lhs, rhs));
        }),

        GREATER_EQUALS((lhs, rhs) -> {
            checkIfAnyComplexIsImaginary(lhs, rhs);
            return Value.of(CompareUtils.isGreaterOrEqual(lhs, rhs));
        }),

        LESS_EQUALS((lhs, rhs) -> {
            checkIfAnyComplexIsImaginary(lhs, rhs);
            return Value.of(CompareUtils.isLessOrEqual(lhs, rhs));
        });

        private final BinaryOperator<Value> operatorFunction;

        Operator(BinaryOperator<Value> operatorFunction) {
            this.operatorFunction = operatorFunction;
        }

        private static void checkIfAnyComplexIsImaginary(Value lhs, Value rhs) {
            if (lhs.hasImaginaryValue() || rhs.hasImaginaryValue())
                throw new TIArgumentException("Cannot compare two imaginary values", ImmutableList.of(lhs, rhs));
        }

        public BinaryOperator<Value> getOperatorFunction() {
            return operatorFunction;
        }
    }
}
