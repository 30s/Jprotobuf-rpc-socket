/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.pbrpc.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.baidu.jprotobuf.pbrpc.server.IDLServiceExporter;
import com.baidu.jprotobuf.pbrpc.server.RpcServiceRegistry;

/**
 * RPC server provider by Netty server.
 * 
 * @author xiemalin
 * @since 1.0
 */
public class RpcServer extends ServerBootstrap {

    private static final Logger LOG = Logger.getLogger(RpcServer.class.getName());

    private AtomicBoolean stop = new AtomicBoolean(false);

    private RpcServerOptions rpcServerOptions;

    /**
     * rpcServiceRegistry
     */
    private RpcServiceRegistry rpcServiceRegistry = new RpcServiceRegistry();

    public RpcServer(RpcServerOptions serverOptions) {
        this(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        rpcServerOptions = new RpcServerOptions();
        RpcServerPipelineFactory rpcServerPipelineFactory = new RpcServerPipelineFactory(rpcServiceRegistry,
                rpcServerOptions);
        setPipelineFactory(rpcServerPipelineFactory);

        this.setOption("child.keepAlive", serverOptions.isKeepAlive());
        this.setOption("child.reuseAddress", true);

        this.setOption("child.bufferFactory",
                new org.jboss.netty.buffer.HeapChannelBufferFactory(serverOptions.getByteOrder()));

        // Configure bootstrap
        this.setOption("child.tcpNoDelay", serverOptions.isTcpNoDelay());
        this.setOption("child.soLinger", serverOptions.getSoLinger());
        this.setOption("child.connectTimeoutMillis", serverOptions.getConnectTimeout());
        this.setOption("backlog", serverOptions.getBacklog());
        this.setOption("child.receiveBufferSize", serverOptions.getReceiveBufferSize());
        this.setOption("child.sendBufferSize", serverOptions.getSendBufferSize());
    }

    public RpcServer() {
        this(new RpcServerOptions());
    }

    public RpcServer(ChannelFactory channelFactory) {
        super(channelFactory);
    }

    public void registerService(IDLServiceExporter service) {
        rpcServiceRegistry.registerService(service);
    }

    public void registerService(final Object target) {
        rpcServiceRegistry.registerService(target);
    }

    public void start(int port) {
        LOG.log(Level.FINE, "Starting ...");
        this.bind(new InetSocketAddress(port));
    }

    public void start(SocketAddress sa) {
        LOG.log(Level.FINE, "Starting on: " + sa);
        this.bind(sa);
    }

    public void waitForStop() throws InterruptedException {
        while (!stop.get()) {
            Thread.sleep(1000);
        }
        shutdown();
    }

    public void stop() {
        stop.set(true);
    }

    public AtomicBoolean getStop() {
        return stop;
    }

    public void setStop(AtomicBoolean stop) {
        this.stop = stop;
    }

    /**
     * get the rpcServerOptions
     * 
     * @return the rpcServerOptions
     */
    public RpcServerOptions getRpcServerOptions() {
        return rpcServerOptions;
    }

    /**
     * set rpcServerOptions value to rpcServerOptions
     * 
     * @param rpcServerOptions
     *            the rpcServerOptions to set
     */
    public void setRpcServerOptions(RpcServerOptions rpcServerOptions) {
        this.rpcServerOptions = rpcServerOptions;
    }

}
