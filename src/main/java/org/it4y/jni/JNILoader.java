package org.it4y.jni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 */
public class JNILoader {
    static String[] libpath = new String[]{"nlib", "/usr/lib"};

    /**
     * Load a libary from well known locations
     */
    public static void loadLibrary(final String lib) {
        final Logger log = LoggerFactory.getLogger(JNILoader.class);
        for (final String path : libpath) {
            final File f = new File(path + '/' + lib);
            try {
            final String fname = f.getCanonicalPath();
            if (f.exists()) {
                System.load(fname);
                log.info("native lib loaded: {}", fname);
                return;
            } else {
                log.debug("{} not found", fname);
            }
            }catch(final IOException io) {
                log.error("IO issue ",io);
            }
        }
        //did we got a error  or just not found ?
        throw new RuntimeException("No library loaded: " + lib);
    }

}
