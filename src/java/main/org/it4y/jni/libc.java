package org.it4y.jni;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by luc on 12/30/13.
 */

public final class libc {

    /*
     * this class wraps libc structure from C lib
     *
     */
    public static class sockaddr_in {
        public int family;
        public int port;
        public int address;

        //cache this result as it is expensive
        private InetAddress cached_remote_address=null;

        public sockaddr_in() {}

        public InetAddress getRemoteAddres() {
            if (cached_remote_address==null) {
                synchronized (this) {
                    if (cached_remote_address==null) {
                        /* ok java uses shitty constructions so we need to hack here */
                        /* Inet4Address.getByAddress() will do reverse of this. */
                        /* a private constructor(int address,int port) exist but it is private */
                        byte[] ab=new byte[4];
                        ab[0]=(byte)((address >> 24) & 0x00ff);
                        ab[1]=(byte)((address >> 16) & 0x00ff);
                        ab[2]=(byte)((address >> 8) & 0x00ff);
                        ab[3]=(byte)(address& 0x00ff);
                        //Why throw this will it is not going to happen ?
                        try {
                            cached_remote_address=Inet4Address.getByAddress(ab);
                        } catch(UnknownHostException ignore) {}
                    }
                }
            }
            return cached_remote_address;
        }

        public InetSocketAddress toInetSocketAddress() {
            return new InetSocketAddress(getRemoteAddres(),port);
        }
        public String toString() {
            return Integer.toHexString(address)+":"+port;
        }


    }

    /*
     * Throw errno results with exceptions
     */
    public static class ErrnoException extends Exception {

        private int errno;
        public ErrnoException(int errno) {
            super();
            this.errno=errno;
        }
        public ErrnoException(String message,int errno) {
            super(message);
            this.errno=errno;
        }

        public int getErrno() {
           return this.errno;
        }
        public String toString(){ return this.getMessage()+" errno: "+Integer.toString(errno); }
    }

}
