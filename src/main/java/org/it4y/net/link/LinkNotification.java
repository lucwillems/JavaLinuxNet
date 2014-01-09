package org.it4y.net.link;

/**
 * Created by luc on 1/3/14.
 */
public interface LinkNotification {

    /**
     * Event type : Link,Address,Routing. All is used for registering to ALL events
     */
    public enum EventType { All,Link,Address,Routing };

    /**
     * Event Action : New, Update or Remove , All is used for registering to ALL actions, use None to skip event notification
     */
    public enum EventAction { All,New,Update,Remove,None};

    /**
     * retrieve low level notification of changes on interface/link. Method is run in the netlink manager thread and must not
     * consume mutch time !.
     *
     * @param action : action enum (New,Update,Remove)
     * @param type : type of message (Link,Address,Routing)
     * @param network : Network interface
     */
    public void onEvent(EventAction action,EventType type,NetworkInterface network);

    /**
     * retrieve high level notification when link is ready or not (see isActive). Method is run in the netlink manager thread and must
     * not consume much time !.
     *
     * @param network
     */
    public void onStateChanged(NetworkInterface network);

}
