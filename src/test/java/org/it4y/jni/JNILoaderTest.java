package org.it4y.jni;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by luc on 3/24/14.
 */
public class JNILoaderTest {
    Logger logger = LoggerFactory.getLogger(JNILoader.class);

    @Test
    public void testJNILoaderWithCustomPath() {

        File tmpDir=new File("/tmp/luc/test");
        System.setProperty(JNILoader.customPathKEY,tmpDir.getAbsolutePath());
        JNILoader.loadLibrary("libjnituntap.so");
        //Directory must exist
        Assert.assertTrue(tmpDir.exists());
        File soFile=new File(tmpDir,"libjnituntap.so");
        Assert.assertTrue(soFile.exists());
        Assert.assertTrue(soFile.length()>0);
    }

    @Test
    public void testJNILoaderLoadNonExisting() {

        File tmpDir=new File("/tmp/luc/test");
        System.getProperties().put(JNILoader.customPathKEY,tmpDir.getAbsoluteFile());
        try {
            JNILoader.loadLibrary("shithappens");
        } catch (RuntimeException re) {
            logger.info("Got RuntimeException (OK): {}",re.getMessage());
            return;
        }
        Assert.assertTrue("We must have RuntimeException",false);
    }

}
