/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionWindowScale implements TCPOption {
    public static final String name="wscale";
    public static final int length=3;

    private byte scale;

    public TCPoptionWindowScale(byte scale) {
        this.scale = scale;
    }

    public String getName() { return name; }

    public int getLength() {
        return length;
    }

    public String toString() {
        return "wscale:" + ((int) (scale) & 0x00ff);
    }

    public byte getScale() {
        return scale;
    }
}
