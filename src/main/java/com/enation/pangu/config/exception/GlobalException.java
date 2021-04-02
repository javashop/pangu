package com.enation.pangu.config.exception;

import com.enation.pangu.model.ResultCode;
import com.enation.pangu.config.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

/**
 * @author guoyou
 * @date 2020/1/15 15:34
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {


    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResult error(ConstraintViolationException e) {
        log.error("参数约束验证出错", e);
        return JsonResult.fail(ResultCode.PARAMETER_CHECK_ERROR.getCode(), e.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult error(MethodArgumentNotValidException e) {
        log.error("参数验证出错", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return JsonResult.fail(ResultCode.PARAMETER_CHECK_ERROR.getCode(), getMessage(allErrors));

    }


    @ExceptionHandler(BindException.class)
    public JsonResult error(BindException e) {
        log.error("参数验证出错", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return JsonResult.fail(ResultCode.PARAMETER_CHECK_ERROR.getCode(), getMessage(allErrors));

    }


    @ExceptionHandler(ServiceException.class)
    public JsonResult error(ServiceException e) {
        log.error("业务出错", e);
        return JsonResult.fail(e.getCode(), e.getMessage());

    }

    @ExceptionHandler(SshException.class)
    public JsonResult error(SshException e) {
        log.error("业务出错", e);
        return JsonResult.fail(ResultCode.BUSINESS_ERROR.getCode(), e.getMessage());

    }

    private String getMessage(List<ObjectError> allErrors) {
        StringBuilder builder = new StringBuilder();

        if ( CollectionUtils.isNotEmpty(allErrors)) {
            allErrors.forEach(p -> {
                builder.append(p.getDefaultMessage() + ",");
            });
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "参数错误";
    }

    @ExceptionHandler(Exception.class)
    public JsonResult error(Exception e) {
        log.error("系统异常,错误信息为:" + e);
        e.printStackTrace();
        return JsonResult.fail();
    }

}
