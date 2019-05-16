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
import javax.servlet.http.HttpSession;

import com.bean.Admin;
/**
 * 过滤器，过滤掉未登陆用户
 * @author likang
 */
@WebFilter("/admin/*")
public class CheckLoginFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		 HttpServletRequest req=(HttpServletRequest)arg0;
		 HttpServletResponse resp=(HttpServletResponse)arg1;
		 HttpSession hs=req.getSession();
		
		 Admin admin=(Admin)hs.getAttribute("loged");
		 if(null!=admin)
		 {
			 arg2.doFilter(req, arg1);
		 }else
		 {
			 resp.sendRedirect("../login");
		 }
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	

}
