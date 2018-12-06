package com.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Admin;
import com.dbutils.Db;
import com.servlet.core.ServletBase;
import com.utils.Md5Encrypt;
/**
 * 和login方式一样
 * @author likang
 *
 */
@WebServlet("/regist")
public class RegistServlet extends ServletBase {

	@Override
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.forwards(req, resp, "regist.jsp");
	}
	
	public void registed(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Admin admin=new Admin();
		this.getBean(req,admin);
		String sql=" insert into admin(email,upwd,pic,name) values(?,?,?,?)";
		try {
			Db.update(sql, admin.getEmail(),Md5Encrypt.md5(admin.getUpwd()),admin.getPic(),admin.getName());
			req.setAttribute("msg", "注册成功!");
		} catch (SQLException e) {
			req.setAttribute("msg", "注册失败!");
		}
		
		this.forwards(req, resp, "regist.jsp");
	}
}
