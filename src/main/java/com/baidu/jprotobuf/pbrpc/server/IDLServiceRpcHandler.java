/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
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
