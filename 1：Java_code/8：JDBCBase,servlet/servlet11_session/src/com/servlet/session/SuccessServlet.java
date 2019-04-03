package com.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * HttpSession  ,Session登陆控制
 * @author likang
 *
 */
@WebServlet("/success")
public class SuccessServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		
		//控制访问，登录过才可以访问
		HttpSession hs=req.getSession();
		if(null==hs.getAttribute("loged"))
		{
			resp.sendRedirect("index");
			return;
		}
		
		
		PrintWriter out=resp.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>Insert title here</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>登录成功!</h1>");
		
		
		out.println("<ul>");
			out.println("<li>isnew:"+hs.isNew()+"</li>");
			out.println("<li>ID:"+hs.getId()+"</li>");
			out.println("<li>CreationTime:"+new Date(hs.getCreationTime())+"</li>");
			out.println("<li>LastAccessedTime:"+new Date(hs.getLastAccessedTime())+"</li>");
			out.println("<li>MaxInactive:"+hs.getMaxInactiveInterval()+"</li>");
		out.println("</ul>");
		out.println("<hr/>");
		
		
		ServletContext sc=this.getServletContext();
		Integer counter=(Integer)sc.getAttribute("counter");
		if(null==counter)
			counter=1;
	    out.println("<h2 align='center'>此页面被放过"+(counter++)+"次</h2>");
		sc.setAttribute("counter", counter);
		
		
		out.println("<h2>只有登录以后才可以访问</h2>");
		out.print("<center>");
		out.println("<a href='index'>返回</a>&nbsp;&nbsp;<a href='logout'>销毁</a>");
		out.println("</center></body>");
		out.println("</html>");
		
		out.close();
	}

}
