package org.it4y.jni.linux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/9/14.
 */
public class rtnetlinkTest {

    @Test
    public void ifrtnetlinkRTANamesTest() {
        Assert.assertNotNull(rtnetlink.RTA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", rtnetlink.RTA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("dst",rtnetlink.RTA_NAMES.get(rtnetlink.RTA_DST));
    }

    @Test
    public void ifrtnetlinkRTNNamesTest() {
        Assert.assertNotNull(rtnetlink.RTN_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", rtnetlink.RTN_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("anycast",rtnetlink.RTN_NAMES.get(rtnetlink.RTN_ANYCAST));
    }

}
