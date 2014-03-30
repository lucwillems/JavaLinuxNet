/*
 * Copyright 2014 Luc Willems (T.M.M.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

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
    public void testIntegerKey() throws Exception {
        IndexNameMap<Integer,String> test=new IndexNameMap<Integer, String>();
        Assert.assertNotNull(test);
        //get a index not existing should return the  text with [<number]
        Assert.assertEquals("[1]",test.get(1));
        String x=test.put(new Integer(1),"DemoTestApp");
        Assert.assertEquals("DemoTestApp",test.get(1));
        test.remove(new Integer(1));
        Assert.assertEquals("[1]",test.get(1));
    }

    @Test
    public void testStringKey() throws Exception {
        IndexNameMap<String,String> test=new IndexNameMap<String, String>();
        Assert.assertNotNull(test);
        String index="hello";
        //get a index not existing should return the  text with [<number]
        Assert.assertEquals("[hello]",test.get(index));
        String x=test.put(index,"DemoTestApp");
        Assert.assertEquals("DemoTestApp",test.get(index));
        test.remove(index);
        Assert.assertEquals("[hello]",test.get(index));
    }

}
