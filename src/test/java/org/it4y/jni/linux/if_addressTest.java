package org.it4y.jni.linux;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by luc on 1/8/14.
 */
public class if_addressTest {

    @Test
    public void ifAddressNamesTest() {
        Assert.assertNotNull(if_address.IFA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("[" + Integer.MAX_VALUE + "]", if_address.IFA_NAMES.get(Integer.MAX_VALUE));
        Assert.assertEquals("address",if_address.IFA_NAMES.get(if_address.IFA_ADDRESS));
    }
}
