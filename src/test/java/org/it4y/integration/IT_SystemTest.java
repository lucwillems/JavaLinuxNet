package org.it4y.integration;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Created by luc on 3/21/14.
 */
public class IT_SystemTest {
    Logger log= LoggerFactory.getLogger(IT_SystemTest.class);
    @Test
    public void testDumpSystemInfo() throws Exception {
        //Just dump system information
        for (Object key:System.getProperties().keySet()) {
            log.info(" property {}: {}",key,System.getProperty(key.toString()));
        }

        //Read our environment
        //Just dump system information
        for (Object key:System.getenv().keySet()) {
            log.info("env {}: {}",key,System.getenv(key.toString()));
        }

        //Read how we are executed
        //read 0 terminated string
        Scanner scanner = new Scanner(new FileInputStream(new File("/proc/self/cmdline")),"UTF-8");
        scanner.useDelimiter("\u0000");
        StringBuilder cmdline=new StringBuilder();
        while (scanner.hasNext()) {
            cmdline.append(scanner.next()).append(" ");
        }
        log.info("Cmd line: {}",cmdline.toString());

    }

}
