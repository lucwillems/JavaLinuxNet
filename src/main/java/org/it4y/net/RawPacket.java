/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net;

import java.nio.ByteBuffer;

public abstract class RawPacket {

    protected ByteBuffer rawPacket;
    protected int rawSize = -1;
    protected int rawLimit = -1;

    public RawPacket(final ByteBuffer bytes, final int length) {
        rawPacket = bytes;
        rawSize = length;
        rawLimit = bytes.limit();
    }

    public ByteBuffer getRawPacket() {
        return rawPacket;
    }

    public void resetBuffer() {
        rawPacket.clear();
        rawPacket.limit(rawLimit);
    }

    public int getRawSize() {
        return rawSize;
    }

    public abstract int getHeaderSize();
    public abstract  int getPayLoadSize();
    public abstract ByteBuffer getHeader();
    public abstract ByteBuffer getPayLoad();
    public abstract int getDstRoutingHash();
    public abstract int getFlowHash();
    public abstract int getReverseFlowHash();

    public void release() {
        this.rawPacket=null;
        this.rawSize=-1;
        this.rawLimit=-1;
    }
}
