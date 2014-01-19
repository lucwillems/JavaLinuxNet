/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.netlink;

import org.it4y.jni.linux.if_arp;
import org.it4y.jni.linux.if_link;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class interfaceInfoMsg extends NlMessage {

    byte ifi_family;
    short ifi_type;
    int ifi_index;
    int ifi_flags;
    int ifi_changed;

    public interfaceInfoMsg(final ByteBuffer msg) {
        super(msg);
        //get ifinfomsg header
        msg.get(); //dummy byte
        ifi_family = msg.get();
        ifi_type = msg.getShort();
        ifi_index = msg.getInt();
        ifi_flags = msg.getInt();
        ifi_changed = msg.getInt();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(final String name) {
        for (final int i : if_link.IFLA_NAMES.keySet()) {
            if (name.equals(if_link.IFLA_NAMES.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(final int position, final ByteBuffer msg) {
        return new InterfaceRTAMessages(position, msg);
    }

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append(super.toString());
        s.append("fam: ").append(ifi_family);
        s.append(" type:").append(if_arp.ARPHDR_NAMES.get((int)ifi_type&0xffff));
        s.append(" idx:").append(ifi_index);
        s.append(" flags:0x").append(Integer.toHexString(ifi_flags));
        s.append(" changed:0x").append(Integer.toHexString(ifi_changed));
        s.append('\n');
        //dump rta messages
        for (final RTAMessage r : rtaMessages.values()) {
            if (r.getType() == 3) {
                s.append(' ').append(r.toString()).append(' ').append(r.getString()).append('\n');
            } else {
                s.append(' ').append(r.toString()).append('\n');
            }
        }
        return s.toString();
    }

    public byte getInterfaceFamily() {
        return ifi_family;
    }

    public short getInterfaceType() {
        return ifi_type;
    }

    public int getInterfaceIndex() {
        return ifi_index;
    }

    public int getInterfaceFlags() {
        return ifi_flags;
    }

    public int getInterfaceChanged() {
        return ifi_changed;
    }


}

