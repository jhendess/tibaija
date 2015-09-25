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
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.TIRuntimeException;
import org.xlrnet.tibaija.graphics.FontConstants;
import org.xlrnet.tibaija.graphics.FontRegistry;
import org.xlrnet.tibaija.graphics.HomeScreen;
import org.xlrnet.tibaija.graphics.NullHomeScreen;
import org.xlrnet.tibaija.io.CalculatorIO;
import org.xlrnet.tibaija.io.ConsoleIO;
import org.xlrnet.tibaija.memory.CalculatorMemory;
import org.xlrnet.tibaija.memory.DefaultCalculatorMemory;
import org.xlrnet.tibaija.processor.ExecutionEnvironment;
import org.xlrnet.tibaija.processor.TI83Plus;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application class for starting the interpreter.
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    boolean configured = false;

    public static void main(String[] args) {
        new Application().run(args);
    }

    private static VirtualCalculator getDefaultCalculator(CodeProvider codeProvider) throws IOException {
        Reader reader;
        Writer writer;

        if (System.console() != null) {
            Console console = System.console();
            reader = console.reader();
            writer = console.writer();
            LOGGER.debug("Initialised native system console");
        } else {
            reader = new InputStreamReader(System.in);
            writer = new OutputStreamWriter(System.out);
            LOGGER.debug("Initialised system I/O streams");
        }

        CalculatorIO io = new ConsoleIO(reader, writer);
        CalculatorMemory memory = new DefaultCalculatorMemory();
        HomeScreen homeScreen = new NullHomeScreen();
        FontRegistry fontRegistry = new FontRegistry();
        fontRegistry.registerFont(Paths.get("largeFont.json"), FontConstants.FONT_LARGE);
        fontRegistry.registerFont(Paths.get("smallFont.json"), FontConstants.FONT_SMALL);

        ExecutionEnvironment.newEnvironment(memory, io, codeProvider, homeScreen, fontRegistry);
        return new TI83Plus(memory, io, codeProvider);
    }

    public boolean isConfigured() {
        return configured;
    }

    private void parseArguments(String[] args) {
        ApplicationConfiguration config = new ApplicationConfiguration();
        CmdLineParser parser = new CmdLineParser(config);

        try {
            parser.parseArgument(args);
            if (config.isShowHelp() || config.isInteractive() || config.getStartFile() != null)
                configured = true;

            if (config.isInteractive()) {
                runInteractiveMode();
            } else if (config.getStartFile() != null) {
                runFileMode(config.getStartFile());
            } else if (config.isShowHelp()) {
                printUsage(parser);
            }

        } catch (CmdLineException e) {
            LOGGER.error("Unable to parse command line parameters", e);
        } catch (Exception e) {
            LOGGER.error("Internal error", e);
        }

        if (!isConfigured()) {
            printUsage(parser);
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
        LOGGER.info("Starting interpreter from file ...");

        try {
            Path filePath = startFile.toPath();
            Path parentDirectory = filePath.toAbsolutePath().getParent();
            FileSystemCodeProvider codeProvider = new FileSystemCodeProvider(parentDirectory);
            String bootFile = codeProvider.registerFile(filePath);
            VirtualCalculator calculator = getDefaultCalculator(codeProvider);

            LOGGER.info("System booted");

            calculator.executeProgram(bootFile);
        } catch (IOException e) {
            LOGGER.error("Program load error", e);
        }

    }

    private void runInteractiveMode() throws IOException {
        LOGGER.info("Starting interpreter in interactive mode ...");

        VirtualCalculator calculator = getDefaultCalculator(new DummyCodeProvider());
        CalculatorIO io = calculator.getIODevice();
        CalculatorMemory memory = calculator.getMemory();

        showWelcome();
        String input;

        while (true) {
            try {
                input = io.readInput();

                if (input == null || StringUtils.equalsIgnoreCase("exit", input)) {
                    break;
                }

                calculator.interpret(input);
                io.printLine(memory.getLastResult());

            } catch (TIRuntimeException ti) {
                io.printLine("ERR: " + ti.getMessage());
            } catch (Exception e) {
                LOGGER.error("An internal error occurred", e);
                break;
            }
        }

        LOGGER.info("Exiting interpreter ...");
        System.exit(0);
    }

    private void showWelcome() {
        LOGGER.info("Started tibaija in interactive mode");
        System.out.println("#############################################################################");
        System.out.println("#################### Tibaija started in interactive mode ####################");
        System.out.println("####################   Type 'exit' to stop interpreter   ####################");
        System.out.println("#############################################################################");
    }
}
