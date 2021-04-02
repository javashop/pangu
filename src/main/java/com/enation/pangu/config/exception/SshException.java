package com.enation.pangu.config.exception;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2020/11/4
 */

public class SshException extends RuntimeException{
    public SshException(String message, Throwable cause) {
        super(message,cause);
    }
}
