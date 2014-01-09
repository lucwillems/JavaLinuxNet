package org.it4y.jni.linux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/9/14.
 */
public class if_linkTest {
    @Test
    public void iflinkNamesTest() {
        Assert.assertNotNull(if_link.IFLA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", if_link.IFLA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("address",if_link.IFLA_NAMES.get(if_link.IFLA_ADDRESS));
    }

}
