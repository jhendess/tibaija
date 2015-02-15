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

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.complex.Complex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.antlr.TIBasicBaseVisitor;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.exception.IllegalControlFlowException;
import org.xlrnet.tibaija.memory.Value;
import org.xlrnet.tibaija.memory.Variables;
import org.xlrnet.tibaija.util.ContextUtils;
import org.xlrnet.tibaija.util.TIMathUtils;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Full visitor for the TI-Basic language. This class implements all
 */
public class FullTIBasicVisitor extends TIBasicBaseVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullTIBasicVisitor.class);

    ExecutionEnvironment environment;

    /**
     * Sets the internal execution environment.
     *
     * @param environment
     *         The new execution environment.
     */
    final public void setEnvironment(ExecutionEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public Object visitCallStatement(@NotNull TIBasicParser.CallStatementContext ctx) {
        return super.visitCallStatement(ctx);
    }

    @Override
    public Object visitCommand(@NotNull TIBasicParser.CommandContext ctx) {
        Object result = null;
        if (ctx.expressionParent() != null) {
            result = ctx.expressionParent().accept(this);
        } else if (ctx.statement() != null) {
            result = ctx.statement().accept(this);
        }

        if (result instanceof Optional) {
            Value lastResult = (Value) ((Optional) result).get();
            environment.getWritableMemory().setLastResult(lastResult);
        } else if (result != null) {
            LOGGER.debug("Command returned object of type {} with value {}", result.getClass().getSimpleName(), result);
        }
        return super.visitCommand(ctx);
    }

    @Override
    public Object visitCommandList(@NotNull TIBasicParser.CommandListContext ctx) {
        final List<TIBasicParser.CommandContext> commandList = ctx.command();
        final int commandListSize = commandList.size();

        Stack<ControlFlowElement> flowElementStack = new Stack<>();
        Stack<ControlFlowElement.ControlFlowToken> skipCommandsStack = new Stack<>();

        for (int commandCounter = 0; commandCounter < commandListSize; commandCounter++) {
            final TIBasicParser.CommandContext nextCommand = commandList.get(commandCounter);

            // Skipping logic
            if (!skipCommandsStack.empty()) {
                if (nextCommand.isControlFlowStatement) {
                    commandCounter = internalHandleSkipFlowLogic(commandCounter, commandList, skipCommandsStack, nextCommand);
                } else {
                    LOGGER.debug("Skipping command {}", commandCounter);
                }
            } else if (nextCommand.isControlFlowStatement) {
                commandCounter = internalHandleControlFlowLogic(commandCounter, commandList, flowElementStack, skipCommandsStack, nextCommand);
            } else {
                nextCommand.accept(this);
            }
        }
        return null;
    }

    @Override
    public Object visitControlFlowStatement(@NotNull TIBasicParser.ControlFlowStatementContext ctx) {
        return super.visitControlFlowStatement(ctx);
    }

    @Override
    public Object visitElseStatement(@NotNull TIBasicParser.ElseStatementContext ctx) {
        int line = ctx.ELSE().getSymbol().getLine();
        int startIndex = ctx.ELSE().getSymbol().getStartIndex();
        return new ControlFlowElement(line, startIndex, ControlFlowElement.ControlFlowToken.ELSE, false, false);
    }

    @Override
    public Object visitEndStatement(@NotNull TIBasicParser.EndStatementContext ctx) {
        int line = ctx.END().getSymbol().getLine();
        int startIndex = ctx.END().getSymbol().getStartIndex();
        return new ControlFlowElement(line, startIndex, ControlFlowElement.ControlFlowToken.END, false, false);
    }

    @Override
    public Object visitExpression(@NotNull TIBasicParser.ExpressionContext ctx) {
        // Nothing to do here -> just return the value ...
        return super.visitExpression(ctx);
    }

    @Override
    public Optional<Value> visitExpressionParent(@NotNull TIBasicParser.ExpressionParentContext ctx) {
        return Optional.of((Value) ctx.expression().accept(this));
    }

    @Override
    public Object visitExpression_and(@NotNull TIBasicParser.Expression_andContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_compare();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_compare(@NotNull TIBasicParser.Expression_compareContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_plus_minus();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Object visitExpression_conv(@NotNull TIBasicParser.Expression_convContext ctx) {
        // TODO: Implement conversion (or some kind of a flag?)
        return super.visitExpression_conv(ctx);
    }

    @Override
    public Value visitExpression_infix(@NotNull TIBasicParser.Expression_infixContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_negation();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_mul_div(@NotNull TIBasicParser.Expression_mul_divContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_infix();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_negation(@NotNull TIBasicParser.Expression_negationContext ctx) {
        Value lhs = (Value) ctx.expression_power_root().accept(this);
        if (ctx.NEGATIVE_MINUS() == null)
            return lhs;                 // Return left hand side if no negation is wanted
        Value rhs = Value.NEGATIVE_ONE;
        return environment.runRegisteredCommand("*", lhs, rhs).get();
    }

    @Override
    public Value visitExpression_or(@NotNull TIBasicParser.Expression_orContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_and();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_plus_minus(@NotNull TIBasicParser.Expression_plus_minusContext ctx) {
        List<String> operators = ctx.operators;
        List<? extends RuleContext> contextRules = ctx.expression_mul_div();
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_postfix(@NotNull TIBasicParser.Expression_postfixContext ctx) {
        List<String> operators = ctx.operators;

        if (ctx.expression_preeval() != null) {
            // Run regular right-associative postfix logic without imaginary parts
            Value expressionValue = (Value) ctx.expression_preeval().accept(this);
            for (String op : operators)
                expressionValue = environment.runRegisteredCommand(op, expressionValue).get();
            return expressionValue;
        } else {
            // Run imaginary logic -> e.g. ii²² == i(i²)²
            int imaginaryCount = ctx.IMAGINARY().size() - 1;
            Value lhs = Value.of(Complex.I);
            LOGGER.debug("(IMAGINARY) -> {}", lhs.complex());
            for (String op : operators) {
                if (imaginaryCount >= 0) {
                    lhs = environment.runRegisteredCommand(op, lhs).get();
                    if (imaginaryCount > 0) {
                        Value before = lhs;
                        lhs = Value.of(lhs.complex().multiply(Complex.I));
                        LOGGER.debug("(MULTIPLY) {} (IMAGINARY) -> {}", before.complex(), lhs.complex());
                        imaginaryCount--;
                    }
                }
            }
            // Multiply value with all left I
            if (imaginaryCount > 0) {
                final Complex factor = TIMathUtils.imaginaryNthPower(imaginaryCount);
                final Value before = lhs;
                lhs = Value.of(lhs.complex().multiply(factor));
                LOGGER.debug("(MULTIPLY) {} {} -> {}", before.complex(), factor, lhs.complex());
            }

            return lhs;
        }
    }

    @Override
    public Value visitExpression_power_root(@NotNull TIBasicParser.Expression_power_rootContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_postfix();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Value visitExpression_prefix(@NotNull TIBasicParser.Expression_prefixContext ctx) {
        Value lhs = (Value) ctx.expression_xor().accept(this);
        if (ctx.operator != null)
            return environment.runRegisteredCommand(ctx.operator, lhs).get();
        return lhs;
    }

    @Override
    public Value visitExpression_value(@NotNull TIBasicParser.Expression_valueContext ctx) {
        if (ctx.expression() != null)
            return (Value) ctx.expression().accept(this);     // Expression with parentheses has more than 1 child
        else
            return (Value) ctx.getChild(0).accept(this);      // All other rules have only one child
    }

    @Override
    public Value visitExpression_xor(@NotNull TIBasicParser.Expression_xorContext ctx) {
        List<? extends RuleContext> contextRules = ctx.expression_or();
        List<String> operators = ctx.operators;
        return processGenericExpressions(operators, contextRules);
    }

    @Override
    public Object visitForStatement(@NotNull TIBasicParser.ForStatementContext ctx) {
        return super.visitForStatement(ctx);
    }

    @Override
    public Object visitGotoStatement(@NotNull TIBasicParser.GotoStatementContext ctx) {
        return super.visitGotoStatement(ctx);
    }

    @Override
    public ControlFlowElement visitIfStatement(@NotNull TIBasicParser.IfStatementContext ctx) {
        final int line = ctx.IF().getSymbol().getLine();
        final int startIndex = ctx.IF().getSymbol().getStartIndex();

        Value value = (Value) ctx.expression().accept(this);
        boolean lastEvaluation = value.bool();

        return new ControlFlowElement(line, startIndex, ControlFlowElement.ControlFlowToken.IF, lastEvaluation, false);
    }

    @Override
    public Object visitLabelIdentifier(@NotNull TIBasicParser.LabelIdentifierContext ctx) {
        return super.visitLabelIdentifier(ctx);
    }

    @Override
    public Object visitLabelStatement(@NotNull TIBasicParser.LabelStatementContext ctx) {
        return super.visitLabelStatement(ctx);
    }

    @Override
    public Value visitLastResult(@NotNull TIBasicParser.LastResultContext ctx) {
        return environment.getMemory().getLastResult();
    }

    @Override
    public Object visitListExpression(@NotNull TIBasicParser.ListExpressionContext ctx) {
        List<TIBasicParser.ExpressionContext> expressions = ctx.expression();
        List<Complex> evaluatedExpressions = expressions.stream()
                .map(expression -> ((Value) expression.accept(this)).complex())
                .collect(Collectors.toList());
        return Value.of(evaluatedExpressions);
    }

    @Override
    public Value visitListValue(@NotNull TIBasicParser.ListValueContext ctx) {
        if (ctx.listVariable() != null) {
            final String listVariableName = ctx.listVariable().listIdentifier().getText();
            return environment.getMemory().getListVariableValue(listVariableName);
        } else if (ctx.listExpression() != null) {
            return (Value) ctx.listExpression().accept(this);
        }
        throw new UnsupportedOperationException("This shouldn't happen");
    }

    @Override
    public Value visitNumber(@NotNull TIBasicParser.NumberContext ctx) {
        return ContextUtils.extractValueFromNumberContext(ctx);
    }

    @Override
    public Value visitNumericalValue(@NotNull TIBasicParser.NumericalValueContext ctx) {
        return (Value) ctx.getChild(0).accept(this);        // Automatically pass-through the next value
    }

    @Override
    public Value visitNumericalVariable(@NotNull TIBasicParser.NumericalVariableContext ctx) {
        String variableName = ctx.getText();
        Variables.NumberVariable variable = Variables.resolveNumberVariable(variableName);
        return environment.getMemory().getNumberVariableValue(variable);
    }

    @Override
    public Object visitProgram(@NotNull TIBasicParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    @Override
    public Object visitRepeatStatement(@NotNull TIBasicParser.RepeatStatementContext ctx) {
        return super.visitRepeatStatement(ctx);
    }

    @Override
    public Optional<Value> visitStatement(@NotNull TIBasicParser.StatementContext ctx) {
        return Optional.ofNullable((Value) super.visitStatement(ctx));
    }

    @Override
    public Value visitStoreListStatement(@NotNull TIBasicParser.StoreListStatementContext ctx) {
        String variableName = ctx.listVariable().listIdentifier().getText();
        Value value = (Value) ctx.expression().accept(this);

        environment.getWritableMemory().setListVariableValue(variableName, value);
        return value;
    }

    @Override
    public Value visitStoreNumberStatement(@NotNull TIBasicParser.StoreNumberStatementContext ctx) {
        String variableName = ctx.numericalVariable().getText();
        Value value = (Value) ctx.expression().accept(this);

        Variables.NumberVariable targetVariable = Variables.resolveNumberVariable(variableName);
        environment.getWritableMemory().setNumberVariableValue(targetVariable, value);

        return value;
    }

    @Override
    public ControlFlowElement visitThenStatement(@NotNull TIBasicParser.ThenStatementContext ctx) {
        int line = ctx.THEN().getSymbol().getLine();
        int startIndex = ctx.THEN().getSymbol().getStartIndex();
        return new ControlFlowElement(line, startIndex, ControlFlowElement.ControlFlowToken.THEN, false, false);
    }

    @Override
    public Object visitWhileStatement(@NotNull TIBasicParser.WhileStatementContext ctx) {
        return super.visitWhileStatement(ctx);
    }

    private int internalHandleControlFlowLogic(int currentCommandCount, List<TIBasicParser.CommandContext> commandList, Stack<ControlFlowElement> flowElementStack, Stack<ControlFlowElement.ControlFlowToken> skipCommandsStack, TIBasicParser.CommandContext nextCommand) {
        int commandListSize = commandList.size();
        ControlFlowElement currentFlowElement = (ControlFlowElement) nextCommand.accept(this);
        ControlFlowElement topFlowElement;
        if (!flowElementStack.empty()) {
            topFlowElement = flowElementStack.peek();
        } else {
            topFlowElement = null;
        }

        // Check if current token depends on a certain pre-token
        final int line = currentFlowElement.getLine();
        final int charIndex = currentFlowElement.getCharIndex();
        switch (currentFlowElement.getToken()) {
            case GOTO:
            case LABEL:
                throw new NotImplementedException(currentFlowElement.getToken() + " is not yet implemented");
            case THEN:
                if (topFlowElement == null) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'Then' Statement");
                }
                if (topFlowElement.getToken() != ControlFlowElement.ControlFlowToken.IF) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'Then' Statement without preceding 'If'");
                }
                if (topFlowElement.getLastEvaluation()) {
                    currentFlowElement.setLastEvaluation(true);
                } else {
                    currentFlowElement.setLastEvaluation(false);
                    skipCommandsStack.push(ControlFlowElement.ControlFlowToken.ELSE);
                    LOGGER.debug("Skipping commands until next ELSE from command {}", currentCommandCount);
                }
                flowElementStack.push(currentFlowElement);
                break;
            case ELSE:
                if (topFlowElement == null) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'Else' Statement");
                }
                if (topFlowElement.getToken() != ControlFlowElement.ControlFlowToken.THEN) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'Else' Statement without preceding 'Then'");
                }
                if (topFlowElement.getLastEvaluation()) {        // Skip until next "END" if previous if was true
                    skipCommandsStack.push(ControlFlowElement.ControlFlowToken.END);
                    LOGGER.debug("Skipping commands until next END from command {}", currentCommandCount);
                }
                break;
            case END:
                if (topFlowElement == null) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'End' Statement without preceding endable element");
                }
                if (topFlowElement.isRepeatable()) {
                    currentCommandCount = topFlowElement.getCommandIndex();          // Move counter backwards
                    LOGGER.debug("Moving command counter to index {}", currentCommandCount);
                } else {
                    flowElementStack.pop();
                }
                break;
            case IF:
                // Look ahead if the next command might be a "Then" i.e. if it is a controlflow statement
                if (commandListSize <= currentCommandCount + 1) {
                    throw new IllegalControlFlowException(line, charIndex, "Illegal 'If' at the end of the program");
                } else if (commandList.get(currentCommandCount + 1).isControlFlowStatement) {
                    flowElementStack.push(currentFlowElement);
                    LOGGER.debug("Predicted multiline IF at command {}", currentCommandCount);
                } else if (!currentFlowElement.getLastEvaluation()) {
                    // If the next command is not a flow statement and the If evaluated to false, skip the next command (i.e. no else allowed!)
                    currentCommandCount++;
                    LOGGER.debug("Skipped IF statement without ELSE clause at command {}", currentCommandCount);
                }
                break;
            default:
                throw new NotImplementedException("Flow not implemented");
        }
        return currentCommandCount;
    }

    private int internalHandleSkipFlowLogic(int currentCommandCounter, List<TIBasicParser.CommandContext> commandList, Stack<ControlFlowElement.ControlFlowToken> skipCommandsStack, TIBasicParser.CommandContext nextCommand) {
        int commandListSize = commandList.size();
        final String enumName = nextCommand.controlFlowStatement().flowType;
        ControlFlowElement.ControlFlowToken currentFlowToken = EnumUtils.getEnum(ControlFlowElement.ControlFlowToken.class, enumName);
        ControlFlowElement.ControlFlowToken topToken = skipCommandsStack.peek();

        if (currentFlowToken == null) {
            throw new IllegalStateException("Internal error: control flow token is null at command " + currentCommandCounter);
        }

        switch (currentFlowToken) {
            case IF:
                // Look ahead if the next command might be a "Then" i.e. if it is a controlflow statement
                if (commandListSize <= currentCommandCounter + 1) {
                    throw new IllegalControlFlowException(-1, -1, "Illegal 'If' at the end of the program");
                } else if (commandList.get(currentCommandCounter + 1).isControlFlowStatement) {
                    skipCommandsStack.push(currentFlowToken);
                    LOGGER.debug("Predicted multiline IF while skipping over command {}", currentCommandCounter);
                } else {
                    LOGGER.debug("Skipping over single line IF at command {}", currentCommandCounter);
                    currentCommandCounter++;
                }
                break;
            case THEN:
                if (topToken != ControlFlowElement.ControlFlowToken.IF)
                    throw new IllegalControlFlowException(-1, -1, "Illegal 'Then' Statement without preceding 'If'");
                skipCommandsStack.pop();
                skipCommandsStack.push(currentFlowToken);
                break;
            case ELSE:
                if (skipCommandsStack.size() > 1 && topToken != ControlFlowElement.ControlFlowToken.THEN)
                    throw new IllegalControlFlowException(-1, -1, "Illegal 'Else' Statement without preceding 'Then' ");
                skipCommandsStack.pop();
                if (!skipCommandsStack.empty())
                    skipCommandsStack.push(topToken);
                break;
            case END:
                skipCommandsStack.pop();
                break;
            default:
                throw new IllegalStateException("Illegal flow token: " + currentFlowToken);
        }
        if (skipCommandsStack.empty()) {
            LOGGER.debug("Skip stack is now empty - continuing execution at command {}", currentCommandCounter + 1);
        }
        return currentCommandCounter;
    }

    /**
     * Internal function for processing expressions. This method takes an initial value and both a list of operators
     * and  a list of operands. Each i-th element in the operator list will be applied to the i-1-th and i-th element
     * in the operand list. Each operator will be looked up in the current environment's command list.
     * If there is only one operand, the left hand side will be returned.
     * <p/>
     * E.g.: operator = ['+','-'] and operands = [1,2,3] will result in 1 + 2 - 3
     *
     * @param operators
     *         List of operators to be applied. Note: the first operator will be applied to the first and second
     *         operand
     *         (see above)
     * @param contextRules
     *         The child contexts that will be invoked by the visitor. Each context must return an object of type
     *         {@link
     *         Value}.
     */
    @NotNull
    private Value processGenericExpressions(@NotNull List<String> operators, @NotNull List<? extends RuleContext> contextRules) {
        Value lhs = (Value) contextRules.get(0).accept(this);
        for (int i = 1; i < contextRules.size(); i++) {
            Value rhs = (Value) contextRules.get(i).accept(this);
            lhs = environment.runRegisteredCommand(operators.get(i - 1), lhs, rhs).get();
        }
        return lhs;
    }

}
