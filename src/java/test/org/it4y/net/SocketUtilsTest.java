package org.it4y.net;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by luc on 1/8/14.
 */

public class SocketUtilsTest {
    private Logger log= LoggerFactory.getLogger(SocketUtilsTest.class);
    @Test
    public void testSocketFd() throws Exception {
        //create client socket and get access to fd
        Socket s= SocketFactory.getDefault().createSocket();
        int fd=SocketUtils.getFd(s);
        log.info("fd client socket: {}",fd);
        Assert.assertTrue("FD must be > 0",fd>0);
        s.close();
    }

    @Test
    public void testServerSocketFd() throws Exception {
        //create Server socket and get access to fd
        ServerSocket s= ServerSocketFactory.getDefault().createServerSocket();
        int fd=SocketUtils.getFd(s);
        log.info("fd server socket: {}",fd);
        Assert.assertTrue("FD must be > 0", fd > 0);
        s.close();
    }

    @Test
    public void testFileOutputStreamFd() throws Exception {
        FileOutputStream fo=new FileOutputStream("/tmp/test");
        int fd=SocketUtils.getFd(fo);
        log.info("fd file outputstream: {}",fd);
        Assert.assertTrue("FD must be > 0", fd > 0);
        fo.close();

    }
    @Test
    public void testFileInputStreamFd() throws Exception {
        FileInputStream fi=new FileInputStream("/proc/sys/net/ipv4/ip_forward");
        int fd=SocketUtils.getFd(fi);
        log.info("fd file inputstream: {}",fd);
        Assert.assertTrue("FD must be > 0", fd > 0);
        fi.close();

    }
    @Test
    public void testRandomAccessFileFd() throws Exception {
        RandomAccessFile random=new RandomAccessFile("/proc/sys/net/ipv4/ip_forward","r");
        int fd=SocketUtils.getFd(random);
        log.info("fd random access file: {}",fd);
        Assert.assertTrue("FD must be > 0", fd > 0);
        random.close();

    }

}
