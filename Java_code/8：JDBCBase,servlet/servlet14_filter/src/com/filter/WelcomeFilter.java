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

//@WebFilter("/admin/*")
public class WelcomeFilter implements Filter {

	public WelcomeFilter()
	{
		System.out.println("WelcomeFilter............");
	}
	
	FilterConfig fc=null;
	
	@Override
	public void destroy() {
		System.out.println("WelcomeFilter..destroy.............");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest req=(HttpServletRequest)arg0;
		HttpServletResponse resp=(HttpServletResponse)arg1;
		System.out.println("WelcomeFilter. dofilter............."+req.getRequestURL());
		
		arg2.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("WelcomeFilter..init.............");
		this.fc=arg0;
	}

}
