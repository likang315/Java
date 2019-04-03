package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

/**
 * 用于得到Connection的对象
 * @author Administrator
 *
 */
public class ConnectionUtils
{
	//从web.xml 中导入初始化值
	public static Connection getConnection(PageContext pc)
	{
		Connection con=null;
		try {
			ServletContext sc=pc.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			String url=sc.getInitParameter("url");
			String user=sc.getInitParameter("user");
			String upwd=sc.getInitParameter("pwd");
			con=DriverManager.getConnection(url,user,upwd);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return con;
	}
	
}
