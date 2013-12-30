package org.it4y.demo;
import org.it4y.jni.libc;
import org.it4y.jni.linuxutils;
import org.it4y.net.tproxy.TProxyClientSocket;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Created by luc on 12/30/13.
 */
public class HttpProxyThread extends Thread {
        private TProxyClientSocket tclient = null;
        private static final int BUFFER_SIZE = 32768;

        public HttpProxyThread(TProxyClientSocket socket) {
            super("ProxyThread");
            this.tclient = socket;
        }

        public void run() {
            //get input from user
            //send request to server
            //get response from server
            //send response to user

            try {
                DataOutputStream out = new DataOutputStream(tclient.getSocket().getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(tclient.getSocket().getInputStream()));

                String inputLine, outputLine;
                int cnt = 0;
                String urlToCall = "";
                ///////////////////////////////////
                //begin get request from client
                while ((inputLine = in.readLine()) != null) {
                    try {
                        System.out.println(inputLine);
                        StringTokenizer tok = new StringTokenizer(inputLine);
                        tok.nextToken();
                    } catch (Exception e) {
                        break;
                    }
                    //parse the first line of the request to find the url
                    if (cnt == 0) {
                        String[] tokens = inputLine.split(" ");
                        urlToCall = tokens[1];
                    }
                    cnt++;
                }
                //end get request from client
                ///////////////////////////////////
                //urlToCall="http://www.google.com"+urlToCall;
                urlToCall="http://noc.21net.com/traintest/100m-random.dat";
                System.out.println("Request for : " + urlToCall);

                libc.tcp_info info=new libc.tcp_info();

                BufferedReader rd = null;
                try {
                    //System.out.println("sending request
                    //to real server for url: "
                    //        + urlToCall);
                    ///////////////////////////////////
                    //begin send request to server, get response from server
                    URL url = new URL(urlToCall);
                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    //not doing HTTP posts
                    conn.setDoOutput(false);
                    System.out.println("Type is: "
                    + conn.getContentType());
                    System.out.println("content length: "
                    + conn.getContentLength());
                    System.out.println("allowed user interaction: "
                    + conn.getAllowUserInteraction());
                    System.out.println("content encoding: "
                    + conn.getContentEncoding());
                    System.out.println("content type: "
                    + conn.getContentType());

                    // Get the response
                    InputStream is = null;
                    HttpURLConnection huc = (HttpURLConnection)conn;
                    try {
                            is = conn.getInputStream();
                            rd = new BufferedReader(new InputStreamReader(is));
                    } catch (IOException ioe) {
                            System.out.println(
                                    "********* IO EXCEPTION **********: " + ioe);
                    }
                    //end send request to server, get response from server
                    ///////////////////////////////////
                    System.out.println("request send...") ;
                    ///////////////////////////////////
                    //begin send response to client
                    byte by[] = new byte[ BUFFER_SIZE ];
                    int index = is.read( by, 0, BUFFER_SIZE );
                    while ( index != -1 )
                    {
                        out.write( by, 0, index );
                        index = is.read( by, 0, BUFFER_SIZE );
                        try {
                         linuxutils.gettcpinfo(tclient.getSocket(), info);
                         System.out.println(info);
                        } catch(libc.ErrnoException errno) {}

                    }
                    out.flush();
                    System.out.println("response send...") ;

                    //end send response to client
                    ///////////////////////////////////
                } catch (Exception e) {
                    //can redirect this to error log
                    System.err.println("Encountered exception: " + e);
                    //encountered error - just send nothing back, so
                    //processing can continue
                    out.writeBytes("");
                }
                System.out.println("done");

                //Dump some tcp info
                try {
                 System.out.println(linuxutils.gettcpinfo(tclient.getSocket(), info));
                 System.out.println(info);
                } catch (libc.ErrnoException errno) {}

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
                e.printStackTrace();
            }
        }
}
