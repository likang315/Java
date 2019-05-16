package com.servlet.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pojo.Admin;
@WebServlet("/success")
public class SuccessServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>Insert title here</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>登录成功!</h1>");
		
		ServletContext sc=this.getServletContext();
		Integer counter=(Integer)sc.getAttribute("counter");
		if(null==counter)
			counter=1;
	    out.println("<h2 align='center'>这个页面被访问过多少"+(counter++)+"次</h2>");
		sc.setAttribute("counter", counter);
		
		
		out.println("</body>");
		out.println("</html>");
		
		out.close();
	}

}
