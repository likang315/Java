package com.servlet.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import com.utils.MD5;

/**
 * 从数据库中查取数据，登录验证
 * @author likang
 *
 */
@WebServlet("/checkLogin")
public class CheckLoginServlet extends GenericServlet 
{
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException 
	{
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
				 out.println("<script>alert('yes');window.location='success.html';</script>");
			 }else
			 {
				 out.println("<script>alert('no');window.location='index.html';</script>");
				 
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
