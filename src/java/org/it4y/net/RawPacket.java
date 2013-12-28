package org.it4y.net;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

/**
 * Created by luc on 12/26/13.
 */
public class RawPacket {

    protected ByteBuffer rawPacket=null;
    protected int rawSize=-1;
    protected int rawLimit=-1;

    public RawPacket(ByteBuffer bytes,int length) {
        this.rawPacket=bytes;
        this.rawSize=length;
        this.rawLimit=bytes.limit();
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
