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

import java.nio.ByteBuffer;

/**
 * Created by luc on 1/3/14.
 */
public class NlDoneMessage extends NlMessage {
    public NlDoneMessage(final ByteBuffer msg) {
        super(msg);
    }

    @Override
    public int getRTAIndex(final String name) {
        return RTA_INDEX_NOTSUPPORTED;
    }

    @Override
    public RTAMessage createRTAMessage(final int position, final ByteBuffer msg) {
        return null;
    }

    @Override
    public boolean moreMessages() {
        return false;
    }

    public String toString() {
        final StringBuilder s = new StringBuilder(128);
        s.append(super.toString());
        s.append("netlink done");
        return s.toString();
    }
}
