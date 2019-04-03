package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;

import com.db.ConnectionManager;
import com.utils.Md5;

/**
 * 注册，通过action判断是否填写了信息
 * @author likang
 *
 */
@WebServlet("/regist")
public class ToRegistServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	   req.setCharacterEncoding("utf-8");
	   //判断是否填写信息
	   String action= null!=req.getParameter("action")?req.getParameter("action"):"to";
		
	   if(action.equals("to"))
	   {
		req.getRequestDispatcher("WEB-INF/page/regist.html").forward(req, resp);
	   }else
	   {
		     req.setCharacterEncoding("utf-8");
		     String uname=null!=req.getParameter("uname")?req.getParameter("uname"):"";
			 String upwd=null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
			 String name=null!=req.getParameter("name")?req.getParameter("name"):"";
			 String mipwd=Md5.tomd5(upwd);
			 
			 String sql="insert into admin(uname,upwd,name) values(?,?,?)";
			 try {
				new QueryRunner(ConnectionManager.ds).update(sql,uname,mipwd,name);
			} catch (SQLException e) {
				System.out.println("Error_05:注册信息失败");
			}
	   }
	   resp.sendRedirect("index");
	}
}
