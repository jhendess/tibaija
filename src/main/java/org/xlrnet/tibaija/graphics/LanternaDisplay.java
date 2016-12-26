/*
 * Copyright (c) 2016 Jakob Hendeß
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

package org.xlrnet.tibaija.graphics;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlrnet.tibaija.exception.TIGraphicsException;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

/**
 * Display using the lanterna {@link com.googlecode.lanterna.screen.Screen} UI.
 */
public class LanternaDisplay implements Display {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanternaDisplay.class);

    private static final int TI_83_PLUS_WIDTH = 95;

    private static final int TI_83_PLUS_HEIGHT = 63;

    private static final TextCharacter BLANK = TextCharacter.DEFAULT_CHARACTER;

    private static final TextCharacter FILLED = new TextCharacter(FontConstants.FILLED);

    /**
     * Flag to indicate if the display is booted.
     */
    private boolean booted;

    private Screen screen;

    @Override
    public void clearScreen() {
        checkInternalState();

        LOGGER.debug("Clearing display");
        screen.clear();
    }

    @Override
    public void close() throws IOException {
        checkInternalState();

        LOGGER.debug("Shutting down display");
        screen.stopScreen();
        LOGGER.info("Display shut down");
    }

    @Override
    public void flush() {
        checkInternalState();

        LOGGER.trace("Flushing display");
        try {
            screen.refresh();
        } catch (IOException e) {
            LOGGER.error("Flushing display failed");
            throw new TIGraphicsException("Flushing display failed", e);
        }
    }

    @Override
    public int getHorizontalDimension() {
        return TI_83_PLUS_WIDTH;
    }

    @Override
    public int getVerticalDimension() {
        return TI_83_PLUS_HEIGHT;
    }

    @Override
    public void invertPixel(int x, int y) {
        checkInternalState();

        LOGGER.trace("Inverting pixel at {},{}", x, y);
        TextCharacter character = screen.getBackCharacter(x, y);
        PixelState pixelState = FILLED.equals(character) ? PixelState.OFF : PixelState.ON;
        setPixel(pixelState, x, y);
    }

    @Override
    public boolean isPixelOn(int x, int y) {
        checkInternalState();

        TextCharacter character = screen.getBackCharacter(x, y);
        return FILLED.equals(character);
    }

    @Override
    public void open() throws IOException {
        checkState(!booted, "Display already booted");
        LOGGER.debug("Booting display");
        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(getHorizontalDimension(), getVerticalDimension()))
                .createTerminalEmulator();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        booted = true;
        LOGGER.info("Display booted");
    }

    @Override
    public void setPixel(PixelState pixelState, int x, int y) {
        checkInternalState();

        LOGGER.trace("Setting pixel {},{} to {}", x, y, pixelState);

        TextCharacter printCharacter = pixelState == PixelState.ON ? FILLED : BLANK;
        screen.setCharacter(x, y, printCharacter);
    }

    private void checkInternalState() {
        checkState(booted, "Display not yet booted");
    }
}
