package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置Filter ,两种配置方式，并且测Filter 方法的调用顺序，过滤顺序
 * @author likang
 */
//@WebFilter("/admin/*")
public class HelloFilter implements Filter {

	public HelloFilter()
	{
		System.out.println("HelloFilter............");
	}
	
	FilterConfig fc=null;
	@Override
	public void destroy() {
		System.out.println("filter..destroy.............");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		//注意强转
		HttpServletRequest req=(HttpServletRequest)arg0;
		HttpServletResponse resp=(HttpServletResponse)arg1;
		System.out.println("filter. dofilter............."+req.getRequestURL());
		//调用下一个过滤器的doFilter()
		arg2.doFilter(req, resp);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("filter..init.............");
		this.fc=arg0;
	}

}
