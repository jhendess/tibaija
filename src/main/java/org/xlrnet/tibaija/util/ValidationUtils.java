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

package org.xlrnet.tibaija.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Several helper methods for string validation.
 */
public class ValidationUtils {

    /**
     * Checks if the given input string is a valid label identifier for TI-Basic. Label names can be either one or two
     * characters long, and the only characters you're allowed to use are letters (including θ) and numbers 0 to 9;
     * this means 37+37*37=1406 possible combinations.
     *
     * @param labelIdentifier
     *         The name that should be checked.
     * @return True if the given string is a valid label identifier. False otherwise.
     */
    public static boolean isValidLabelIdentifier(@NotNull String labelIdentifier) {
        return Pattern.matches("[A-Z0-9θ]{1,2}", labelIdentifier);
    }

    /**
     * Checks if the given input string is a valid program name. A valid program name consists of one to eight capital
     * letters or theta. Except for the first symbol, digits from 0 to 9 are also allowed.
     *
     * @param programName
     *         The name that should be checked.
     * @return True if the given string is a valid program name. False otherwise.
     */
    public static boolean isValidProgramName(@NotNull String programName) {
        return Pattern.matches("[A-Zθ][A-Zθ0-9]{0,7}", programName);
    }

    /**
     * Checks if the given input string is a valid list name. A valid list name consists of one to five capital
     * letters or theta. Except for the first symbol, digits from 0 to 9 are also allowed.
     *
     * @param listName
     *         Name of the list variable with the leading list token.
     * @return True if the given string is a valid list variable name. False otherwise.
     */
    public static boolean isValidListName(@NotNull String listName) {
        return Pattern.matches("∟[A-Zθ][A-Z0-9θ]{0,4}", listName);
    }

    /**
     * Checks if the given input string is a valid name for a number variable. A valid number variable name consists of
     * any of capital letters A to Z or theta.
     *
     * @param variableName
     *         Name of the number variable.
     * @return True if the given string is a valid number variable name. False otherwise.
     */
    public static boolean isValidNumberVariableName(@NotNull String variableName) {
        return Pattern.matches("[A-Zθ]", variableName);        
    }
}
