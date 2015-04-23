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

import com.baidu.jprotobuf.pbrpc.RpcHandler;

/**
 * RPC handler for IDLServiceExporter
 * 
 * @author xiemalin
 * @since 1.0
 */
public class IDLServiceRpcHandler implements RpcHandler {
    
    @SuppressWarnings("unused")
	private IDLServiceExporter idlServiceExporter;
    
    /**
     * @param idlServiceExporter
     */
    public IDLServiceRpcHandler(IDLServiceExporter idlServiceExporter) {
        super();
        this.idlServiceExporter = idlServiceExporter;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.pbrpc.RpcHandler#doHandle(com.baidu.jprotobuf.pbrpc.server.RpcData)
     */
    public RpcData doHandle(RpcData data) throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.pbrpc.RpcHandler#getServiceName()
     */
    public String getServiceName() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.pbrpc.RpcHandler#getMethodName()
     */
    public String getMethodName() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.pbrpc.RpcHandler#getService()
     */
    public Object getService() {
        return null;
    }


}
