/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

import org.it4y.net.protocols.IP.TCP.TCPOption;

public class TCPoptionMSS implements TCPOption {
    short mss;

    public TCPoptionMSS(short mss) {
        this.mss = mss;
    }

    public String getName() {
        return "mss";
    }

    public int getLength() {
        return 4;
    }

    public String toString() {
        return "mss:" + ((int) mss & 0xffff);
    }
}
