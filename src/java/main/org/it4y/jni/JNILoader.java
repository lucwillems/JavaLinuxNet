package org.it4y.jni;

import java.io.File;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 *
 */
public class JNILoader {
    static String[] libpath = new String[] { "nlib","/usr/lib" };

    /**
     * Load a libary from well known locations
     */
    public static void loadLibrary(String... libs) {
        Throwable e=null;
        for(String lib : libs) {
            try {
                for (String path : libpath) {
                    File f=new File(path+"/"+lib);
                    if (f.exists()) {
                        System.load(f.getCanonicalPath());
                        System.err.println("loaded "+f);
                        return;
                    } else {
                        System.out.println(f.getCanonicalFile()+" not found...");
                    }
                }
            } catch (Throwable eio) {e = eio;}
        }
        if (e!=null)
            throw new RuntimeException("load library failed:",e);
    }

}
