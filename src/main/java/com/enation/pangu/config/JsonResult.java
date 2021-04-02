package com.enation.pangu.config;

import com.enation.pangu.model.ResultCode;

/**
 * @author guoyou
 * @date 2019/12/25 9:57
 * 通用信息返回类
 */
public class JsonResult {
    /**
     * 是否业务成功
     */
    private Boolean success;
    /**
     * 返回代码
     */
    private String code;
    /**
     * 返回描述
     */
    private String message;
    /**
     * 返回结果
     */
    private Object data;

    public static  JsonResult succsee() {
        return new JsonResult(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static  JsonResult succsee(String resultMessage,Object data) {
        return new JsonResult(true, ResultCode.SUCCESS.getCode(), resultMessage, data);
    }

    public static  JsonResult succsee(Object data) {
        return new JsonResult(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static  JsonResult fail() {
        return new JsonResult(false, ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage(), null);
    }

    public static  JsonResult fail(String resultMessage) {
        return new JsonResult(false, ResultCode.SYSTEM_ERROR.getCode(), resultMessage, null);
    }

    public static  JsonResult fail(String resultCode, String resultMessage) {
        return new JsonResult(false, resultCode, resultMessage, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public JsonResult(Boolean success, String code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private JsonResult() {
    }
}
