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

public class RawPacket {

    protected ByteBuffer rawPacket = null;
    protected int rawSize = -1;
    protected int rawLimit = -1;

    public RawPacket(ByteBuffer bytes, int length) {
        this.rawPacket = bytes;
        this.rawSize = length;
        this.rawLimit = bytes.limit();
    }

    public ByteBuffer getRawPacket() {
        return rawPacket;
    }

    public void resetBuffer() {
        rawPacket.position(0);
        rawPacket.limit(rawLimit);
    }


    public int getRawSize() {
        return rawSize;
    }

    public int getHeaderSize() {
        return 0;
    }

    public int getPayLoadSize() {
        return rawLimit;
    }

    public ByteBuffer getHeader() {
        return null;
    }

    public ByteBuffer getPayLoad() {
        rawPacket.position(0);
        return rawPacket.slice();
    }
}
