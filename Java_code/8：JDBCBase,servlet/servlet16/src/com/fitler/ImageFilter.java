package com.fitler;

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
 * 通过报头来获得访问IP，看是不是本主机，否则跳转到特定界面
 * @author likang
 */
@WebFilter("/img/*")
public class ImageFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)arg0;
		HttpServletResponse resp=(HttpServletResponse)arg1;
		
		String RF=req.getHeader("Referer");
		System.out.println(RF);
		if(null!=RF&&RF.indexOf("localhost")!=-1)
		{
			//如果检索到，则是本地主机，否则不是
			arg2.doFilter(req, resp);
		}else
		{
			resp.sendRedirect("../a3.jpg");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
