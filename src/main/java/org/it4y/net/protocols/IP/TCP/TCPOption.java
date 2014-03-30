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

package org.it4y.net.protocols.IP.TCP;

public interface TCPOption {

    //WELL KNOW TCP options id's
    byte END = 0;
    byte NOP = 1;
    byte MSS = 2;
    byte WSCALE = 3;
    byte SACK_ENABLED = 4;
    byte SACK = 5;
    byte TIMESTAMP = 8;

    String getName();

    int getLength();
}
