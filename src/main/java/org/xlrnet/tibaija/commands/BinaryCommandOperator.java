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

import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.util.CompareUtils;
import org.xlrnet.tibaija.util.LogicUtils;
import org.xlrnet.tibaija.util.TIMathUtils;

import java.util.function.BinaryOperator;

import static org.xlrnet.tibaija.util.ValueUtils.checkIfAnyValueIsImaginary;

/**
 * Static enum that defines several functions for evaluating arithmetic operations with two operands.
 */
public enum BinaryCommandOperator {

    PLUS((lhs, rhs) -> {
        if (lhs.isString() && rhs.isString())
            return Value.of(lhs.string().concat(rhs.string()));
        return Value.of(lhs.complex().add(rhs.complex()));
    }),

    MINUS((lhs, rhs) -> Value.of(lhs.complex().subtract(rhs.complex()))),

    MULTIPLY((lhs, rhs) -> Value.of(lhs.complex().multiply(rhs.complex()))),

    DIVIDE((lhs, rhs) -> Value.of(lhs.complex().divide(rhs.complex()))),

    POWER((lhs, rhs) -> Value.of(lhs.complex().pow(rhs.complex()))),

    NTH_ROOT((lhs, rhs) -> Value.of(TIMathUtils.complexNthRoot(lhs.complex(), rhs.complex()))),

    EQUALS((lhs, rhs) -> Value.of(CompareUtils.isEqual(lhs, rhs))),

    NOT_EQUALS((lhs, rhs) -> Value.of(CompareUtils.isNotEqual(lhs, rhs))),

    GREATER_THAN((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(CompareUtils.isGreaterThan(lhs, rhs));
    }),

    LESS_THAN((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(CompareUtils.isLessThan(lhs, rhs));
    }),

    GREATER_EQUALS((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(CompareUtils.isGreaterOrEqual(lhs, rhs));
    }),

    LESS_EQUALS((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(CompareUtils.isLessOrEqual(lhs, rhs));
    }),

    AND((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(LogicUtils.and(lhs.complex().getReal(), rhs.complex().getReal()));
    }),

    OR((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(LogicUtils.or(lhs.complex().getReal(), rhs.complex().getReal()));
    }),

    XOR((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(LogicUtils.xor(lhs.complex().getReal(), rhs.complex().getReal()));
    }),

    NPR((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(TIMathUtils.numberOfPermutations(lhs.complex().getReal(), rhs.complex().getReal()));
    }),

    NCR((lhs, rhs) -> {
        checkIfAnyValueIsImaginary(lhs, rhs);
        return Value.of(TIMathUtils.numberOfCombinations(lhs.complex().getReal(), rhs.complex().getReal()));
    });

    private final BinaryOperator<Value> operatorFunction;

    BinaryCommandOperator(BinaryOperator<Value> operatorFunction) {
        this.operatorFunction = operatorFunction;
    }

    public BinaryOperator<Value> getOperatorFunction() {
        return operatorFunction;
    }
}
