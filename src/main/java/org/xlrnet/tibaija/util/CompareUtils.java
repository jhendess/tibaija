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

import java.util.Comparator;
import java.util.Objects;

/**
 * Utility class that provides comparison methods with boolean results like e.g. isGreaterThan().
 */
public class CompareUtils {

    /**
     * Compares two given objects and returns true if the first object is <u>greater than</u> the second one i.e. the
     * implementation of compareTo() must return <b>1</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is greater or false if not.
     */
    public static <T extends Comparable> boolean isGreaterThan(T o1, T o2) {
        return (1 == Objects.compare(o1, o2, Comparator.<T>naturalOrder()));
    }

    /**
     * Compares two given objects and returns true if the first object is <u>greater than or equal to</u> the second one
     * i.e. the
     * implementation of compareTo() must return either <b>1 or 0</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is greater than or equal to the second. False otherwise.
     */
    public static <T extends Comparable> boolean isGreaterOrEqual(T o1, T o2) {
        return isGreaterThan(o1, o2) || isEqual(o1, o2);
    }

    /**
     * Compares two given objects and returns true if the first object is <u>less than</u> the second one i.e. the
     * implementation of compareTo() must return <b>-1</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is less than the second. False otherwise..
     */
    public static <T extends Comparable> boolean isLessThan(T o1, T o2) {
        return (-1 == Objects.compare(o1, o2, Comparator.<T>naturalOrder()));
    }

    /**
     * Compares two given objects and returns true if the first object is <u>less than or equal to</u> the second one
     * i.e. the
     * implementation of compareTo() must return either <b>-1 or 0</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is greater than or equal to the second. False otherwise.
     */
    public static <T extends Comparable> boolean isLessOrEqual(T o1, T o2) {
        return isLessThan(o1, o2) || isEqual(o1, o2);
    }

    /**
     * Compares two given objects and returns true if the first object is <u>equal to</u> the second one i.e. the
     * implementation of compareTo() must return <b>0</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is equal to the second. False otherwise.
     */
    public static <T extends Comparable> boolean isEqual(T o1, T o2) {
        return (0 == Objects.compare(o1, o2, Comparator.<T>naturalOrder()));
    }

    /**
     * Compares two given objects and returns true if the first object is <u>not equal to</u> the second one i.e. the
     * implementation of compareTo() must <b>not</b> return <b>0</b>.
     *
     * @param o1
     *         The first object.
     * @param o2
     *         The second object.
     * @param <T>
     *         A comparable class type.
     * @return True if the first object is not equal to the second. False otherwise.
     */
    public static <T extends Comparable> boolean isNotEqual(T o1, T o2) {
        return !isEqual(o1, o2);
    }

}
