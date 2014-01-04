package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class interfaceAddressMsg extends NlMessage {
    byte ifa_family;
    byte ifa_prefixlen;  /* The prefix length            */
    byte ifa_flags;      /* Flags                        */
    byte ifa_scope;      /* Address scope                */
    byte ifa_index;      /* Link index                   */

    public interfaceAddressMsg(ByteBuffer msg) {
        super(msg);
        //dummy
        ifa_family = msg.get();
        ifa_prefixlen = msg.get();
        ifa_flags = msg.get();
        ifa_scope = msg.get();
        ifa_index = msg.get();
        //padding
        msg.get();
        msg.get();
        msg.get();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(String name) {
        for (int i = 0; i < libnetlink.linux.if_address.RTA_NAMES.length; i++) {
            if (name.equals(libnetlink.linux.if_address.RTA_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(int position, ByteBuffer msg) {
        return new AddressRTAMessages(position, msg);
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString());
        s.append("fam: ").append(ifa_family);
        s.append(" flags:0x").append(Integer.toHexString(((int) ifa_flags) & 0xff));
        s.append(" idx:").append(ifa_index);
        s.append(" scope:0x").append(Integer.toHexString(((int) ifa_scope) & 0xff));
        s.append(" prefixlen:").append(ifa_prefixlen);
        s.append("\n");
        //dump rta messages
        for (RTAMessage r : rtaMessages.values()) {
            if (r.getType() == libnetlink.linux.if_address.IFA_LABEL) {
                s.append(" ").append(r.toString()).append(" ").append(r.getString()).append("\n");
            } else if (r.getType() == libnetlink.linux.if_address.IFA_BROADCAST) {
                s.append(" ").append(r.toString()).append(" ").append(r.getInetAddress()).append("\n");
            } else if (r.getType() == libnetlink.linux.if_address.IFA_ADDRESS) {
                s.append(" ").append(r.toString()).append(" ").append(r.getInetAddress()).append("\n");
            } else {
                s.append(" ").append(r.toString()).append("\n");
            }
        }
        return s.toString();
    }

    public byte getAddresFamily() {
        return ifa_family;
    }

    public byte getAddressPrefixlen() {
        return ifa_prefixlen;
    }

    public byte getAddressFlags() {
        return ifa_flags;
    }

    public byte getAddressScope() {
        return ifa_scope;
    }

    public byte getInterfaceIndex() {
        return ifa_index;
    }

}
