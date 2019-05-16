package com.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.db.ConnectionManager;
import com.pojo.Admin;
import com.utils.Md5;

/**
 * 校验登录，获取Session
 * @author likang
 *
 */
@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		 req.setCharacterEncoding("utf-8");
		 boolean re=false;
		 String uname=null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String upwd=null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		 String mipwd=Md5.tomd5(upwd);
		 
		 try {
			 QueryRunner run=new QueryRunner(ConnectionManager.ds);
			 String sql="select * from admin where uname=? and upwd=? limit 1";
			 Admin admin=run.query(sql,new BeanHandler<Admin>(Admin.class),uname,mipwd);
		 
			 if(null!=admin&&admin.getUname().equals(uname))
			 {
				 re=true;
				 //建立会话，并且设置报头属性 loged-admin
				 HttpSession hs=req.getSession();
				 hs.setAttribute("loged", admin);
			 }
		  } catch (SQLException e) {
			  System.out.println("Error_04:校验登录失败");
		  }
		 
		 if(re)
			 resp.sendRedirect("index");
		 else
			 resp.sendRedirect("login");
	}
}
