package org.it4y.net.link;

/**
 * Created by luc on 1/3/14.
 */
public interface LinkNotification {

    public enum EventType { All,Link,Address,Routing };
    public enum EventAction { All,New,Update,Remove};

    public void onEvent(EventAction action,EventType type,NetworkInterface network);
    public void onStateChanged(NetworkInterface network);

}
