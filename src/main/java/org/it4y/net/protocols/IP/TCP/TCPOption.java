/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public interface TCPOption {

    //WELL KNOW TCP options id's
    public final static byte END = 0;
    public final static byte NOP = 1;
    public final static byte MSS = 2;
    public final static byte WSCALE = 3;
    public final static byte SACK_ENABLED = 4;
    public final static byte SACK = 5;
    public final static byte TIMESTAMP = 8;

    public String getName();

    public int getLength();
}
