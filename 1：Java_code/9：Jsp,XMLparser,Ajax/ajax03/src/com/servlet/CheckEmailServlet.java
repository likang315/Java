package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/checkemail")
public class CheckEmailServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		String email=null!=req.getParameter("email")?req.getParameter("email"):"";
		if("admin@qq.com".equals(email))
		{
		      out.print("0");	
		}else
		{
			  out.print("1");	
		}
		
		out.close();
		
		
		
		
		
		
	}

}
