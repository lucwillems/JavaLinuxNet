package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class neighbourMsg extends NlMessage {
      byte ndm_family;
      byte ndm_pad1;
      byte ndm_pad2;
      byte ndm_ifindex;
      byte ndm_state;
      byte ndm_flags;
      byte ndm_type;

    public neighbourMsg(ByteBuffer msg) {
        super(msg);
        ndm_family=msg.get();
        ndm_pad1=msg.get();
        ndm_pad2=msg.get();
        ndm_ifindex=msg.get();
        ndm_state=msg.get();
        ndm_flags=msg.get();
        ndm_type=msg.get();
        msg.get();
        msg.getInt();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(String name) {
        for (int i=0;i< libnetlink.linux.if_neighbour.RTA_NAMES.length;i++) {
            if (name.equals(libnetlink.linux.if_neighbour.RTA_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(int position, ByteBuffer msg) {
        return new neighbourRTAMessages(position,msg);
    }

    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("fam: ").append(ndm_family);
        s.append(" ifindex:").append(ndm_ifindex);
        s.append(" state:0x").append(Integer.toHexString(((int)ndm_state)&0xff));
        s.append(" flags:0x").append(Integer.toHexString(((int)ndm_state)&0xff));
        s.append(" type:0x").append(Integer.toHexString(((int) ndm_type) & 0xff));
        s.append("\n");
        //dump rta messages
        for (RTAMessage r: rtaMessages.values()) {
             if (r.getType() == libnetlink.linux.if_neighbour.NDA_DST) {
                s.append(" ").append(r.toString()).append(" ").append(r.getInetAddress()).append("\n");
             } else {
                s.append(" ").append(r.toString()).append("\n");
             }
        }
        return s.toString();
    }
}
