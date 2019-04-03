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
 * ServletContext  三种获取RequestDispatcher的方式
 * @author likang
 *
 */
@WebServlet(value="/wel",name="welcome")
public class WelcomeServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext sc=this.getServletContext();
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		//初始化参数
		out.println("<h1>"+sc.getInitParameter("user")+"</h1>");
		out.println("<h1>"+sc.getRealPath("img")+"</h1>");
		out.println("<h1>"+sc.getMimeType("img/a1.jpg")+"</h1>");
		
		URL url=sc.getResource("img/a1.jpg");
		out.println("<h1>"+url+"</h1>");
	    InputStream is=sc.getResourceAsStream("img/a1.jpg");
	    
	   
	   RequestDispatcher rd0=req.getRequestDispatcher("img/a1.jpg");
	   RequestDispatcher rd1=sc.getRequestDispatcher("/img/a1.jpg");
	   RequestDispatcher rd2=sc.getNamedDispatcher("welcome");
	
	   out.close();
	}

}
