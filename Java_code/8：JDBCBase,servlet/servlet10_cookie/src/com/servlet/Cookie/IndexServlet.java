package com.servlet.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 读Cookie
 * @author likang
 *
 */
@WebServlet("/index.html")
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
		
		ServletContext sc=this.getServletContext();
		Integer counter=(Integer)sc.getAttribute("counter");
		if(null==counter)
			counter=1;
	    out.println("<h2 align='center'>此页面被访问过多少"+(counter++)+"次</h2>");
		sc.setAttribute("counter", counter);
		
		//读Cookie
		Cookie [] cookies=req.getCookies();
		String uname=null;
		String hz=null;
		
		//数组检查获得是否为空和长度
		if(null!=cookies&&cookies.length>0)
		{
			for(Cookie c:cookies)
			{
				if("user_name".equals(c.getName()))
				{
					uname=c.getValue();
				}
				if("hz".equals(c.getName()))
				{
					hz=URLDecoder.decode(c.getValue(), "utf-8");
				}
			}
		}
		//out.println("<h2>"+hz+"</h2>");
		
		out.println("  <form action='checkLogin' method='post'>");
		if(null!=uname)
			out.println("  用户名:<input type='text' name='name' value='"+uname+"'/><br/>");
		else
		   out.println("  用户名:<input type='text' name='name'/><br/>");
		
		out.println("  密码: <input type='password' name='pwd'/><br/>");
		out.println("  <input type='submit' value='登录'/>");
		out.println("  ");
		out.println("  </form>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

}
