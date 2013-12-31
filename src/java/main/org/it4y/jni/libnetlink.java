package org.it4y.jni;

/**
 * Created by luc on 12/31/13.
 */
public class libnetlink {

    public static class rtnl_handle {
        public static final int SIZE=36;
        public byte[] handle=new byte[SIZE];

        public rtnl_handle() {
        }
    }
}
