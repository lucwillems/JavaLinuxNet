/*
 * This file is derived from Linux kernel headers
 * All copyrights preserved to original kernel author.
 *
 * more info see https://www.kernel.org/pub/linux/kernel/COPYING
 */
package org.it4y.jni.linux;

/**
 * Created by luc on 1/10/14.
 */
//from include<bits/socket.h>
public final class socket {
    public final static short AF_UNSPEC=0;
    public final static short AF_LOCAL=1;
    public final static short AF_UNIX=AF_LOCAL;
    public final static short AF_FILE=AF_LOCAL;
    public final static short AF_INET=2;
    public final static short AF_INET6=10;

}
