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

package org.it4y.net.netlink;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by luc on 1/2/14.
 */
public abstract class NlMessage {

    public static final int RTA_INDEX_NOTSUPPORTED=-1;

    //Read nlmsg header
    protected final int nlmsg_len;
    protected final short nlmsg_type;
    protected final short nlmsg_flags;
    protected final int nlmsg_seq;
    protected final int nlmsg_pid;
    protected final HashMap<Integer, RTAMessage> rtaMessages;

    public NlMessage(final ByteBuffer msg) {
        //Read nlmsg header
        nlmsg_len = msg.getInt();
        nlmsg_type = msg.getShort();
        nlmsg_flags = msg.getShort();
        nlmsg_seq = msg.getInt();
        nlmsg_pid = msg.getInt();
        rtaMessages = new HashMap<Integer, RTAMessage>(10);
    }

    public int getNlMsgLen() {
        return nlmsg_len;
    }

    public short getNlMsgType() {
        return nlmsg_type;
    }

    public short getNlmsg_flags() {
        return nlmsg_flags;
    }

    public int getNlMsgSequence() {
        return nlmsg_seq;
    }

    public int getNlMsgPID() {
        return nlmsg_pid;
    }

    public RTAMessage getRTAMessage(final int index) {
        return rtaMessages.get(index);
    }

    public boolean moreMessages() {
        return (nlmsg_flags & (short) 0x02) > 0;
    }

    public RTAMessage getRTAMessage(final String name) {
        return getRTAMessage(getRTAIndex(name));
    }

    public abstract int getRTAIndex(String name);

    protected abstract RTAMessage createRTAMessage(int position, ByteBuffer msg);

    protected void parseRTAMessages(final ByteBuffer msg) {
        while (msg.position() < nlmsg_len) {
            final int position = msg.position();
            final RTAMessage rta = createRTAMessage(position, msg);
            msg.position(position + rta.getPaddedSize());
            rtaMessages.put((int) rta.getType()&0xffff, rta);
        }

    }

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append(getClass().getSimpleName()).append(' ');
        s.append("nlmg[size:").append(nlmsg_len).append(",type:").append(nlmsg_type).append(",flags:0x").append(Integer.toHexString(nlmsg_flags)).append("]\n");
        return s.toString();
    }

}
