/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.jprotobuf.pbrpc.transport;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Time task to process each request timeout event
 * 
 * @author xiemalin
 * @since 1.0
 */
public class RpcTimerTask implements TimerTask {

    public static Logger LOG = Logger.getLogger(RpcTimerTask.class.getName());

    private long correlationId;
    private RpcClient rpcClient;

    public RpcTimerTask(long correlationId, RpcClient client) {
        this.correlationId = correlationId;
        this.rpcClient = client;
    }

    public void run(Timeout timeout) throws Exception {

        LOG.log(Level.FINE, "correlationId:" + correlationId + " timeout");
        RpcClientCallState state = rpcClient.removePendingRequest(correlationId);
        if (null != state) {
            state.handleTimeout();
        } else {
            LOG.log(Level.FINE, "correlationId:" + correlationId
                    + ": is timeout and no PendingClientCallState found for correlationId " + correlationId);

        }

    }

}
