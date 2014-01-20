/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
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
