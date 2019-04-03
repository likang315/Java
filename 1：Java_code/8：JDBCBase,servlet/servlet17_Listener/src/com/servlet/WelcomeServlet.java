package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/wel")
public class WelcomeServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		String name=req.getParameter("name");
		
		User user=new User(name);
		
		out.println("<h1>"+user+"</h1>");
		
		//客户端的一些报头信息
		HttpSession hs=req.getSession();
		hs.setAttribute("user", user);
		hs.removeAttribute("user");
		
		//ServletContext 中的一些网站的报头信息
		ServletContext sc=this.getServletContext();
		sc.setAttribute("aaa", "hello~~");
		sc.setAttribute("aaa", "welcome~~");
		sc.removeAttribute("aaa");
		out.close();
		
	}

}
