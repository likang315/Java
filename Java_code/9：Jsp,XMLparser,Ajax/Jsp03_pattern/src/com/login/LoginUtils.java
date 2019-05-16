package com.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

public class LoginUtils {

	/**
	 * 完成所有的业务，只有纯Java代码
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public static void checkLogin(PageContext pc) throws ServletException, IOException
	{
		System.out.println(pc.getAttribute("aa"));
		System.out.println(pc.findAttribute("aa"));
		System.out.println(pc.getAttribute("aa", PageContext.APPLICATION_SCOPE));
		
		HttpServletRequest req=(HttpServletRequest) pc.getRequest();
		String uname= null!=req.getParameter("uname")?req.getParameter("uname"):"";
		String upwd=  null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		
		if("admin".equals(uname)&&"123".equals(upwd))
		{
			pc.getSession().setAttribute("loged",uname);
			pc.forward("success.jsp");
		}else
		{
			pc.forward("index.jsp");
		}
	}
}
