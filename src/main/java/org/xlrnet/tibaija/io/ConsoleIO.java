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

package org.xlrnet.tibaija.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.memory.AnswerVariable;

import java.io.*;

/**
 * Default implementation for {@link org.xlrnet.tibaija.io.CalculatorIO}. All in- and output will be redirected to the
 * console. Printing or drawing to the graphical screen is not supported and will be ignored.
 */
public class ConsoleIO implements CalculatorIO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleIO.class);

    private final BufferedReader reader;

    private final BufferedWriter writer;

    public ConsoleIO(final Reader reader, final Writer writer) {
        this.reader = new BufferedReader(reader);
        this.writer = new BufferedWriter(writer);
    }

    @Override
    public void print(String input) {
        try {
            writer.write(input);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("I/O print error: ", e);
        }
    }

    @Override
    public void printLine(String input) {
        try {
            writer.write(input);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("I/O print error: ", e);
        }
    }

    @Override
    public void printLine(AnswerVariable... values) {
        for (AnswerVariable value : values) {
            printLine(value.toString());            // TODO: Implement real output formatting
        }
    }

    @Override
    public String readInput() throws IOException {
        return reader.readLine();
    }
}
