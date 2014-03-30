/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
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
