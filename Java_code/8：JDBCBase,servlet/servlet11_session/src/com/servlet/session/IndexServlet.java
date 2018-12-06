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
 * HttpSession 
 * @author likang
 *
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

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
		out.println("<center>");
		out.println("<ul>");
		
		//得到Session,测方法
		HttpSession hs=req.getSession();
		out.println("<li>isnew:"+hs.isNew()+"</li>");
		out.println("<li>HTTPSessionID:"+hs.getId()+"</li>");
		out.println("<li>CreationTime:"+new Date(hs.getCreationTime())+"</li>");
		out.println("<li>LastAccessedTime:"+new Date(hs.getLastAccessedTime())+"</li>");
		out.println("<li>MaxInactive:"+hs.getMaxInactiveInterval()+"</li>");
		out.println("</ul>");
		out.println("<hr/>");
		
		
		ServletContext sc=this.getServletContext();
		Integer counter=(Integer)sc.getAttribute("counter");
		if(null==counter)
			counter=1;
	    out.println("<h2 align='center'>此页面被访问过"+(counter++)+"次</h2>");
		sc.setAttribute("counter", counter);
		
		
		out.println("  <form action='checkLogin' method='post'>");
			out.println("  用户名:<input type='text' name='name'/><br/>");
			out.println("  密码: <input type='password' name='pwd'/><br/>");
			out.println("  <input type='submit' value='OK'/>");
		out.println("  </form>");
		
		
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
		
	}

}
