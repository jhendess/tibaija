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

import ch.qos.logback.classic.Level;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.TIRuntimeException;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.io.FileSystemCodeProvider;
import org.xlrnet.tibaija.memory.ReadOnlyCalculatorMemory;
import org.xlrnet.tibaija.processor.ExecutionEnvironmentFactory;
import org.xlrnet.tibaija.processor.InternalExecutionEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application class for starting the interpreter.
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private boolean configured = false;

    public static void main(String[] args) {
        new Application().run(args);
    }

    private void configureRootLogger(ApplicationConfiguration config) {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Level level = Level.INFO;
        if (config.isShowDebugLog()) {
            level = Level.DEBUG;
        } else if (config.isShowVerboseLog()) {
            level = Level.TRACE;
        }
        rootLogger.setLevel(level);
    }

    private void parseArguments(String[] args) {
        ApplicationConfiguration config = new ApplicationConfiguration();
        CmdLineParser parser = new CmdLineParser(config);

        try {
            parser.parseArgument(args);
            if (config.isShowHelp() || config.isInteractive() || config.getStartFile() != null) {
                this.configured = true;
            }

            configureRootLogger(config);

            if (config.isInteractive()) {
                runInteractiveMode();
            } else if (config.getStartFile() != null) {
                runFileMode(config.getStartFile());
            } else if (config.isShowHelp() || !configured) {
                printUsage(parser);
            }

        } catch (CmdLineException e) {
            LOGGER.error("Unable to parse command line parameters", e);
        } catch (Exception e) {
            LOGGER.error("Internal error", e);
        }
    }

    private void printUsage(CmdLineParser parser) {
        System.out.println("Tibaija - a TI-Basic interpreter for Java");
        System.out.println();
        System.out.println("Usage: [file] [options]");
        parser.printUsage(System.out);
    }

    private void run(String[] args) {
        parseArguments(args);
    }

    private void runFileMode(File startFile) {
        LOGGER.info("Starting interpreter from file {} ...", startFile.getAbsolutePath());

        InternalExecutionEnvironment environment = null;
        try {
            Path filePath = startFile.toPath();
            Path parentDirectory = filePath.toAbsolutePath().getParent();
            FileSystemCodeProvider codeProvider = new FileSystemCodeProvider(parentDirectory);
            environment = ExecutionEnvironmentFactory.newDefaultEnvironment(codeProvider);
            environment.boot();
            String bootFile = codeProvider.registerFile(filePath);

            environment.executeProgram(bootFile);
        } catch (IOException e) {
            LOGGER.error("Program load error", e);
        }

        if (environment != null) {
            environment.shutdown();
        }
    }

    private void runInteractiveMode() throws IOException {
        LOGGER.info("Starting interpreter in interactive mode ...");

        FileSystemCodeProvider codeProvider = new FileSystemCodeProvider(Paths.get(""));
        InternalExecutionEnvironment environment = ExecutionEnvironmentFactory.newDefaultEnvironment(codeProvider);
        CalculatorIO io = environment.getCalculatorIO();
        ReadOnlyCalculatorMemory memory = environment.getMemory();
        environment.boot();

        showWelcome(io);
        String input;
        boolean isRunning = true;

        while (isRunning) {
            try {
                input = io.readInput();

                if (input == null || StringUtils.equalsIgnoreCase("exit", input)) {
                    isRunning = false;
                }

                environment.interpret(input);
                io.printLine(memory.getLastResult());

            } catch (TIRuntimeException ti) {
                io.printLine("ERR: " + ti.getMessage());
            } catch (Exception e) {
                LOGGER.error("An internal error occurred", e);
                isRunning = false;
            }
        }

        LOGGER.info("Exiting interpreter ...");
        environment.shutdown();
    }

    private void showWelcome(CalculatorIO io) {
        LOGGER.info("Started tibaija in interactive mode");
        io.printLine("#############################################################################");
        io.printLine("#################### Tibaija started in interactive mode ####################");
        io.printLine("####################   Type 'exit' to stop interpreter   ####################");
        io.printLine("#############################################################################");
    }
}
