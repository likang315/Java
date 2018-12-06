package com.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.handlers.BeanHandler;

import com.bean.Admin;
import com.dbutils.Db;
import com.servlet.core.ServletBase;
import com.utils.Md5Encrypt;
/** 首次访问时，action 判null 设置为index,注意方式
 * @author likang
 */
@WebServlet("/login")
public class LoginServlet extends ServletBase {

	@Override
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 this.forwards(req, resp, "login.jsp");
	}
	
	public void checkLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		String email=this.getString(req, "email");
		String upwd=this.getString(req, "upwd");
		String sql="select * from admin where email=? and upwd=? limit 1";
		
		Admin admin=null;
		try {
			admin=Db.query(sql, new BeanHandler<Admin>(Admin.class),email,Md5Encrypt.md5(upwd));
		    if(null!=admin)
		    {
				req.getSession().setAttribute("loged", admin);
			    resp.sendRedirect("admin/show");
		    }else
		    {
		    	req.setAttribute("msg", "用户名和密码不正确!");
		        index(req, resp);
		    }      	
		} catch (SQLException e) {
			index(req, resp);
		}
	}

}
