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

package org.it4y.demo;

import org.it4y.jni.libc;
import org.it4y.jni.linuxutils;
import org.it4y.net.tproxy.TProxyInterceptedSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by luc on 12/30/13.
 */
public class HttpProxyThread extends Thread {
    private static final Pattern spaceSplit = Pattern.compile(" ");
    private final Logger log= LoggerFactory.getLogger(HttpProxyThread.class);
    private TProxyInterceptedSocket tclient = null;
    private static final int BUFFER_SIZE = 32768;

    public HttpProxyThread(TProxyInterceptedSocket socket) {
        super("ProxyThread");
        tclient = socket;
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
            final DataOutputStream out = new DataOutputStream(tclient.getSocket().getOutputStream());
            final BufferedReader in = new BufferedReader(new InputStreamReader(tclient.getSocket().getInputStream()));

            String inputLine;
            int cnt = 0;
            String urlToCall = "";
            ///////////////////////////////////
            //begin get request from client
            while ((inputLine = in.readLine()) != null) {
                try {
                    log.info(">{}", inputLine);
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }
                //parse the first line of the request to find the url
                if (cnt == 0) {
                    final String[] tokens = spaceSplit.split(inputLine);
                    urlToCall = tokens[1];
                }
                cnt++;
            }
            //end get request from client
            ///////////////////////////////////
            urlToCall="http://www.google.com";
            //urlToCall = "http://noc.21net.com/traintest/100m-random.dat";
            log.info("Request for : {}", urlToCall);

            libc.tcp_info info = new libc.tcp_info();

            BufferedReader rd = null;
            try {
                ///////////////////////////////////
                //begin send request to server, get response from server
                URL url = new URL(urlToCall);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                //not doing HTTP posts
                conn.setDoOutput(false);
                log.info("Content-Type is: {}", conn.getContentType());
                log.info("Content-length: {}", conn.getContentLength());
                log.info("Content-encoding: {}", conn.getContentEncoding());
                log.info("allowed user interaction: {}", conn.getAllowUserInteraction());

                // Get the response
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(is));
                } catch (final IOException ioe) {
                    log.error("oeps: io error", ioe);
                }
                //end send request to server, get response from server
                log.info("request sended.");
                ///////////////////////////////////
                //begin send response to client
                final byte[] by = new byte[BUFFER_SIZE];
                int index = is.read(by, 0, BUFFER_SIZE);
                cnt = 0;
                while (index != -1) {
                    out.write(by, 0, index);
                    index = is.read(by, 0, BUFFER_SIZE);
                    if (cnt % 16 == 0) {
                        try {
                            linuxutils.gettcpinfo(tclient.getSocket(), info);
                            log.info("tcpinfo: {}",info);
                        } catch (libc.ErrnoException ignore) {
                        }
                    }
                    cnt++;
                }
                out.flush();
                log.info("response send...");

                //end send response to client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                log.error("Encountered exception: ", e);
                //encountered error - just send nothing back, so
                //processing can continue
                out.writeBytes("");
            }
            log.info("done");

            //Dump some final tcp info
            try {
                linuxutils.gettcpinfo(tclient.getSocket(), info);
                log.info("Final tcp info: ", info);
            } catch (libc.ErrnoException errno) {
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (tclient != null) {
                tclient.getSocket().close();
            }

        } catch (IOException e) {
            log.error("oeps ", e);
        }
    }
}
