/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.pbrpc.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.baidu.jprotobuf.pbrpc.ClientAttachmentHandler;
import com.baidu.jprotobuf.pbrpc.LogIDGenerator;
import com.baidu.jprotobuf.pbrpc.client.RpcMethodInfo;

/**
 * 
 * RPC 包数据完整定义实现 
 *
 * @author xiemalin
 * @since 1.0
 */
public class RpcDataPackage implements Readable, Writerable {

    private RpcHeadMeta head;
    private RpcMeta rpcMeta;
    private byte[] data;
    private byte[] attachment;
    
    /**
     * set magic code
     * @param magicCode
     * @return
     */
    public RpcDataPackage magicCode(String magicCode) {
        setMagicCode(magicCode);
        return this;
    }
    
    /**
     * @param serviceName
     * @return
     */
    public RpcDataPackage serviceName(String serviceName) {
        RpcRequestMeta request = initRequest();
        request.setServiceName(serviceName);
        return this;
    }
    
    public RpcDataPackage methodName(String methodName) {
        RpcRequestMeta request = initRequest();
        request.setMethodName(methodName);
        return this;
    }
    
    public RpcDataPackage data(byte[] data) {
        setData(data);
        return this;
    }
    
    public RpcDataPackage attachment(byte[] attachment) {
        setAttachment(attachment);
        return this;
    }
    
    public RpcDataPackage authenticationData(byte[] authenticationData) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setAuthenticationData(authenticationData);
        return this;
    }
    
    public RpcDataPackage correlationId(long correlationId) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setCorrelationId(correlationId);
        return this;
    }
    
    public RpcDataPackage compressType(int compressType) {
        RpcMeta rpcMeta = initRpcMeta();
        rpcMeta.setCompressType(compressType);
        return this;
    }
    
    public RpcDataPackage logId(long logId) {
        RpcRequestMeta request = initRequest();
        request.setLogId(logId);
        return this;
    }

    public RpcDataPackage errorCode(int errorCode) {
        RpcResponseMeta response = initResponse();
        response.setErrorCode(errorCode);
        return this;
    }
    
    public RpcDataPackage errorText(String errorText) {
        RpcResponseMeta response = initResponse();
        response.setErrorText(errorText);
        return this;
    }

    /**
     * 
     */
    private RpcRequestMeta initRequest() {
        RpcMeta rpcMeta = initRpcMeta();
        
        RpcRequestMeta request = rpcMeta.getRequest();
        if (request == null) {
            request = new RpcRequestMeta();
            rpcMeta.setRequest(request);
        }
        
        return request;
    }
    
    private RpcResponseMeta initResponse() {
        RpcMeta rpcMeta = initRpcMeta();
        
        RpcResponseMeta response = rpcMeta.getResponse();
        if (response == null) {
            response = new RpcResponseMeta();
            rpcMeta.setResponse(response);
        }
        
        return response;
    }

    /**
     * 
     */
    private RpcMeta initRpcMeta() {
        if (rpcMeta == null) {
            rpcMeta = new RpcMeta();
        }
        return rpcMeta;
    }
    
    public void setMagicCode(String magicCode) {
        if (head == null) {
            head = new RpcHeadMeta();
        }
        head.setMagicCode(magicCode);
    }
    
    /**
     * get the head
     * @return the head
     */
    public RpcHeadMeta getHead() {
        return head;
    }

    /**
     * set head value to head
     * @param head the head to set
     */
    public void setHead(RpcHeadMeta head) {
        this.head = head;
    }

    /**
     * get the rpcMeta
     * @return the rpcMeta
     */
    public RpcMeta getRpcMeta() {
        return rpcMeta;
    }

    /**
     * set rpcMeta value to rpcMeta
     * @param rpcMeta the rpcMeta to set
     */
    protected void setRpcMeta(RpcMeta rpcMeta) {
        this.rpcMeta = rpcMeta;
    }

    /**
     * get the data
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * set data value to data
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * get the attachment
     * @return the attachment
     */
    public byte[] getAttachment() {
        return attachment;
    }

    /**
     * set attachment value to attachment
     * @param attachment the attachment to set
     */
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
    
    public RpcDataPackage getErrorResponseRpcDataPackage(int errorCode, String errorText) {
        return getErrorResponseRpcDataPackage(new RpcResponseMeta(errorCode, errorText));
    }
    
    public RpcDataPackage getErrorResponseRpcDataPackage(RpcResponseMeta responseMeta) {
        RpcDataPackage response =  new RpcDataPackage();
        
        RpcMeta eRpcMeta = rpcMeta;
        if (eRpcMeta == null) {
            eRpcMeta = new RpcMeta();
        }
        eRpcMeta.setResponse(responseMeta);
        eRpcMeta.setRequest(null);
        
        response.setRpcMeta(eRpcMeta);
        response.setHead(head);
        
        return response;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.remoting.pbrpc.Writerable#write()
     */
    public byte[] write() {
        if (head == null) {
            throw new RuntimeException("property 'head' is null.");
        }
        if (rpcMeta == null) {
            throw new RuntimeException("property 'rpcMeta' is null.");
        }
        
        int totolSize = RpcHeadMeta.SIZE;
        
        // set dataSize
        int dataSize = 0;
        if (data != null) {
            dataSize = data.length;
            totolSize += dataSize;
        }
        
        // set attachment size
        int attachmentSize = 0;
        if (attachment != null) {
            attachmentSize = attachment.length;
            totolSize += attachmentSize;
        }
        rpcMeta.setAttachmentSize(attachmentSize);
        
        // get rpc meta data
        byte[] rpcMetaBytes = rpcMeta.write();
        int rpcMetaSize = rpcMetaBytes.length;
        totolSize += rpcMetaSize;
        head.setMetaSize(rpcMetaSize);
        head.setMessageSize(totolSize);
        
        try {
            // join all byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream(totolSize);
            // write head
            baos.write(head.write());
            
            // write meta data
            baos.write(rpcMetaBytes);
            
            // write data
            if (data != null) {
                baos.write(data);
            }
            
            if (attachment != null) {
                baos.write(attachment);
            }
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.remoting.pbrpc.Readable#read(byte[])
     */
    public void read(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("param 'bytes' is null.");
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        
        // read head data
        byte[] headBytes = new byte[RpcHeadMeta.SIZE];
        bais.read(headBytes, 0, RpcHeadMeta.SIZE);
        
        // parse RPC head
        head = new RpcHeadMeta();
        head.read(headBytes);
        
        // get RPC meta size
        int metaSize = head.getMetaSize();
        // read meta data
        byte[] metaBytes = new byte[metaSize];
        bais.read(metaBytes, 0, metaSize);
        
        rpcMeta = new RpcMeta();
        rpcMeta.read(metaBytes);
        
        int attachmentSize = rpcMeta.getAttachmentSize();
        
        // read message data
        // message data size =  totalsize - metasize - headsize - attachmentSize
        int totalSize = head.getMessageSize();
        int dataSize = totalSize - RpcHeadMeta.SIZE - metaSize - attachmentSize;
        
        if (dataSize > 0) {
            data = new byte[dataSize];
            bais.read(data, 0, dataSize);
        }
        
        // if need read attachment
        if (attachmentSize > 0) {
            attachment = new byte[attachmentSize];
            bais.read(attachment, 0, attachmentSize);
        }
        
    }
    
    public static RpcDataPackage buildRpcDataPackage(RpcMethodInfo methodInfo, Object[] args) throws IOException {
        RpcDataPackage dataPacage = new RpcDataPackage();
        dataPacage.magicCode(ProtocolConstant.MAGIC_CODE);
        dataPacage.serviceName(methodInfo.getServiceName()).methodName(methodInfo.getMethod().getName());
        // set data
        if (args.length == 1) {
            byte[] data = methodInfo.inputEncode(args[0]);
            if (data != null) {
                dataPacage.data(data);
            }
        }
        
        // set logid
        LogIDGenerator logIDGenerator = methodInfo.getLogIDGenerator();
        if (logIDGenerator != null) {
            long logId = logIDGenerator.generate(methodInfo.getServiceName(), methodInfo.getMethod().getName(), args);
            dataPacage.logId(logId);
        }
        
        // set attachment
        ClientAttachmentHandler attachmentHandler = methodInfo.getClientAttachmentHandler();
        if (attachmentHandler != null) {
            byte[] attachment = attachmentHandler.handleRequest(methodInfo.getServiceName(), 
                    methodInfo.getMethod().getName(), args);
            if (attachment != null) {
                dataPacage.attachment(attachment);
            }
        }
        return dataPacage;
    }
    
}
