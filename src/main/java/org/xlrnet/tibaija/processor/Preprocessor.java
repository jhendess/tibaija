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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.xlrnet.tibaija.antlr.TIBasicLexer;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.exception.PreprocessException;

/**
 * The preprocessor for TI-Basic programs. The main task of this class is to run through a source program, parse it
 * and create a map of all available labels and return an instance of {@link ExecutableProgram}.
 * After the preprocessor has completed its work without any errors, the syntax of the provided TI-Basic program is
 * correct.
 */
public class Preprocessor {

    PreprocessVisitor preprocessVisitor = new PreprocessVisitor();

    /**
     * Takes a TI-Basic program source code as input, checks for valid syntax and returns a new {@link
     * ExecutableProgram}. During preprocessing all labels will be identified, so that the ExecutableProgram contains a
     * valid label map.
     *
     * @param programName
     *         Name of the program to load. Must consist of one to eight capitalized letters or digits
     * @param programCode
     *         Valid TI-Basic code that should be preprocessed
     * @return An instance of {@link ExecutableProgram} with all necessary information to execute the given source,
     * @throws PreprocessException
     *         Will be thrown if any errors occur while parsing or processing the program
     */
    public ExecutableProgram preprocessProgramCode(String programName, CharSequence programCode) throws PreprocessException {

        ExecutableProgram executableProgram;
        TIBasicParser parser = getParser(programCode);
        TIBasicParser.ProgramContext programContext = parser.program();

        Object result = preprocessVisitor.visitProgram(programContext);

        executableProgram = new ExecutableProgram();
        executableProgram.setMainProgramContext(programContext);
        executableProgram.setProgramName(programName);
        executableProgram.setOriginalSource(programCode);

        if (result instanceof PreprocessVisitor.LabelMapWrapper) {
            PreprocessVisitor.LabelMapWrapper wrapper = (PreprocessVisitor.LabelMapWrapper) result;
            executableProgram.setInternalLabelMap(wrapper.getMap());
        }

        return executableProgram;
    }

    private TIBasicParser getParser(CharSequence programCode) {
        ANTLRInputStream inputStream = new ANTLRInputStream(programCode.toString());
        TIBasicLexer lexer = new TIBasicLexer(inputStream);
        lexer.addErrorListener(PreprocessErrorListener.INSTANCE);
        TokenStream tokens = new CommonTokenStream(lexer);
        TIBasicParser parser = new TIBasicParser(tokens);
        parser.addErrorListener(PreprocessErrorListener.INSTANCE);
        return parser;
    }

}
