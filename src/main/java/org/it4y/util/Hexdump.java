/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.it4y.util;

import java.nio.ByteBuffer;

public class Hexdump {
    final protected static char[] hexArray = "0123456789ABCDEF ".toCharArray();

    public static String bytesToHex(final ByteBuffer bytes, final int maxSize) {
        final char[] hexChars = new char[Math.min(bytes.capacity(), maxSize) * 3];
        int v;
        for (int j = 0; j < Math.min(maxSize, bytes.capacity()); j++) {
            v = bytes.get(j) & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = hexArray[0x10];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(final byte[] bytes, final int maxSize) {
        final char[] hexChars = new char[Math.min(bytes.length, maxSize) * 3];
        int v;
        for (int j = 0; j < Math.min(maxSize, bytes.length); j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = hexArray[0x10];
        }
        return new String(hexChars);
    }


}
