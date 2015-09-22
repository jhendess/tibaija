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
import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.IllegalTypeException;
import org.xlrnet.tibaija.exception.TIArgumentException;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;
import org.xlrnet.tibaija.processor.Command;
import org.xlrnet.tibaija.util.ValueUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic implementation for arithmetic operations with two operands like + , - , *  and /. Uses a functional enum
 * pattern for instantiation.
 */
public class BinaryCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryCommand.class);

    /**
     * A function with two parameters that will be used to calculate the result of an operation
     */
    private final BinaryOperator<Value> evaluationFunction;

    private BinaryCommandOperator operator;

    public BinaryCommand(BinaryCommandOperator operator) {
        this(operator.getOperatorFunction());
        this.operator = operator;
    }

    protected BinaryCommand(BinaryOperator<Value> evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    @Override
    protected Optional<Value> execute(ImmutableList<Parameter> arguments) {
        final Value lhs = arguments.get(0).value();
        final Value rhs = arguments.get(1).value();
        final Value result = applyOperator(lhs, rhs);

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
    protected boolean hasValidArgumentValues(ImmutableList<Parameter> parameters) {
        final Value lhs = parameters.get(0).value();
        final Value rhs = parameters.get(1).value();
        checkNotNull(lhs);
        checkNotNull(rhs);

        if (operator == BinaryCommandOperator.PLUS && lhs.isString()) {
            // Type check for string concatenation
            if (!rhs.isString())
                throw new IllegalTypeException("Right hand side of concatenation expression is not a string: " + rhs.getValue(), Variables.VariableType.STRING, rhs.getType());
        } else {
            if (!ValueUtils.isNumberOrList(lhs))
                throw new IllegalTypeException("Left hand side of expression is not a list or number: " + lhs.getValue(), Variables.VariableType.NUMBER, lhs.getType());
            if (!ValueUtils.isNumberOrList(rhs))
                throw new IllegalTypeException("Right hand side of expression is not a list or number: " + rhs.getValue(), Variables.VariableType.NUMBER, rhs.getType());
        }

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
     * Apply the internal function if they both operands are lists and have the same length. Each i-th element of the
     * left list will be applied to the i-th element of the right list to build the i-th element of the result list.
     *
     * @param lhs
     *         Left side of the expression
     * @param rhs
     *         Right side of the expression.
     * @return A new Value object with the internal function applied to it.
     */
    @NotNull
    private Value applyOnBothList(@NotNull Value lhs, @NotNull Value rhs) {
        final ImmutableList<Complex> leftList = lhs.list();
        final ImmutableList<Complex> rightList = rhs.list();

        if (leftList.size() != rightList.size())
            throw new TIArgumentException("Mismatching dimensions: " + leftList.size() + " - " + rightList.size(), lhs, rhs);

        List<Complex> resultList = new ArrayList<>(leftList.size());
        for (int i = 0; i < leftList.size(); i++)
            resultList.add(evaluationFunction.apply(Value.of(leftList.get(i)), Value.of(rightList.get(i))).complex());
        return Value.of(resultList);
    }

    /**
     * Apply the internal function if the left operand is a list and the right is a number. Each i-th element of the
     * left list will be applied to the numerical right-side expression to build the i-th value of the resulting list.
     *
     * @param lhs
     *         Left side of the expression
     * @param rhs
     *         Right side of the expression.
     * @return A new Value object with the internal function applied to it.
     */
    @NotNull
    private Value applyOnLeftList(@NotNull Value lhs, @NotNull Value rhs) {
        List<Complex> valueList = lhs.list()
                .stream()
                .map(c -> evaluationFunction.apply(Value.of(c), rhs).complex())
                .collect(Collectors.toList());
        return Value.of(valueList);
    }

    /**
     * Apply the internal function if the right operand is a list and the left is a number. The numerical left-side
     * expression will be applied to each i-th element of the right list to build the i-th value of the resulting list.
     *
     * @param lhs
     *         Left side of the expression
     * @param rhs
     *         Right side of the expression.
     * @return A new Value object with the internal function applied to it.
     */
    @NotNull
    private Value applyOnRightList(@NotNull Value lhs, @NotNull Value rhs) {
        List<Complex> valueList = rhs.list()
                .stream()
                .map(c -> evaluationFunction.apply(lhs, Value.of(c)).complex())
                .collect(Collectors.toList());
        return Value.of(valueList);
    }

    /**
     * Apply the internal operator function on the given operand. If both operand are numbers, the function will be
     * applied to both numerical values. If both operands are lists and have the same length, each i-th element of the
     * left list will be applied with the i-th element of the right list to build the i-th element of the result.  If
     * both lists have a different length an exception will be thrown. If only one side of the expression is a list,
     * the non-list side will be applied to each element of the list side.
     *
     * @param lhs
     *         Left side of the expression
     * @param rhs
     *         Right side of the expression.
     * @return A new Value object with the internal function applied to it.
     */
    private Value applyOperator(Value lhs, Value rhs) throws TIArgumentException {
        Value result;

        if (lhs.isList() || rhs.isList()) {
            if (lhs.isList() && rhs.isList()) {
                result = applyOnBothList(lhs, rhs);
            } else if (lhs.isList()) {
                result = applyOnLeftList(lhs, rhs);
            } else {
                result = applyOnRightList(lhs, rhs);
            }
        } else {
            result = evaluationFunction.apply(lhs, rhs);
        }
        return result;
    }

}
