package com.enation.pangu.config.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务异常类,各业务异常需要继承此异常
 * 业务类如有不能处理异常也要抛出此异常
 *
 * @author kingapex
 * @version v1.0.0
 * @since v7.0.0
 * 2017年3月7日 上午11:09:29
 */
public class ServiceException extends RuntimeException {

    protected HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    private String code;
    /**
     * 错误信息
     */
    private String data;


    public ServiceException(String code, String message) {
        super(message);
        this.code = code;

    }

    public ServiceException(String code, String message, String data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getCode() {
        return code;
    }


    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
