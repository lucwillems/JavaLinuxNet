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
    byte scale;

    public TCPoptionWindowScale(byte scale) {
        this.scale = scale;
    }

    public String getName() {
        return "wscale";
    }

    public int getLength() {
        return 3;
    }

    public String toString() {
        return "wscale:" + ((int) (scale) & 0x00ff);
    }
}
