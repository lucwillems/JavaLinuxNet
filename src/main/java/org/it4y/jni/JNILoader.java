package org.it4y.jni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 */
public class JNILoader {
    static String tmpPath = "/tmp/linux-net";
    static String[] libpath = new String[]{"/usr/lib", tmpPath};

    /**
     * Load a libary from well known locations
     */
    public static void loadLibrary(final String lib) {
        final Logger log = LoggerFactory.getLogger(JNILoader.class);
        //Try loading from lib path
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
            } catch (final IOException io) {
                log.error("IO issue ", io);
            }
        }

        //try loading from JAR resource stream
        String targetFolder = getTargetFolder();
        File targetFile = new File(targetFolder, lib);
        //cleanup any old file
        if (targetFile.exists()) {
            log.info("cleanup old {}", targetFile);
            targetFile.delete();
        }
        targetFile.deleteOnExit();

        // extract file into the current directory
        InputStream reader = JNILoader.class.getResourceAsStream("/" + lib);
        if (reader != null) {
            try {
                FileOutputStream writer = new FileOutputStream(targetFile);
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }
                writer.close();
                reader.close();
                //load native lib
                System.load(targetFile.getAbsolutePath());
                log.info("native lib loaded: {}", targetFile);
                return;
                //
            } catch (IOException io) {
                log.error("Fatal IO error : {}", io.getMessage());
            }
        } else {
            log.warn("lib {} not found in jar", lib);
        }
        //did we got a error or just not found ?
        throw new RuntimeException("No library loaded: " + lib);
    }

    private static String getTargetFolder() {
        File dir = new File("/tmp/linux-net");
        dir.deleteOnExit();
        if (!dir.exists())
            dir.mkdirs();
        return dir.getAbsolutePath();
    }
}
