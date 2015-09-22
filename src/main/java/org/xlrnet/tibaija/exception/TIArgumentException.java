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

package org.xlrnet.tibaija.exception;

import com.google.common.collect.ImmutableList;
import org.xlrnet.tibaija.memory.Parameter;
import org.xlrnet.tibaija.memory.Value;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This exception will be thrown if invalid arguments were given to a command. This includes wrong types and parameter
 * count.
 */
public class TIArgumentException extends TIRuntimeException {

    private static final long serialVersionUID = -9040606410143723978L;

    private List<? extends Parameter> arguments;

    public TIArgumentException(String message, Number... arguments) {
        this(message, ImmutableList.copyOf(
                Stream.of(arguments)
                        .map(Value::of)
                        .map(Parameter::value)
                        .collect(Collectors.toList())));
    }

    public TIArgumentException(String message, Parameter... arguments) {
        this(message, ImmutableList.copyOf(arguments));
    }

    public TIArgumentException(String message, Value... arguments) {
        this(message, ImmutableList.copyOf(arguments).stream().map(Parameter::value).collect(Collectors.toList()));
    }

    public TIArgumentException(String message, List<Parameter> arguments) {
        super(message);
        this.arguments = arguments;
    }

    public TIArgumentException(int linenumber, int startIndex, String message, List<Parameter> arguments) {
        super(linenumber, startIndex, message);
        this.arguments = ImmutableList.copyOf(arguments);
    }

    public List<? extends Parameter> getArguments() {
        return arguments;
    }
}
