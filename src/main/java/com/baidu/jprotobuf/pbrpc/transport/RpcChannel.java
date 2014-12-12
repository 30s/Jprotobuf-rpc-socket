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

import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.Timeout;

import com.baidu.jprotobuf.pbrpc.data.RpcDataPackage;
import com.google.protobuf.RpcCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RPC request and response channel processor.
 * 
 * @author xiemalin
 * @since 1.0
 */
public class RpcChannel {

    private static Logger LOG = Logger.getLogger(RpcChannel.class.getName());

    /**
     * RPC client
     */
    private RpcClient rpcClient;
    private ChannelPool channelPool;

    /**
     * try to do connect
     */
    public void testChannlConnect() {
        Connection channel = channelPool.getChannel();
        channelPool.returnChannel(channel);
    }

    /**
     * @param rpcClient
     * @param host
     * @param port
     */
    public RpcChannel(RpcClient rpcClient, String host, int port) {
        this.rpcClient = rpcClient;
        channelPool = new ChannelPool(rpcClient, host, port);
        rpcClient.setChannelPool(channelPool);
    }

    public void doTransport(RpcDataPackage rpcDataPackage, RpcCallback<RpcDataPackage> callback, long onceTalkTimeout) {
        if (rpcDataPackage == null) {
            throw new IllegalArgumentException("param 'rpcDataPackage' is null.");
        }

        long callMethodStart = System.currentTimeMillis();

        // register timer
        Timeout timeout = rpcClient.getTimer().newTimeout(
                new RpcTimerTask(rpcDataPackage.getRpcMeta().getCorrelationId(), this.rpcClient), onceTalkTimeout,
                TimeUnit.MILLISECONDS);

        RpcClientCallState state = new RpcClientCallState(callback, rpcDataPackage, timeout);

        Connection channel = channelPool.getChannel();
        try {
            Long correlationId = state.getDataPackage().getRpcMeta().getCorrelationId();
            rpcClient.registerPendingRequest(correlationId, state);

            if (!channel.getFuture().isSuccess()) {
                try {
                    channel.produceRequest(state);
                } catch (IllegalStateException e) {
                    RpcClientCallState callState = rpcClient.removePendingRequest(correlationId);
                    if (callState != null) {
                        callState.handleFailure(e.getMessage());
                        LOG.log(Level.FINE, "id:" + correlationId + " is put in the queue");
                    }
                }
            } else {
                channel.getFuture().getChannel().write(state.getDataPackage());
            }

            long callMethodEnd = System.currentTimeMillis();
            LOG.log(Level.FINE, "profiling callMethod cost " + (callMethodEnd - callMethodStart) + "ms");
        } finally {
            channelPool.returnChannel(channel);
        }

    }

}
