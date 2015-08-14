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

package org.xlrnet.tibaija;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.PreprocessException;
import org.xlrnet.tibaija.exception.ProgramNotFoundException;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.processor.ControlflowLessTIBasicVisitor;
import org.xlrnet.tibaija.processor.ExecutableProgram;
import org.xlrnet.tibaija.processor.FullTIBasicVisitor;
import org.xlrnet.tibaija.processor.Preprocessor;
import org.xlrnet.tibaija.util.ExecutionEnvironmentUtil;
import org.xlrnet.tibaija.util.ValidationUtils;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Virtual calculator based on the TI-83+ model.
 */
public class TI83Plus implements VirtualCalculator {

    private static Logger LOGGER = LoggerFactory.getLogger(TI83Plus.class);

    private final Preprocessor preprocessor = new Preprocessor();

    private final CalculatorMemory calculatorMemory;

    private final CalculatorIO calculatorIO;

    private final CodeProvider codeProvider;

    public TI83Plus(CalculatorMemory calculatorMemory, CalculatorIO calculatorIO, CodeProvider codeProvider) {
        this.calculatorMemory = calculatorMemory;
        this.calculatorIO = calculatorIO;
        this.codeProvider = codeProvider;
    }

    @Override
    public void executeProgram(String programName) throws ProgramNotFoundException {
        programName = programName.toUpperCase();
        if (!calculatorMemory.containsProgram(programName)) {
            try {
                loadProgram(programName, codeProvider.getProgramCode(programName));
            } catch (IOException e) {
                LOGGER.error("Loading external code failed", e);
            }
        }

        ExecutableProgram executableProgram = calculatorMemory.getStoredProgram(programName);

        LOGGER.info("Starting program '{}'", programName);

        ExecutionEnvironmentUtil.newDefaultEnvironment(this).run(executableProgram, new FullTIBasicVisitor());
    }

    @Override
    public CalculatorIO getIODevice() {
        return calculatorIO;
    }

    @Override
    public CalculatorMemory getMemory() {
        return calculatorMemory;
    }

    @Override
    public void interpret(String input) {
        // Fix input without colon:
        input = StringUtils.prependIfMissing(input, ":");

        try {
            ExecutableProgram executableProgram = internalPreprocessCode("TMP", input);
            ExecutionEnvironmentUtil.newDefaultEnvironment(this).run(executableProgram, new ControlflowLessTIBasicVisitor());
        } catch (PreprocessException e) {
            LOGGER.error("Preprocessing commands failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void loadProgram(String programName, CharSequence programCode) {
        checkArgument(ValidationUtils.isValidProgramName(programName), "Invalid program name: %s", programName);

        try {
            ExecutableProgram executableProgram = internalPreprocessCode(programName, programCode);
            getMemory().storeProgram(programName, executableProgram);
        } catch (PreprocessException e) {
            LOGGER.error("Loading program {} failed", programName);
            throw e;
        }
    }

    /**
     * Run all neccessary internal routines for preprocessing a given code.
     */
    private ExecutableProgram internalPreprocessCode(String programName, CharSequence programCode) {
        ExecutableProgram executableProgram;
        executableProgram = preprocessor.preprocessProgramCode(programName, programCode);
        return executableProgram;
    }
}
