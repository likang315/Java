package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class WelcomeServlet extends GenericServlet {

   public WelcomeServlet()
   {
	   System.out.println("--->---...001");
   }
	  
	@Override
	public void init() throws ServletException {
		 System.out.println("--->init---...002");
	}



	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		
		   System.out.println("---->>service.....003");
		  
		   resp.setContentType("text/html;charset=utf-8");
		   //获得字符流
		   PrintWriter out=resp.getWriter();
		   out.println("<h1>welcome servlet!</h1>");
		   
		   
		   ServletConfig sc=this.getServletConfig();
		   String driver=sc.getInitParameter("driver");
		   out.println("<h2>driver:"+driver+"</h2>");
		   
		   //获得初始化参数名集合
		   java.util.Enumeration<java.lang.String> allkey= sc.getInitParameterNames();
		   out.println("<ul>");
		   
		   while(allkey.hasMoreElements())
		   {
			 String key=allkey.nextElement();
			 out.println("<li>"+key+":"+sc.getInitParameter(key)+"</li>");
		   }
		   
		  out.println("</ul>");
		  this.log("");
		  this.log("",new Exception("AAAAAAAAAAAAAAAAAAAAAAAA"));
		  
		  out.close();
		

	}



	@Override
	public void destroy() {
		 System.out.println("--->004----destroy......");
	}
	
	

}
