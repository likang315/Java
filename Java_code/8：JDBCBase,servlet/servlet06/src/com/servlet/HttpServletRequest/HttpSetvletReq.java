package com.servlet.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletRequest 测试
 * @author likang
 *
 */
@WebServlet("/aaa")
public class HttpSetvletReq extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		out.println("<h1>报头:</h1>");
		out.println("<ul>");
		
		java.util.Enumeration<java.lang.String> keys=req.getHeaderNames();
		 while(keys.hasMoreElements())
		 {
			 String key=keys.nextElement();//属性名
			 String va=req.getHeader(key);//属性值
			 out.println("<li>"+key+"::"+va+"</li>");
		 }
		out.println("</ul>");
		
		
		out.println("<h3>req.getMethod():"+req.getMethod()+"</h3>");
		out.println("<h3>req.getQueryString():"+req.getQueryString()+"</h3>");//用户名和密码
		out.println("<h3>req.getRequestURI():"+req.getRequestURI()+"</h3>");//URI 不带Http协议和ip
		out.println("<h3>req.getRequestURL():"+req.getRequestURL()+"</h3>");
	
		out.println("<h3>req.getContextPath():"+req.getContextPath()+"</h3>");
		
		out.println("<h3>req.getContextPath():"+req.getServletPath()+"</h3>");//servlet 配置路径
		out.close();
		
		
	}

}
