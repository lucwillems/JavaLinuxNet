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

package org.it4y.net.netlink;

import org.it4y.jni.linux.if_neighbour;

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/2/14.
 */
public class neighbourRTAMessages extends RTAMessage {
    public neighbourRTAMessages(final int pos, final ByteBuffer buffer) {
        super(pos, buffer);
    }

    @Override
    public String getRTAName() {
        return if_neighbour.NDA_NAMES.get((int)type&0xffff);
    }
}
