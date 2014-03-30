/*
 *
 *  * Copyright (C) 2014  Luc.Willems @ T.M.M.
 *  *
 *  * This file is released under the LGPL.
 *  * see license.txt for terms and conditions
 *
 */

package org.it4y.net.tproxy;

import org.it4y.jni.SocketOptions;
import org.it4y.jni.libc;
import org.it4y.jni.linuxutils;
import org.it4y.jni.tproxy;
import org.it4y.net.JVMException;
import org.it4y.net.SocketUtils;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


/**
 * Created by luc on 12/27/13.
 * You need this on linux to run it as normal user
 * setcap "cap_net_raw=+eip cap_net_admin=+eip" /usr/lib/jvm/java-1.7.0/bin/java
 * replace java executable path with your java and use that java
 * Note: after upgrade/changing this file you need to repeat this
 */
public class TProxyServerSocket extends ServerSocket {

    /*
     * This fields are manipulated by native c code so don't change it !!!
     */
    public TProxyServerSocket() throws IOException {
    }

    public FileDescriptor getFileDescriptor() throws InvocationTargetException, IllegalAccessException {
        return SocketUtils.getFileDescriptor(this);
    }

    public int getFd() throws JVMException {
        return SocketUtils.getFd(this);
    }

    public void setIPTransparentOption() throws libc.ErrnoException {
        linuxutils.setbooleanSockOption(this, SocketOptions.SOL_IP, SocketOptions.IP_TRANSPARENT, true);
    }

    public void initTProxy(final InetAddress address, final int port, final int backlog) throws libc.ErrnoException,IOException {
        setIPTransparentOption();
        setReuseAddress(true);
        //bind to localhost interface
        final InetSocketAddress local = new InetSocketAddress(address, port);
        bind(local, backlog);
    }

    public TProxyInterceptedSocket accepProxy() throws IOException,libc.ErrnoException {
        final tproxy proxy = new tproxy();
        final Socket c = accept();
        //get original destination address stored in client socket structure
        final libc.sockaddr_in remote = linuxutils.getsockname(c);
        return new TProxyInterceptedSocket(c, remote);
    }

}
