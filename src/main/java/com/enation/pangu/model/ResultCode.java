package com.enation.pangu.model;


/**
 * @author gy
 * @version v1.0
 * @date 2021/3/17
 * @since v7.2.0
 */
public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS("200","操作成功"),
    /**
     * 参数校验错误
     */
    PARAMETER_CHECK_ERROR("400", "参数校验错误"),
    UNLOGIN_ERROR("401", "用户未登录或登录状态超时失效"),
    AUTH_VALID_ERROR("403", "用户权限不足"),
    METHOD_NOT_ALLOWED("405", "访问方式不正确，GET请求使用POST方式访问"),
    SYSTEM_ERROR("500", "系统错误"),
    /**
     * 业务异常  不同的业务状态码可以向下顺延
     */
    BUSINESS_ERROR("600", "业务异常");



    private  String code;

    private  String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
