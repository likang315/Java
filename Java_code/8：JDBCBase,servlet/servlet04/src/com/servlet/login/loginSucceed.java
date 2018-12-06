package com.servlet.login;

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
@WebServlet("/checkLogin1")

public class loginSucceed extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		 req.setCharacterEncoding("utf-8");
		 resp.setContentType("text/html;charset=utf-8");
		 PrintWriter out=resp.getWriter();
		 
		 String uname=  null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String upwd=  null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		 String mipwd=MD5.getInstance().getMD5(upwd);
		 
		 try {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
			 PreparedStatement ps=con.prepareStatement("select * from mysql where name=? and pwd=? limit 1");
			 ps.setString(1, uname);
			 ps.setString(2, mipwd);
			 ResultSet rs=ps.executeQuery();
			 
			 if(rs.next())
			 {
				 resp.sendRedirect("success.html");
			 }else
			 {
				 resp.sendRedirect("index.html");
				 out.println("<script>alert('no');</script>");
			 }
			 con.close();
			 } catch (ClassNotFoundException e) {
					e.printStackTrace();
			 } catch (SQLException e) {
					e.printStackTrace();
			 }
			 out.close();	
			 
			 
	}
	
}
