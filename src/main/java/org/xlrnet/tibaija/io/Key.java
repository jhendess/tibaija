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

package org.xlrnet.tibaija.io;

/**
 * Enum representing a real key on a non-calculator device.
 */
public enum Key {

    A(KeyType.CHARACTER, 'A'),
    B(KeyType.CHARACTER, 'B'),
    C(KeyType.CHARACTER, 'C'),
    D(KeyType.CHARACTER, 'D'),
    E(KeyType.CHARACTER, 'E'),
    F(KeyType.CHARACTER, 'F'),
    G(KeyType.CHARACTER, 'G'),
    H(KeyType.CHARACTER, 'H'),
    I(KeyType.CHARACTER, 'I'),
    J(KeyType.CHARACTER, 'J'),
    K(KeyType.CHARACTER, 'K'),
    L(KeyType.CHARACTER, 'L'),
    M(KeyType.CHARACTER, 'M'),
    N(KeyType.CHARACTER, 'N'),
    O(KeyType.CHARACTER, 'O'),
    P(KeyType.CHARACTER, 'P'),
    Q(KeyType.CHARACTER, 'Q'),
    R(KeyType.CHARACTER, 'R'),
    S(KeyType.CHARACTER, 'S'),
    T(KeyType.CHARACTER, 'T'),
    U(KeyType.CHARACTER, 'U'),
    V(KeyType.CHARACTER, 'V'),
    W(KeyType.CHARACTER, 'W'),
    X(KeyType.CHARACTER, 'X'),
    Y(KeyType.CHARACTER, 'Y'),
    Z(KeyType.CHARACTER, 'Z'),
    ZERO(KeyType.NUMBER, '0'),
    ONE(KeyType.NUMBER, '1'),
    TWO(KeyType.NUMBER, '2'),
    THREE(KeyType.NUMBER, '3'),
    FOUR(KeyType.NUMBER, '4'),
    FIVE(KeyType.NUMBER, '5'),
    SIX(KeyType.NUMBER, '6'),
    SEVEN(KeyType.NUMBER, '7'),
    EIGHT(KeyType.NUMBER, '8'),
    NINE(KeyType.NUMBER, '9');

    private final KeyType keyType;

    private final char value;

    Key(KeyType keyType, char value) {
        this.keyType = keyType;
        this.value = value;
    }

    enum KeyType {
        CHARACTER,

        NUMBER,

        OTHER;
    }
}
