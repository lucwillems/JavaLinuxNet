/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.it4y.net.protocols.IP.IPIP;

import org.it4y.net.protocols.IP.IpPacket;
import org.it4y.util.Hexdump;
import java.nio.ByteBuffer;

/**
 * Created by luc on 3/24/14.
 * bassed on RFC1853
 */
public class IPIPPacket extends IpPacket{
    public static final byte PROTOCOL=4;
    public static final int IPIP_HEADER_SIZE = 0;

    private int ip_header_offset;

    public IPIPPacket(ByteBuffer buffer, int size) {
        super(buffer, size);
        ip_header_offset=super.getHeaderSize();
    }
    public IPIPPacket(IpPacket ip) {
        super(ip.getRawPacket(),ip.getRawSize());
        //get IP header size
        ip_header_offset=ip.getHeaderSize();
    }

    @Override
    public void initIpHeader() {
        super.initIpHeader();
        setProtocol(IPIPPacket.PROTOCOL);
        ip_header_offset=super.getIpHeaderSize();
    }

    @Override
    public int getHeaderSize() {
        return IPIP_HEADER_SIZE;
    }

    @Override
    public int getPayLoadSize() {
        return rawLimit - ip_header_offset - getHeaderSize();
    }

    @Override
    public ByteBuffer getHeader() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset);
        rawPacket.limit(ip_header_offset + getHeaderSize());
        return rawPacket.slice();
    }

    @Override
    public ByteBuffer getPayLoad() {
        //get IP header size
        resetBuffer();
        rawPacket.position(ip_header_offset+getHeaderSize());
        rawPacket.limit(rawSize);
        return rawPacket.slice();
    }

    @Override
    public String toString() {
        final StringBuilder s=new StringBuilder(128);
        if (getPayLoadSize()>0) {
            s.append(Hexdump.bytesToHex(getPayLoad(), Math.min(getPayLoadSize(), 20)));
        }
        return s.toString();
    }

}
