package com.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.pojo.Admin;

@WebServlet("/del")
public class DeleteAdminServlet extends HttpServlet {

	@Override
	public void service( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		
		 req.setCharacterEncoding("utf-8");
		 String sid=null!=req.getParameter("id")?req.getParameter("id"):"";
		 
		 try {
			 Connection con=ConnectionManager.getConnection();
			 QueryRunner run=new QueryRunner();
			 String sql="delete from  admin  where id=?";
			 run.update(con,sql,Integer.parseInt(sid));
			
			 List<Admin> alladmin=new QueryRunner(ConnectionManager.ds).query("select * from admin", new BeanListHandler<Admin>(Admin.class));
			 req.setAttribute("alladmin", alladmin);
			 
			 req.getRequestDispatcher("success").forward(req, resp);
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		 
	}

}
