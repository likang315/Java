package com.part4.c3p0;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 从c3p0连接池得到对象，以前是从DriveManager 得到对象
 * @author likang
 *
 */
public class Demo03 {
	//初始化一次数据库连接池对象
	private static ComboPooledDataSource  ds=new ComboPooledDataSource();
	static
	{
		try {
			ds.setDriverClass("com.mysql.jdbc.Driver");
			ds.setJdbcUrl("jdbc:mysql://localhost:3306/xupt");
			ds.setUser("root");
			ds.setPassword("mysql");
			ds.setMaxPoolSize(10);
			ds.setInitialPoolSize(3);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)throws Exception {
		 //javax.sql.DataSource 数据库连接池必须实现此接口
		
		Connection con=ds.getConnection();
		ResultSet rs=con.createStatement().executeQuery("select * from person");
		
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
		}
		con.close();
		System.out.println(con);
		

	}

}
