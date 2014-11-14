/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.pbrpc;

import com.baidu.jprotobuf.pbrpc.data.RpcDataPackage;

/**
 * 
 * Error data exception.
 *
 * @author xiemalin
 * @since 1.4
 */
public class ErrorDataException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9052741930614009382L;
    
    private RpcDataPackage rpcDataPackage;
    private int errorCode;
    
    /**
     * get the errorCode
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * set errorCode value to errorCode
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * get the rpcDataPackage
     * @return the rpcDataPackage
     */
    public RpcDataPackage getRpcDataPackage() {
        return rpcDataPackage;
    }

    /**
     * set rpcDataPackage value to rpcDataPackage
     * @param rpcDataPackage the rpcDataPackage to set
     */
    public void setRpcDataPackage(RpcDataPackage rpcDataPackage) {
        this.rpcDataPackage = rpcDataPackage;
    }

    /**
     * 
     */
    public ErrorDataException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public ErrorDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ErrorDataException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ErrorDataException(Throwable cause) {
        super(cause);
    }

    
}
