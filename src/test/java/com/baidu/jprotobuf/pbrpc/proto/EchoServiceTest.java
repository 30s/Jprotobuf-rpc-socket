/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.pbrpc.proto;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.jprotobuf.pbrpc.proto.EchoInfoClass.EchoInfo;

/**
 * 
 * Test for {@link EchoService}
 *
 * @author xiemalin
 * @since 1.0
 * @see EchoService
 * 
 */
public class EchoServiceTest extends BaseEchoServiceTest {
    
    /**
     * test client auto recover connection from server.
     */
    @Test
    public void testAutoRecoverConnection() {

        EchoInfo echoInfo = getEchoInfo();
        
        EchoServiceImpl ecohImpl = new EchoServiceImpl();
        
        EchoInfo response = echoService.echo(echoInfo);
        Assert.assertEquals(ecohImpl.doEcho(echoInfo).getMessage(), response.getMessage());
        
        // here stop server
        stopServer();
        
        try {
            echoService.echo(echoInfo);
            Assert.fail("should be connect server failed.");
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }
        
        startServer();
        
        response = echoService.echo(echoInfo);
        Assert.assertEquals(ecohImpl.doEcho(echoInfo).getMessage(), response.getMessage());
    }

    /**
     * @return
     */
    private EchoInfo getEchoInfo() {
        String message = "xiemalin";
        // test success
        EchoInfo echoInfo = EchoInfo.newBuilder().setMessage(message).build();
        return echoInfo;
    }
    
    @Test
    public void testAttachment() {
        EchoInfo echoInfo = getEchoInfo();
        
        EchoServiceImpl ecohImpl = new EchoServiceImpl();
        
        EchoInfo response = echoService.echoWithAttachement(echoInfo);
        Assert.assertEquals(ecohImpl.doEcho(echoInfo).getMessage(), response.getMessage());
    }

}
