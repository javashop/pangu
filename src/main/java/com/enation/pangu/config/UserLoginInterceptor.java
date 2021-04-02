package com.enation.pangu.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shen
 * @create 2020-12-23-18:57
 */

public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        String str = (String)session.getAttribute("login");
        if (!"login".equals(str)) {
//            request.getRequestDispatcher("/view/login").forward(request, response);
           response.sendRedirect("/view/login");
            return false;
        }

        return true;
    }

}
