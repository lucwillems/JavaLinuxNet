package org.it4y.jni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 */
public class JNILoader {
    static String[] libpath = new String[]{"nlib", "/usr/lib"};
    /**
     * Load a libary from well known locations
     */
    public static void loadLibrary(String... libs) {
        final Logger log= LoggerFactory.getLogger(JNILoader.class);
        Throwable e = null;
        for (String lib : libs) {
            try {
                for (String path : libpath) {
                    File f = new File(path + "/" + lib);
                    if (f.exists()) {
                        System.load(f.getCanonicalPath());
                        log.info("native lib loaded: " + f.getCanonicalFile());
                        return;
                    } else {
                        log.debug("{} not found",f.getCanonicalFile());
                    }
                }
            } catch (Throwable eio) {
                log.debug("load error: {}",e,e);
                e = eio;
            }
        }
        if (e != null)
            throw new RuntimeException("failed loading native library", e);
    }

}
