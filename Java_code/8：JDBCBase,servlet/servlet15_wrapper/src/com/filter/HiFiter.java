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

@WebFilter("/*")
public class HiFiter implements Filter {

	@Override
	public void destroy() {
		System.out.println("HiFiter.............destory");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)arg0;
		HttpServletResponse resp=(HttpServletResponse)arg1;
		
		//重写请求或者响应
		MyRequest myre=new MyRequest(req);
		arg2.doFilter(myre, resp);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("HiFiter-----------init--------");
	}
}
