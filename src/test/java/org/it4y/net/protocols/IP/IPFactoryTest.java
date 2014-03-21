package org.it4y.net.protocols.IP;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by luc on 3/21/14.
 */
public class IPFactoryTest {
    @Test
    public void testProcessRawPacket() throws Exception {
        Object result=IPFactory.processRawPacket(null,0);
        Assert.assertNull(result);
    }
}
