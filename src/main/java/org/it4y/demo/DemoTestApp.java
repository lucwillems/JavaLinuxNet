
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

package org.it4y.demo;

import org.it4y.net.link.LinkManager;
import org.it4y.net.link.LinkNotification;
import org.it4y.net.link.NetworkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 12/25/13.
 * run src/test/scripts/setup-test.sh before running this app. this will setup correct tun device and routing.
 * and than ping to 8.8.4.4
 */
public class DemoTestApp {
    private final static Logger log= LoggerFactory.getLogger("Demo");

    public static void main(String[] args) throws Exception {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
        TProxyListener tproxy = new TProxyListener();
        tproxy.start();
        log.info("tproxy server running");
        Thread.sleep(100);
        TunTapInterfaceListener tundev = new TunTapInterfaceListener("test", 1500);
        tundev.start(); //this will bring interface luc UP
        log.info("tun interface listener running");
        LinkManager lnkMng=new LinkManager();
        lnkMng.registerListener(LinkNotification.EventAction.All,LinkNotification.EventType.All, new LinkNotification() {
            @Override
            public void onEvent(EventAction action, EventType type, NetworkInterface network) {
                log.info("onEvent : "+action+" "+type+" "+network);
            }

            @Override
            public void onStateChanged(NetworkInterface network) {
                log.info("onStateChanged : "+network+" active:"+network.isActive());
            }
        });
        //start link manager
        lnkMng.start();
        Thread.sleep(500);
        log.info("ready to rock and roll");
        while (true) {
            Thread.sleep(1000);
            tundev.dumpSpeed();
        }
    }
}
