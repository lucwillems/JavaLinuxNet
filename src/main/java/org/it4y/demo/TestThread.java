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

import org.it4y.net.link.LinkManager;
import org.it4y.net.link.NetworkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luc on 1/4/14.
 */
public class TestThread extends TestRunner {
    LinkManager lnkMng;
    Logger log= LoggerFactory.getLogger(TestThread.class);

    public TestThread(final LinkManager lnkMng) {
        this.lnkMng=lnkMng;
    }

    public void run() {
        running=true;
        while(running) {
            lnkMng.ReadLock();
            try {
                for (final String name:lnkMng.getInterfaceList()) {
                    final NetworkInterface x=lnkMng.findByInterfaceName(name);
                    if(!log.isTraceEnabled()) {
                        log.trace("{}",x);
                    }
                }
            } finally {
                lnkMng.ReadUnLock();
            }
        }
    }
}
