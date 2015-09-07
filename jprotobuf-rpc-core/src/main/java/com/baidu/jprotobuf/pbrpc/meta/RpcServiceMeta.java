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

package com.baidu.jprotobuf.pbrpc.meta;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Rpc service description info
 * 
 * @author xiemalin
 * @since 2.1
 */
public class RpcServiceMeta {

    /**
     * rpc 服务名
     */
    @Protobuf(required = true)
    private String serviceName;
    
    /**
     * rpc方法中
     */
    @Protobuf(required = true)
    private String methodName;
    
    /**
     * 请求参数 google protocol buffer IDL 描述说明 
     */
    @Protobuf
    private String inputProto;
    
    /**
     * 请求参数对象名称
     */
    @Protobuf 
    private String inputObjName;
    
    /**
     *  返回参数 google protocol buffer IDL 描述说明 
     */
    @Protobuf
    private String outputProto;
    
    /**
     * 返回参数对象名称
     */
    @Protobuf 
    private String outputObjName;

    /**
     * get the serviceName
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * set serviceName value to serviceName
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * get the methodName
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * set methodName value to methodName
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * get the inputProto
     * @return the inputProto
     */
    public String getInputProto() {
        return inputProto;
    }

    /**
     * set inputProto value to inputProto
     * @param inputProto the inputProto to set
     */
    public void setInputProto(String inputProto) {
        this.inputProto = inputProto;
    }

    /**
     * get the outputProto
     * @return the outputProto
     */
    public String getOutputProto() {
        return outputProto;
    }

    /**
     * set outputProto value to outputProto
     * @param outputProto the outputProto to set
     */
    public void setOutputProto(String outputProto) {
        this.outputProto = outputProto;
    }

    /**
     * get the inputObjName
     * @return the inputObjName
     */
    public String getInputObjName() {
        return inputObjName;
    }

    /**
     * set inputObjName value to inputObjName
     * @param inputObjName the inputObjName to set
     */
    public void setInputObjName(String inputObjName) {
        this.inputObjName = inputObjName;
    }

    /**
     * get the outputObjName
     * @return the outputObjName
     */
    public String getOutputObjName() {
        return outputObjName;
    }

    /**
     * set outputObjName value to outputObjName
     * @param outputObjName the outputObjName to set
     */
    public void setOutputObjName(String outputObjName) {
        this.outputObjName = outputObjName;
    }
    
    
}
