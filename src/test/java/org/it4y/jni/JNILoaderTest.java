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

package org.it4y.jni;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by luc on 3/24/14.
 */
public class JNILoaderTest {
    Logger logger = LoggerFactory.getLogger(JNILoader.class);
    private File tmpDir;

    @Before
    public void initTest() {
        //Setup System properties for this test.
        //As we could run both test into 1 VM, we should "try" to cleanup before test
        tmpDir=new File("/tmp/luc/test");
        //Setup System properties
        System.getProperties().remove(JNILoader.customPathKEY); //remove any "old" stuff
        System.setProperty(JNILoader.customPathKEY,tmpDir.getAbsolutePath());
    }

    @Test
    public void testJNILoaderWithCustomPath() {
        JNILoader.loadLibrary("libjnituntap");
        //Directory must exist
        Assert.assertTrue(tmpDir.exists());
        File soFile=new File(tmpDir,JNILoader.libraryArchFileName("libjnituntap"));
        Assert.assertTrue(soFile.exists());
        Assert.assertTrue(soFile.length()>0);
    }

    @Test
    public void testJNILoaderLoadNonExisting() {
        try {
            JNILoader.loadLibrary("shithappens");
        } catch (RuntimeException re) {
            logger.info("Got RuntimeException (OK): {}",re.getMessage());
            return;
        }
        Assert.assertTrue("We must have RuntimeException",false);
    }

}
