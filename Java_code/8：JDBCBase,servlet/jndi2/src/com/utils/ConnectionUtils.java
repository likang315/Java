package com.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.jsp.PageContext;
import javax.sql.DataSource;


public class ConnectionUtils
{
	public static Connection getConnection(PageContext pc)
	{
		Connection con=null;

		try {
			//初始化名称查找上下文环境
			InitialContext ic = new InitialContext();
			//通过JNDI名称找到DataSource,对名称进行定位java:comp/env是必须加的,后面跟的是DataSource名
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/test");
			con=ds.getConnection();
			
			System.out.println(con);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
		
	}
}
