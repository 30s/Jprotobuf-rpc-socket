/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.pbrpc.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.baidu.jprotobuf.pbrpc.ProtobufPRCService;
import com.baidu.jprotobuf.pbrpc.RpcHandler;
import com.baidu.jprotobuf.pbrpc.client.RpcMethodInfo;
import com.baidu.jprotobuf.pbrpc.utils.ReflectionUtils;
import com.baidu.jprotobuf.pbrpc.utils.StringUtils;

/**
 * To register all RPC service.
 * 
 * @author xiemalin
 * @since 1.0
 */
public class RpcServiceRegistry {

    /**
     * 
     */
    private Map<String, RpcHandler> serviceMap = new HashMap<String, RpcHandler>();
    
    /**
     * if override exist allowed. default is not allowed
     */
    private boolean dummyOverride = false;
    
    /**
     * set dummyOverride value to dummyOverride
     * @param dummyOverride the dummyOverride to set
     */
    public void setDummyOverride(boolean dummyOverride) {
        this.dummyOverride = dummyOverride;
    }
    
    public void registerService(final Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Param 'target' is null.");
        }
        
        Class<? extends Object> cls = target.getClass();
        
        ReflectionUtils.doWithMethods(cls, new ReflectionUtils.MethodCallback() {
            
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                
                ProtobufPRCService protobufPRCService = method.getAnnotation(ProtobufPRCService.class);
                if (protobufPRCService != null) {
                    doRegiterService(method, target, protobufPRCService);
                }
                
            }
        });
        
    }
    
    private void doRegiterService(Method method, Object service, ProtobufPRCService protobufPRCService) {
        boolean messageType = RpcMethodInfo.isMessageType(method);
        AbstractRpcHandler rpcHandler;
        if (!messageType) {
            rpcHandler = new AnnotationRpcHandler(method, service, protobufPRCService);
        } else {
            rpcHandler = new MessageGeneratedRpcHandler(method, service, protobufPRCService);
        }
        if (StringUtils.isEmpty(rpcHandler.getServiceName())) {
            throw new IllegalArgumentException(" serviceName from 'serviceExporter' is empty.");
        }
        serviceMap.put(getMethodSignature(rpcHandler.getServiceName(), rpcHandler.getMethodName()), rpcHandler);
    }
    
    private String getMethodSignature(String serviceName, String method) {
         return serviceName + "!" + method;
    }
    
    public RpcHandler lookupService(String serviceName, String methodName) {
        String methodSignature = getMethodSignature(serviceName, methodName);
        return serviceMap.get(methodSignature);
    }

    /**
     * @param serviceExporter
     */
    public void registerService(IDLServiceExporter serviceExporter) {
        if (serviceExporter == null) {
            throw new IllegalArgumentException("Param 'serviceExporter' is null.");
        }
        
        String serviceName = serviceExporter.getServiceName();
        if (StringUtils.isEmpty(serviceName)) {
            throw new IllegalArgumentException(" serviceName from 'serviceExporter' is empty.");
        }
        
        String methodSignature = getMethodSignature(serviceName, serviceExporter.getMethodName());
        
        if (serviceMap.containsKey(methodSignature)) {
            if (dummyOverride) {
                serviceMap.put(methodSignature, new IDLServiceRpcHandler(serviceExporter));
            }
        } else {
            serviceMap.put(methodSignature, new IDLServiceRpcHandler(serviceExporter));
        }
        
    }
}
