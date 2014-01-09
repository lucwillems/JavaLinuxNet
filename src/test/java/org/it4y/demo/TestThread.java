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

    public TestThread(LinkManager lnkMng) {
        this.lnkMng=lnkMng;
    }
    @Override
    public void run() {
        while(true) {
            lnkMng.ReadLock();
            try {
                for (String name:lnkMng.getInterfaceList()) {
                    NetworkInterface x=lnkMng.findByInterfaceName(name);
                    if(!log.isTraceEnabled()) {
                        log.trace("{}",x.toString());
                    }
                }
            } finally {
                lnkMng.ReadUnLock();
            }
        }
    }
}
