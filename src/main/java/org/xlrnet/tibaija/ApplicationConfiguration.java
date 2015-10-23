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

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;

/**
 * Bean for application configuration. Is usually automatically configured by args4j.
 */
public class ApplicationConfiguration {

    @Argument(metaVar = "[file]", usage = "the first file that should be interpreted")
    private File startFile;

    @Option(name = "-i", usage = "start interpreter in interactive mode")
    private boolean interactive;

    @Option(name = "-h", usage = "show this help")
    private boolean showHelp;

    @Option(name = "-d", usage = "enable debug logging")
    private boolean showDebugLog;

    public File getStartFile() {
        return startFile;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public boolean isShowDebugLog() {
        return showDebugLog;
    }

    public boolean isShowHelp() {
        return showHelp;
    }
}
