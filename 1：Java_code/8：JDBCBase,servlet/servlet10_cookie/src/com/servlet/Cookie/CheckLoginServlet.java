package com.servlet.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.pojo.Admin;
import com.utils.MD5;

/**
 * 写Cookie
 * @author likang
 *
 */
@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet 
{

	@Override
	public void service( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		 req.setCharacterEncoding("utf-8");
		 resp.setContentType("text/html;charset=utf-8");
		 PrintWriter out=resp.getWriter();
		 String uname=null!=req.getParameter("name")?req.getParameter("name"):"";
		 String upwd=null!=req.getParameter("pwd")?req.getParameter("pwd"):"";
		 String mipwd=MD5.getInstance().getMD5(upwd);
		 
		 try {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
			 PreparedStatement ps=con.prepareStatement("select * from admin where name=? and pwd=? limit 1");
			 ps.setString(1, uname);
			 ps.setString(2, mipwd);
			 ResultSet rs=ps.executeQuery();
			 
			 if(rs.next())
			 {
				 
				 //验证成功，取所有管理员
				 List<Admin> alladmin=null;
				 alladmin=new QueryRunner(ConnectionManager.ds).query("select * from admin", new BeanListHandler<Admin>(Admin.class));
				 req.setAttribute("alladmin", alladmin);
				 
				 //写cookie
				 Cookie cookie=new Cookie("user_name",uname);
				 cookie.setMaxAge(60*60*24*7);
				 cookie.setPath("/");
				 resp.addCookie(cookie);
				 
				 //单字节编码
				 Cookie cookie1=new Cookie("hz",URLEncoder.encode("你好", "utf-8"));
				 cookie1.setMaxAge(60*60*24*7);
				 cookie1.setPath("/");
				 resp.addCookie(cookie1);
				 
				 
				  //用相对路径，调用SuccessServlet对象的servlces方法
				  RequestDispatcher rd=req.getRequestDispatcher("success");
				  rd.forward(req, resp);
				  
			 }else
			 {
				  RequestDispatcher rd=req.getRequestDispatcher("index.html");
				  rd.include(req, resp);
				 
			 }
			 con.close();
			 
		} catch (Exception e) {
			System.out.println("Error_01:写Cookie  错误");
		}
		 
		 out.close();

	}

}
