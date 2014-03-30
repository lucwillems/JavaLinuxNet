/*
 * Copyright 20124 Luc Willems (T.M.M.)
 *
 * We licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luc on 12/28/13.
 * Use this class as for loading of .so files from any class implementing native methods
 *
 * The loader will first look into /usr/lib . if not found it will look into jar which include this class.
 * If found , it will copy the .so file to /tmp directory and load from there.
 *
 * We will remove any file retrieved from JAR on exist, just to make sure we have the latest version
 *
 *
 */
public class JNILoader {
    public  static String customPathKEY="JNILoader.tmpPath";
    private static String tmpPath = "/tmp/linux-net";

    //Predefined location of librabry path.
    private static String[] libpath = new String[]{"/usr/lib","/usr/lib/jlinux-net"};

    public static String libraryArchFileName(String lib) {
        final String arch=System.getProperty("os.arch");
        return lib+"-"+arch+".so";
    }
    /**
     * Load .so library from different location.
     * @param lib the library (filename only !!!)
     */
    public static void loadLibrary(final String lib) {
        final String libfname=libraryArchFileName(lib);
        final Logger log = LoggerFactory.getLogger(JNILoader.class);
        log.info("load {}",libfname);
        //Try loading from lib path
        for (final String path : libpath) {
            final File f = new File(path + '/' + libfname);
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
        File targetFile = new File(targetFolder, libfname);
        //cleanup any old file
        if (targetFile.exists()) {
            log.info("cleanup old {}", targetFile);
            if(!targetFile.delete()) {
                //should never happen but you never know
                log.warn("Could not delete old {}",targetFile);
            };
        }
        targetFile.deleteOnExit();

        // extract file into the current directory
        InputStream reader = JNILoader.class.getResourceAsStream("/" + libfname);
        FileOutputStream writer=null;
        if (reader != null) {
            try {
                writer = new FileOutputStream(targetFile);
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }
                //load native lib
                System.load(targetFile.getAbsolutePath());
                log.info("native lib loaded: {}", targetFile);
                return;
                //
            } catch (IOException io) {
                log.error("Fatal IO error : {}", io.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignore) {
                        log.warn("could not close resource {}",lib);
                    }
                }
                if (writer != null ) {
                    try {
                        writer.close();
                    } catch (IOException ignore) {
                        log.warn("could not close {}",targetFile);
                    }
                }
            }
        } else {
            log.warn("lib {} ({}) not found in jar", lib,libfname);
        }

        //did we got a error or just not found ?
        //we can not continue here
        log.error("FATAL : unable to load native lib {}",libfname);
        throw new RuntimeException("No library loaded: " + libfname);
    }

    /**
     *
     * @return Directory of tmp file for .so loading
     */
    private static String getTargetFolder() {
        final Logger log = LoggerFactory.getLogger(JNILoader.class);
        File tmpDir=null;
        if (System.getProperty(customPathKEY) != null) {
            tmpDir=new File(System.getProperty(customPathKEY));
            log.info("using custom tmp {}",tmpDir);

        } else if (System.getProperty("java.io.tmpdir") != null) {
            tmpDir=new File(System.getProperty("java.io.tmpdir")+"/linux-net");
            log.info("using java tmp {}",tmpDir);
        } else {
            tmpDir = new File("/tmp/linux-net");
            log.info("using tmp {}",tmpDir);
        }
        if (!tmpDir.exists())
            tmpDir.mkdirs();
        return tmpDir.getAbsolutePath();
    }
}
