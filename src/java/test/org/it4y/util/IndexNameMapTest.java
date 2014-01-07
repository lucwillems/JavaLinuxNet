package org.it4y.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by luc on 1/7/14.
 */
public class IndexNameMapTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetIntegerKey() throws Exception {
        IndexNameMap<Integer,String> test=new IndexNameMap<Integer, String>();
        Assert.assertNotNull(test);
        //get a index not existing should return the  text with [<number]
        Assert.assertEquals("[1]",test.get(1));
        String x=test.put(new Integer(1),"test");
        Assert.assertEquals("test",test.get(1));
        test.remove(new Integer(1));
        Assert.assertEquals("[1]",test.get(1));
    }

    @Test
    public void testGetStringKey() throws Exception {
        IndexNameMap<String,String> test=new IndexNameMap<String, String>();
        Assert.assertNotNull(test);
        String index="hello";
        //get a index not existing should return the  text with [<number]
        Assert.assertEquals("[hello]",test.get(index));
        String x=test.put(index,"test");
        Assert.assertEquals("test",test.get(index));
        test.remove(index);
        Assert.assertEquals("[hello]",test.get(index));
    }

}
