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
import org.xlrnet.tibaija.util.TIMathUtils;

import java.util.function.UnaryOperator;

import static org.xlrnet.tibaija.util.LogicUtils.not;
import static org.xlrnet.tibaija.util.ValueUtils.checkIfAnyValueIsImaginary;

/**
 * Static enum that defines several functions for evaluating arithmetic operations with one operand.
 */
public enum UnaryCommandOperator {

    NOT((Value operand) -> {
        checkIfAnyValueIsImaginary(operand);
        return Value.of(not(operand.complex().getReal()));
    }),

    SQUARED(operand -> Value.of(operand.complex().multiply(operand.complex()))),

    CUBED(operand -> Value.of(operand.complex().multiply(operand.complex()).multiply(operand.complex()))),

    SQUARE_ROOT(operand -> Value.of(operand.complex().sqrt())),

    CUBIC_ROOT(operand -> Value.of(operand.complex().nthRoot(3).get(0))),

    FACTORIAL(operand -> {
        checkIfAnyValueIsImaginary(operand);
        return Value.of(TIMathUtils.factorial(operand.complex().getReal()));
    });

    private final UnaryOperator<Value> operatorFunction;

    UnaryCommandOperator(UnaryOperator<Value> operatorFunction) {
        this.operatorFunction = operatorFunction;
    }

    public UnaryOperator<Value> getOperatorFunction() {
        return operatorFunction;
    }

}
