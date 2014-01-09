package org.it4y.util;

/**
 * Created by luc on 1/9/14.
 */
public class Counter {
    //workaround around final/inner class access
    private int cnt=0;
    public void inc() {
          cnt++;
    }
    public void dec() {
        cnt--;
    }

    public int getCount() {
          return cnt;
    }

}
