/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionNOP implements TCPOption {
    public static final String name="NOP";
    public static final int length=1;

    public TCPoptionNOP() {
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return getName();
    }
}
