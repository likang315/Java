package com.checkLogin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet 
{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		 req.setCharacterEncoding("utf-8");
		 resp.setContentType("text/html;charset=utf-8");
		 PrintWriter out=resp.getWriter();
		 
		 String uname= null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String upwd=  null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		if("admin".equals(uname)&&"123".equals(upwd))
		{
			req.getRequestDispatcher("success.html").forward(req, resp);
		}else
		{
			req.getRequestDispatcher("index.html").forward(req, resp);
		}
		
		
		out.close();
	}

}
