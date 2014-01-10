/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.netlink;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by luc on 1/2/14.
 */
public abstract class NlMessage {


    //Read nlmsg header
    protected int nlmsg_len;
    protected short nlmsg_type;
    protected short nlmsg_flags;
    protected int nlmsg_seq;
    protected int nlmsg_pid;
    protected HashMap<Integer, RTAMessage> rtaMessages;

    public NlMessage(ByteBuffer msg) {
        //Read nlmsg header
        this.nlmsg_len = msg.getInt();
        this.nlmsg_type = msg.getShort();
        this.nlmsg_flags = msg.getShort();
        this.nlmsg_seq = msg.getInt();
        this.nlmsg_pid = msg.getInt();
        rtaMessages = new HashMap<Integer, RTAMessage>();
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

    public RTAMessage getRTAMessage(int index) {
        return rtaMessages.get(index);
    }

    public boolean moreMessages() {
        return (nlmsg_flags & (short) 0x02) > 0;
    }

    public RTAMessage getRTAMessage(String name) {
        return getRTAMessage(getRTAIndex(name));
    }

    public abstract int getRTAIndex(String name);

    public abstract RTAMessage createRTAMessage(int position, ByteBuffer msg);

    protected void parseRTAMessages(ByteBuffer msg) {
        while (msg.position() < nlmsg_len) {
            int position = msg.position();
            RTAMessage rta = createRTAMessage(position, msg);
            msg.position(position + rta.getPaddedSize());
            rtaMessages.put(new Integer(rta.getType()), rta);
        }

    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(this.getClass().getSimpleName()).append(" ");
        s.append("nlmg[size:").append(nlmsg_len).append(",type:").append(nlmsg_type).append(",flags:0x").append(Integer.toHexString(nlmsg_flags)).append("]\n");
        return s.toString();
    }

}
