package org.it4y.demo;

/**
 * Created by luc on 12/28/13.
 */
public abstract class TestRunner extends Thread {
    protected boolean running;

    public TestRunner() {
        setDaemon(true);
    }

    public TestRunner(final String name) {
        super(name);
        setDaemon(true);
    }

    public void halt() {
        running = false;
    }

    abstract public void run();
}
