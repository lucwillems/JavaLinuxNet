package org.it4y.net.netlink;

import org.it4y.jni.libnetlink;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class routeMsg extends NlMessage {

    private byte rtm_family;
    private byte rtm_dst_len;
    private byte rtm_src_len;
    private byte rtm_tos;
    private byte rtm_table;
    private byte rtm_protocol;
    private byte rtm_scope;
    private byte rtm_type;
    private int rtm_flags;

    public routeMsg(ByteBuffer msg) {
        super(msg);
        this.rtm_family=msg.get();
        this.rtm_dst_len=msg.get();
        this.rtm_src_len=msg.get();
        this.rtm_tos=msg.get();
        this.rtm_table=msg.get();
        this.rtm_protocol=msg.get();
        this.rtm_scope=msg.get();
        this.rtm_type=msg.get();
        this.rtm_flags=msg.getInt();
        parseRTAMessages(msg);
    }

    @Override
    public int getRTAIndex(String name) {
        for (int i=0;i< libnetlink.linux.rtnetlink.RTA_NAMES.length;i++) {
            if (name.equals(libnetlink.linux.rtnetlink.RTA_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public RTAMessage createRTAMessage(int position, ByteBuffer msg) {
        return new routeRTAMessages(position,msg);
    }

    @Override
    public String toString() {
        StringBuffer s=new StringBuffer();
        s.append(super.toString());
        s.append("fam:").append(rtm_family);
        s.append(" dst_len:/").append(rtm_dst_len);
        s.append(" sr_len:/").append(rtm_src_len);
        s.append(" tos:0x").append(Integer.toHexString(rtm_tos));
        s.append(" table:").append(((short)rtm_table)&0xff);
        s.append(" prot:");
        switch(((int)rtm_protocol)&0xff) {
            case libnetlink.linux.rtnetlink.RTPROT_UNSPEC:s.append("unspec");break;
            case libnetlink.linux.rtnetlink.RTPROT_REDIRECT:s.append("redirect");break;
            case libnetlink.linux.rtnetlink.RTPROT_KERNEL:s.append("kernel");break;
            case libnetlink.linux.rtnetlink.RTPROT_BOOT:s.append("boot");break;
            case libnetlink.linux.rtnetlink.RTPROT_STATIC:s.append("static");break;
            default : s.append(((short)rtm_protocol)&0xff);
        }
        s.append(" scope:");
        switch(((int)rtm_scope)&0xff) {
            case libnetlink.linux.rtnetlink.RT_SCOPE_UNIVERSE:s.append("universe");break;
            case libnetlink.linux.rtnetlink.RT_SCOPE_SITE:s.append("site");break;
            case libnetlink.linux.rtnetlink.RT_SCOPE_LINK:s.append("link");break;
            case libnetlink.linux.rtnetlink.RT_SCOPE_HOST:s.append("host");break;
            case libnetlink.linux.rtnetlink.RT_SCOPE_NOWHERE:s.append("hell");break;
            default : s.append(((short)rtm_scope)&0xff);
        }
        //s.append(" type:").append(((short)rtm_type)&0xff);
        s.append(" type:").append(libnetlink.linux.rtnetlink.RTTYPE_NAMES[rtm_type]);
        s.append(" flags:").append(Integer.toHexString(rtm_flags));
        s.append("\n");
        //dump rta messages
        for (RTAMessage r: rtaMessages.values()) {
             if (r.getType() == libnetlink.linux.rtnetlink.RTA_DST ||
                 r.getType() == libnetlink.linux.rtnetlink.RTA_SRC ||
                 r.getType() == libnetlink.linux.rtnetlink.RTA_GATEWA
                 ) {
                s.append(" ").append(r.toString()).append(" ").append(r.getInetAddress()).append("\n");
             } else {
                s.append(" ").append(r.toString()).append("\n");
             }
        }
        return s.toString();
    }
}
