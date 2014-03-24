/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.protocols.IP.TCP;

public class TCPoptionEnd implements TCPOption {
    public static final String name="end";
    public static final int length=1;

    public TCPoptionEnd() {
    }

    ;

    public String getName() {  return name;  }

    public int getLength() {
        return length;
    }

    public String toString() {
        return getName();
    }
}
