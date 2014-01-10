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

public class TCPoptionNOP implements TCPOption {
    public TCPoptionNOP() {
    }

    public String getName() {
        return "NOP";
    }

    public int getLength() {
        return 1;
    }

    public String toString() {
        return getName();
    }
}
