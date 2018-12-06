package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/xmlservlet")
public class XmlServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.setContentType("text/xml;charset=UTF-8");
		 PrintWriter out=resp.getWriter();
		 req.setCharacterEncoding("UTF-8");
		
	     out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	     out.println("<stus>");
			 out.println(" <stu>");
			  out.println("<name>李四</name>");
			  out.println("<age>22</age>");
			  out.println(" </stu>");
				 out.println(" <stu>");
				  out.println("<name>张三</name>");
				  out.println("<age>23</age>");
				  out.println(" </stu>");
	     out.println("</stus>");
		
		 out.close();
	}

}
