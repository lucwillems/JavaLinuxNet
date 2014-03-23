package org.it4y.jni;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 3/23/14.
 */
public class SocketOptionsTest {

    @Test
    public void testSocketOptions() {
        //nothing really usefull ?
        Assert.assertEquals(0,SocketOptions.SOL_IP);
        Assert.assertEquals(6,SocketOptions.SOL_TCP);
        Assert.assertEquals(17,SocketOptions.SOL_UDP);
    }
}
