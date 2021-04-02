package com.enation.pangu.config;

import com.enation.pangu.utils.CurrencyUtil;
import com.enation.pangu.utils.StringUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * DataTables 分页参数处理器
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月19日上午12:45:33
 */
public class DataTablesPagerArgumentResolver implements HandlerMethodArgumentResolver {

	//默认分页大小
	private static final int default_size =3;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		String name = parameter.getParameterName();

		if(name.equals("pageNo")   ||name.equals("pageSize")  ){
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String name = parameter.getParameterName();
		if(name.equals("pageNo") ){

			int pageNo =  this.getPageNo(webRequest) ;
			return pageNo;

		}

		if(name.equals("pageSize") ){
			int pageSize = this.getPageSize(webRequest);

			return pageSize;
		}

		throw new IllegalArgumentException("分页参数处理异常");
	}



	/**
	 * 从request中读取 page_size ，如果为空则用默认的
	 * @param request
	 * @return
	 */
	private int getPageSize(NativeWebRequest request){

		String length = request.getParameter("length");
		return StringUtil.toInt(length,default_size);

	}

	/**
	 * 从reqeust中读取 start 和length 来生成页码
	 * @return
	 */
	private int getPageNo(NativeWebRequest request){

		if(request == null){
			return 1;
		}



		int start = StringUtil.toInt(request.getParameter("start"),0);
		int length = StringUtil.toInt(request.getParameter("length"),10);
		int pageNo = this.convertPage(start, length);

		return pageNo;
	}


	/**
	 * 将要查询的起始条数，转换成页数
	 * @return
	 */
	private   int  convertPage(int startNum,int pageSize){

		startNum = startNum+1;
		int page = 1;

		if(startNum<pageSize){
			page = 1;
		}else{
			double num = CurrencyUtil.div(startNum, pageSize);
			page = (int) Math.ceil(num);
		}
		return page;
	}


}
