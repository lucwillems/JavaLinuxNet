package org.it4y.jni.linux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/8/14.
 */
public class if_arpTest {
    @Test
    public void ifarpNamesTest() {
        Assert.assertNotNull(if_arp.ARPHDR_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", if_arp.ARPHDR_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("appletalk",if_arp.ARPHDR_NAMES.get(if_arp.ARPHRD_APPLETLK));
    }

}
