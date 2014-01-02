package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class interfaceInfoMsg extends  NlMessage {

    byte  ifi_family;
    short ifi_type;
    int   ifi_index;
    int   ifi_flags;
    int   ifi_changed;

    public interfaceInfoMsg(ByteBuffer msg) {
        super(msg);
        //get ifinfomsg header
        msg.get(); //dummy byte
        ifi_family=msg.get();
        ifi_type=msg.getShort();
        ifi_index=msg.getInt();
        ifi_flags=msg.getInt();
        ifi_changed=msg.getInt();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(String name) {
        for (int i=0;i<libnetlink.linux.if_link.RTA_NAMES.length;i++) {
            if (name.equals(libnetlink.linux.if_link.RTA_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(int position, ByteBuffer msg) {
        return new InterfaceRTAMessages(position,msg);
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("fam: ").append(ifi_family);
        s.append(" type:0x").append(Integer.toHexString(ifi_type));
        s.append(" idx:").append(ifi_index);
        s.append(" flags:0x").append(Integer.toHexString(ifi_flags));
        s.append(" changed:0x").append(Integer.toHexString(ifi_changed));
        s.append("\n");
        //dump rta messages
        for (RTAMessage r: rtaMessages.values()) {
            if (r.getType() == 3) {
              s.append(" ").append(r.toString()).append(" ").append(r.getString()).append("\n");
            } else {
              s.append(" ").append(r.toString()).append("\n");
            }
        }
        return s.toString();
    }
}
