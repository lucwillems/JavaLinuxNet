package org.it4y.demo;

/**
 * Created by luc on 12/28/13.
 */
public abstract class TestRunner extends Thread {
    protected boolean running = false;

    public TestRunner() {
        super();
        running = false;
        this.setDaemon(true);
    }

    public TestRunner(String name) {
        super(name);
        running = false;
        this.setDaemon(true);
    }

    public void halt() {
        running = false;
    }

    abstract public void run();
}
