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

package org.it4y.net.protocols.IP.TCP;

public interface TCPOption {

    //WELL KNOW TCP options id's
    public final static byte END = 0;
    public final static byte NOP = 1;
    public final static byte MSS = 2;
    public final static byte WSCALE = 3;
    public final static byte SACK_ENABLED = 4;
    public final static byte SACK = 5;
    public final static byte TIMESTAMP = 8;

    public String getName();

    public int getLength();
}
