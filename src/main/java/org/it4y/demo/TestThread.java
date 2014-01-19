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
