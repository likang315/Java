package com.servlet.counter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面访问计数器
 * @author likang
 *
 */
@WebServlet("/counter")
public class PageCounterServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//响应的媒体类型
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		ServletContext sc=this.getServletContext();
	    Integer counter=(Integer)sc.getAttribute("number");
		if(null==counter)
			counter=1;
		
		out.println("<h1 align='center'> 页面计数器</h1>");
		out.println("<h2 align='center'>页面被访问"+(counter++)+"次"+"</h2>");
		sc.setAttribute("number", counter);
	
	}

}
