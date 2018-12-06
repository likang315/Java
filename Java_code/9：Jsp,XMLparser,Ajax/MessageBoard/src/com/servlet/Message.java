package com.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Msg;
import com.dbutils.Db;
import com.servlet.core.ServletBase;

@WebServlet("/admin/message")
public class Message extends ServletBase {

	@Override
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.forwards(req, resp, "ask.jsp");
	}
	
	public void message(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 Msg msg=new Msg();
		 this.getBean(req, msg);
	
		 String sql="insert into msg(admin_id,title,txt,createtime) values(?,?,?,?)";
		 
		 try {
			Db.update(sql,msg.getAdmin_id(),msg.getTitle(),msg.getTxt(),msg.getCreatetime());
		    req.setAttribute("msg", "留言成功!");
		 } catch (SQLException e) {
			 req.setAttribute("msg", "留言失败!");
		}
		 
		 
		 req.getRequestDispatcher("show?action=index").forward(req, resp);
	}


}
