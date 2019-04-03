package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.pojo.Admin;
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
		 
		 String uname=null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String upwd=null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
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
				 //结果集
				 List<Admin> alladmin=null;
				 alladmin = new QueryRunner(ConnectionManager.ds).query("select * from admin", new BeanListHandler<Admin>(Admin.class));
				 
				 //请求中添加属性，存放所有的数据
				 req.setAttribute("alladmin", alladmin);

				  //调用资源，跳转其他页面，朝前
				  RequestDispatcher rd=req.getRequestDispatcher("success");
				  rd.forward(req, resp);
				  
			 }else
			 {
				   //返回含有内容的响应
				   RequestDispatcher rd=req.getRequestDispatcher("index.html");
				   rd.include(req, resp);
			 }
			 con.close();
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 out.close();

	}

}
