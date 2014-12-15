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

package com.baidu.jprotobuf.pbrpc.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import com.baidu.jprotobuf.pbrpc.ProtobufRPCService;
import com.google.protobuf.GeneratedMessage;

/**
 * RPC handler for Google protoc generated java code.
 * 
 * @author xiemalin
 * @since 1.2
 */
public class MessageGeneratedRpcHandler extends AbstractRpcHandler {

    private static final String PROTOBUF_PARSE_METHOD = "parseFrom";

    private Method parseFromMethod;

    /**
     * @param method
     * @param service
     * @param protobufPRCService
     */
    public MessageGeneratedRpcHandler(Method method, Object service, ProtobufRPCService protobufPRCService) {
        super(method, service, protobufPRCService);

        if (getInputClass() != null) {
            if (GeneratedMessage.class.isAssignableFrom(getInputClass())) {
                try {
                    parseFromMethod = getInputClass().getMethod(PROTOBUF_PARSE_METHOD, InputStream.class);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.jprotobuf.pbrpc.RpcHandler#doRealHandle(com.baidu.jprotobuf.pbrpc
     * .server.RpcData)
     */
    protected RpcData doRealHandle(RpcData data) throws Exception {

        Object input = null;
        Object[] param;
        Object ret;
        if (data.getData() != null && parseFromMethod != null) {
            input = parseFromMethod.invoke(getInputClass(), new ByteArrayInputStream(data.getData()));
            param = new Object[] { input };
        } else {
            param = new Object[0];
        }

        RpcData retData = new RpcData();
        // process attachment
        if (getAttachmentHandler() != null) {
            byte[] responseAttachment = getAttachmentHandler().handleAttachement(data.getAttachment(),
                    getServiceName(), getMethodName(), param);
            retData.setAttachment(responseAttachment);
        }

        ret = getMethod().invoke(getService(), param);

        if (ret == null) {
            return retData;
        }

        if (ret != null && ret instanceof GeneratedMessage) {
            byte[] response = ((GeneratedMessage) ret).toByteArray();
            retData.setData(response);
        }

        return retData;
    }

}
