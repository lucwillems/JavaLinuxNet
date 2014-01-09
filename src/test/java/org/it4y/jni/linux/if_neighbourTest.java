package org.it4y.jni.linux;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luc on 1/9/14.
 */
public class if_neighbourTest {

    @Test
    public void iflinkNamesTest() {
        Assert.assertNotNull(if_neighbour.NDA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", if_neighbour.NDA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("port",if_neighbour.NDA_NAMES.get(if_neighbour.NDA_PORT));
    }

}
