package com.enation.pangu.config;

import com.enation.pangu.utils.StringUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author gy
 * @version v1.0
 * @date 2021/3/17
 * @since v7.2.0
 */

@RestControllerAdvice()
@Slf4j
public class GlobalReturnConfig implements ResponseBodyAdvice<Object> {

    /**
     * 判定哪些请求要执行beforeBodyWrite，返回true执行，返回false不执行
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 返回值为void
        if (body == null) {
            return JsonResult.succsee();
        }
        // 判断返回类型如果为字符串  则处理数据为Json格式
        if (StringUtil.equals(methodParameter.getMethod().getReturnType().getName(), String.class.getName())) {
            Gson gson = new Gson();
            return gson.toJson(JsonResult.succsee(body));
        }
        //如果返回值类型为BaseResponse说明已经处理好了不需要二次处理
        if (body instanceof JsonResult) {
            return body;
        }

        return JsonResult.succsee(body);
    }

}
