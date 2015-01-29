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

package org.xlrnet.tibaija.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.xlrnet.tibaija.antlr.TIBasicLexer;
import org.xlrnet.tibaija.antlr.TIBasicParser;
import org.xlrnet.tibaija.processor.PreprocessErrorListener;

/**
 * Helper util for testing.
 */
public class TestUtils {

    public static final double DEFAULT_TOLERANCE = 0.0000001;

    /**
     * Create a parser from the given input string.
     *
     * @param testString
     *         The input string.
     * @return A parser that is capable of parsing the given input.
     */
    public static TIBasicParser createParser(String testString) {
        CharStream stream = new ANTLRInputStream(testString);
        TIBasicLexer lexer = new TIBasicLexer(stream);
        lexer.addErrorListener(PreprocessErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TIBasicParser parser = new TIBasicParser(tokens);
        parser.addErrorListener(PreprocessErrorListener.INSTANCE);
        return parser;
    }
}
