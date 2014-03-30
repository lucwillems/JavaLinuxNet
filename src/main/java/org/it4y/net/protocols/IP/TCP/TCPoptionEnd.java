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

public class TCPoptionEnd implements TCPOption {
    public static final String name="end";
    public static final int length=1;

    public TCPoptionEnd() {
    }

    ;

    public String getName() {  return name;  }

    public int getLength() {
        return length;
    }

    public String toString() {
        return getName();
    }
}
