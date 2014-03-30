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

package org.it4y.net.link;

public interface LinkNotification {

    /**
     * Event type : Link,Address,Routing. All is used for registering to ALL events
     */
    enum EventType { All,Link,Address,Routing }

    /**
     * Event Action : New, Update or Remove , All is used for registering to ALL actions, use None to skip event notification
     */
    enum EventAction { All,New,Update,Remove,None}

    /**
     * retrieve low level notification of changes on interface/link. Method is run in the netlink manager thread and must not
     * consume mutch time !.
     *
     * @param action : action enum (New,Update,Remove)
     * @param type : type of message (Link,Address,Routing)
     * @param network : Network interface
     */
    void onEvent(EventAction action, EventType type, NetworkInterface network);

    /**
     * retrieve high level notification when link is ready or not (see isActive). Method is run in the netlink manager thread and must
     * not consume much time !.
     *
     * @param network
     */
    void onStateChanged(NetworkInterface network);

}
