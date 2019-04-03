package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.utils.MD5;

@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet 
{

	@Override
	public void service( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		
		 req.setCharacterEncoding("utf-8");
		 resp.setContentType("text/html;charset=utf-8");
		 PrintWriter out=resp.getWriter();
		 String name= null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String pwd=  null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		 System.out.println(name + pwd);
		 String mipwd=MD5.getInstance().getMD5(pwd);
		 
		 try {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
			 PreparedStatement ps=con.prepareStatement("select * from mysql where name=? and pwd=? limit 1");
			 ps.setString(1, name);
			 ps.setString(2, mipwd);
			 ResultSet rs=ps.executeQuery();
			 
			 if(rs.next())
			 {
				   resp.sendRedirect("success.html");
			 }else
			 {
				   resp.sendRedirect("index.html");;
			 }
			 con.close();
			 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 out.close();

	}

}
