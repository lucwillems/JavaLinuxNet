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

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by luc on 3/23/14.
 */
public class netLinkMessageTest {
    private Logger logger= LoggerFactory.getLogger(netLinkMessageTest.class);

    @Test
    public void testNlErrorMessage() throws Exception {
        logger.info("NlErrorMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16);
        NlErrorMessage msg=new NlErrorMessage(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertEquals(-1,msg.getRTAIndex(""));
        Assert.assertNull(msg.createRTAMessage(0, rawData));
        Assert.assertNotNull(msg.toString());
        logger.info("NlErrorMessage : {}",msg.toString());
    }
    @Test
    public void testNlDoneMessage() throws Exception {
        logger.info("NlDoneMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16);
        NlDoneMessage msg=new NlDoneMessage(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertEquals(-1,msg.getRTAIndex(""));
        Assert.assertNull(msg.createRTAMessage(0,rawData));
        Assert.assertNotNull(msg.toString());
        logger.info("NlErrorMessage : {}",msg.toString());
    }

    @Test
    public void testneighbourMessage() throws Exception {
        logger.info("neighbourMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16+12);
        neighbourMsg msg=new neighbourMsg(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertEquals(-1, msg.getRTAIndex(""));
        Assert.assertNotNull(msg.toString());
        logger.info("neighbourMessage : {}",msg.toString());
    }

    @Test
    public void testRouteMessage() throws Exception {
        logger.info("routeMessage...");
        ByteBuffer rawData=ByteBuffer.allocate(16+12);
        routeMsg msg=new routeMsg(rawData);
        Assert.assertNotNull(msg);
        Assert.assertEquals(0, msg.getNlMsgType());
        Assert.assertEquals(0, msg.getNlMsgLen());
        Assert.assertEquals(0, msg.getNlMsgPID());
        Assert.assertEquals(0,msg.getNlMsgSequence());
        Assert.assertEquals(0,msg.getNlmsg_flags());
        Assert.assertEquals(-1, msg.getRTAIndex(""));
        Assert.assertNotNull(msg.toString());
        logger.info("routeMessage : {}",msg.toString());
    }

}
