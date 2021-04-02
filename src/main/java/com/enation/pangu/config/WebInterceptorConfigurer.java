package com.enation.pangu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *
 * 加载自定义的 拦截器
 * @author kingapex
 * @version v1.0
 * 2020年10月31日 下午5:29:56
 *
 */
@Configuration
public class WebInterceptorConfigurer implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add( new DataTablesPagerArgumentResolver());

	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor( new PanguRequestInterceptor() ).addPathPatterns("/view/**");
		registry.addInterceptor( new UserLoginInterceptor() )
		.addPathPatterns("/**")                      //所有路径都被拦截
		.excludePathPatterns(                         //添加不拦截路径
				"/view/login",
				"/data/user/login",          //登录
				"/**/*.html",            //html静态资源
				"/**/*.js",              //js静态资源
				"/**/*.css",			//css静态资源
				"/**/*.woff2",
				"/**/*.woff",
				"/**/*.ftlh",
				"/**/*.map",
				"/**/*.ttf"

		);

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		//设置系统访问的默认首页
		registry.addViewController("/").setViewName("redirect:/view/javashop/");
	}

}
