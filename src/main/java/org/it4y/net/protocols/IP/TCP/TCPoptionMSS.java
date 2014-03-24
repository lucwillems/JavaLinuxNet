/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionMSS implements TCPOption {
    public static final String name="mss";
    public static final int length=4;

    private short mss;

    public TCPoptionMSS(short mss) {
        this.mss = mss;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return "mss:" + ((int) mss & 0xffff);
    }

    public short getMss() {
        return mss;
    }
}
