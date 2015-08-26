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
package com.baidu.jprotobuf.pbrpc.spring;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * 
 * @author xiemalin
 * @since 2.17
 */
@Ignore
public class AnnotationRpcXmlConfigurationTest extends RpcXmlConfigurationTestBase {

    protected String getConfigurationPath() {
        return "classpath:" + AnnotationRpcXmlConfigurationTest.class.getName().replace('.', '/') + ".xml";
    }

    @Test
    public void testCommonRpcRequest() {
        
        SimpleAopClass bean = context.getBean("simpleAOP", SimpleAopClass.class);
        bean.doHello();

        AnnotationEchoServiceClient annotationEchoServiceClient =
                context.getBean("echoServiceClient", AnnotationEchoServiceClient.class);
        
        // test common client
        super.internalRpcRequestAndResponse(annotationEchoServiceClient.echoService);

    }
    
    @Test
    public void testHaRpcRequest() {

        AnnotationEchoServiceClient annotationEchoServiceClient =
                context.getBean("echoServiceClient", AnnotationEchoServiceClient.class);
        
        // test ha client
        super.internalRpcRequestAndResponse(annotationEchoServiceClient.haEchoService);
        
    }
    
    @Test
    public void testHaRpcRequestWithPartialFailed() {

        AnnotationEchoServiceClient annotationEchoServiceClient =
                context.getBean("echoServiceClient", AnnotationEchoServiceClient.class);
        
        // test ha client
        super.internalRpcRequestAndResponse(annotationEchoServiceClient.haEchoServiceOfPartialFailed);
    }
}
