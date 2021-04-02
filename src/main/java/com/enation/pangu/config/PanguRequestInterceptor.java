package com.enation.pangu.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * request拦截器
 * @author kingapex
 * @version v1.0
 * 2020年10月31日 下午5:29:56
 */
public class PanguRequestInterceptor extends HandlerInterceptorAdapter {


	@Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("context_path", request.getContextPath());
		return super.preHandle(request, response, handler);
	}


}
