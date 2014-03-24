/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionSACK implements TCPOption {
    public static final String name="SACK";
    public static final int length=2;


    public TCPoptionSACK() {
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
