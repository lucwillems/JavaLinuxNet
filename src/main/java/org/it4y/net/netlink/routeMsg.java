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

package org.it4y.net.netlink;

import org.it4y.jni.linux.rtnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class routeMsg extends NlMessage {

    private final byte rtm_family;
    private final byte rtm_dst_len;
    private final byte rtm_src_len;
    private final byte rtm_tos;
    private final byte rtm_table;
    private final byte rtm_protocol;
    private final byte rtm_scope;
    private final byte rtm_type;
    private final int rtm_flags;

    public routeMsg(final ByteBuffer msg) {
        super(msg);
        rtm_family = msg.get();
        rtm_dst_len = msg.get();
        rtm_src_len = msg.get();
        rtm_tos = msg.get();
        rtm_table = msg.get();
        rtm_protocol = msg.get();
        rtm_scope = msg.get();
        rtm_type = msg.get();
        rtm_flags = msg.getInt();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(final String name) {
        for (final int i : rtnetlink.RTA_NAMES.keySet()) {
            if (name.equals(rtnetlink.RTA_NAMES.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected RTAMessage createRTAMessage(final int position, final ByteBuffer msg) {
        return new routeRTAMessages(position, msg);
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append(super.toString());
        s.append("fam:").append(rtm_family);
        s.append(" dst_len:/").append(rtm_dst_len);
        s.append(" sr_len:/").append(rtm_src_len);
        s.append(" tos:0x").append(Integer.toHexString(rtm_tos));
        s.append(" table:").append((short)rtm_table & 0xff);
        s.append(" prot:");
        switch ((int)rtm_protocol & 0xff) {
            case rtnetlink.RTPROT_UNSPEC:
                s.append("unspec");
                break;
            case rtnetlink.RTPROT_REDIRECT:
                s.append("redirect");
                break;
            case rtnetlink.RTPROT_KERNEL:
                s.append("kernel");
                break;
            case rtnetlink.RTPROT_BOOT:
                s.append("boot");
                break;
            case rtnetlink.RTPROT_STATIC:
                s.append("static");
                break;
            default:
                s.append((short)rtm_protocol & 0xff);
        }
        s.append(" scope:");
        switch ((int)rtm_scope & 0xff) {
            case rtnetlink.RT_SCOPE_UNIVERSE:
                s.append("universe");
                break;
            case rtnetlink.RT_SCOPE_SITE:
                s.append("site");
                break;
            case rtnetlink.RT_SCOPE_LINK:
                s.append("link");
                break;
            case rtnetlink.RT_SCOPE_HOST:
                s.append("host");
                break;
            case rtnetlink.RT_SCOPE_NOWHERE:
                s.append("hell");
                break;
            default:
                s.append((short) rtm_scope & 0xff);
        }
        //s.append(" type:").append(((short)rtm_type)&0xff);
        s.append(" type:").append(rtnetlink.RTN_NAMES.get((int)rtm_type & 0xffff));
        s.append(" flags:").append(Integer.toHexString(rtm_flags));
        s.append('\n');
        //dump rta messages
        for (final RTAMessage r : rtaMessages.values()) {
            if (r.getType() == rtnetlink.RTA_DST ||
                    r.getType() ==rtnetlink.RTA_SRC ||
                    r.getType() == rtnetlink.RTA_GATEWA
                    ) {
                s.append(' ').append(r.toString()).append(' ').append(r.getInetAddress()).append('\n');
            } else {
                s.append(' ').append(r.toString()).append('\n');
            }
        }
        return s.toString();
    }

    public byte getRouteFamily() {
        return rtm_family;
    }

    public byte getRouteDestLen() {
        return rtm_dst_len;
    }

    public byte getRouteSourceLen() {
        return rtm_src_len;
    }

    public byte getRouteTos() {
        return rtm_tos;
    }

    public byte getRouteTable() {
        return rtm_table;
    }

    public byte getRouteProtocol() {
        return rtm_protocol;
    }

    public byte getRouteScope() {
        return rtm_scope;
    }

    public byte getRouteType() {
        return rtm_type;
    }

    public int getRouteFlags() {
        return rtm_flags;
    }
}
