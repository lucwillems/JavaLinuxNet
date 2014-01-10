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

public class TCPoptionSACK implements TCPOption {

    public TCPoptionSACK() {
    }

    public String getName() {
        return "SACK";
    }

    public int getLength() {
        return 2;
    }

    public String toString() {
        return getName();
    }
}
