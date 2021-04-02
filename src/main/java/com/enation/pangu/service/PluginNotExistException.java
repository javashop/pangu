package com.enation.pangu.service;

/**
 * 插件不存在异常
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2021/3/19
 */

public class PluginNotExistException extends RuntimeException {

    public PluginNotExistException(String message) {
        super(message);
    }
}
