package org.it4y.jni;

import java.io.File;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 *
 */
public class JNILoader {
    /**
     * Load a libary from well known locations
     */
    public static void loadLibrary(String... libs) {
        Throwable e=null;
        for(String lib : libs) {
            try {
                System.load(new File(lib).getCanonicalPath());
                break;
            } catch (Throwable eio) {e = eio;}
        }
        if (e!=null)
            throw new RuntimeException("load library failed:",e);
    }

}
