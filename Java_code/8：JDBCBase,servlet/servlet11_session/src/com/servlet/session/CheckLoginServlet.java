package com.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.pojo.Admin;
import com.utils.MD5;

/**
 * 创建Session 对象，返回Set-Cookie  SessionID
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
				//得到Session,第一次创建Session对象，返回Set-Cookie session ID 
				HttpSession hs=req.getSession();
				hs.setAttribute("loged", uname);//设置 属性来控制直接访问成功页面 
				 
				  RequestDispatcher rd=req.getRequestDispatcher("success");
				  rd.forward(req, resp);
				  
			 }else
			 {
				  RequestDispatcher rd=req.getRequestDispatcher("index");
				  rd.include(req, resp);
			 }
			 con.close();
			 
		} catch (Exception e) {

		}
		 out.close();
	}

}
