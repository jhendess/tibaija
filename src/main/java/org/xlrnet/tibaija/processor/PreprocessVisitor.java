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

import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.antlr.TIBasicBaseVisitor;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.exception.PreprocessException;
import org.xlrnet.tibaija.util.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The visitor implementation for the preprocessor for TI-Basic programs. Its main task is to validate the syntax of a
 * given program and mark all labels inside the program, so that an {@link ExecutableProgram} can be built.
 */
public class PreprocessVisitor extends TIBasicBaseVisitor {

    private static Logger LOGGER = LoggerFactory.getLogger(TIBasicBaseVisitor.class);

    /**
     * Visits the command list and calls the underlying accept methods. If any of the commands return a {@link Label}
     * object and there is not yet a label with this name, the label information will be stored in a temporary map and
     * will then be returned. This is necessary, so that a more top-level visitor can merge the label commands.
     *
     * @param ctx
     *         The command list context.
     * @return A map of
     */
    @Override
    public Object visitCommandList(@NotNull TIBasicParser.CommandListContext ctx) {
        List<TIBasicParser.CommandContext> commandContextList = ctx.command();

        Map<String, Integer> labelMap = new HashMap<>();

        // Iterate through all commands and find labels
        for (int i = 0; i < commandContextList.size(); i++) {
            Object result = commandContextList.get(i).accept(this);

            if (result instanceof Label) {
                Label label = (Label) result;

                String labelIdentifier = label.getIdentifier();
                if (!labelMap.containsKey(labelIdentifier)) {
                    labelMap.put(labelIdentifier, i);
                    LOGGER.info("Registered label {} as command {}", labelIdentifier, i);
                } else {
                    LOGGER.warn("Label {} is already defined - ignoring new definition", labelIdentifier);
                }
            }
        }
        return new LabelMapWrapper(labelMap);
    }

    /**
     * Visits a LabelStatement and returns a Label object that contains the name of the label. This object must be
     * passed upwards in the visitor hierarchy until it is collected.
     *
     * @param ctx
     *         The label context.
     * @return A {@link Label} object for internal use.
     */
    @Override
    public Object visitLabelStatement(@NotNull TIBasicParser.LabelStatementContext ctx) throws PreprocessException {
        TIBasicParser.LabelIdentifierContext labelIdentifier = ctx.labelIdentifier();
        if (!ValidationUtils.isValidLabelIdentifier(labelIdentifier.getText()))
            throw new PreprocessException(-1, -1, "Invalid label identifier: " + labelIdentifier.getText());
        return new Label(labelIdentifier.getText());
    }

    private static class Label {

        String identifier;

        public Label(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    /**
     * Wrapper class for a label map.
     */
    static class LabelMapWrapper {

        Map<String, Integer> content;

        public LabelMapWrapper(Map<String, Integer> content) {
            this.content = content;
        }

        public Map<String, Integer> getMap() {
            return content;
        }
    }
}
