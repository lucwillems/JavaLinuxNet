package org.it4y.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luc on 1/9/14.
 */
public class Counter {
    //workaround around final/inner class access
    private AtomicInteger cnt=new AtomicInteger(0);
    public void inc() {
          cnt.incrementAndGet();
    }
    public void dec() {
        cnt.decrementAndGet();
    }

    public int getCount() {
          return cnt.intValue();
    }

}
