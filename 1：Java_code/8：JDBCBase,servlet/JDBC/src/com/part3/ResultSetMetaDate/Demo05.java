package com.part3.ResultSetMetaDate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
/**
 * DatabaseMetaData 数据库的原数据的信息，数据库名，表名
 * @author likang
 *
 */
public class Demo05 {

	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		
		//数据库的原数据
		DatabaseMetaData   dmd=con.getMetaData();
		
		ResultSet rs=dmd.getCatalogs();
		while(rs.next())
		{
			System.out.println(rs.getString("TABLE_CAT"));
		}
		
		System.out.println("---------------------");
		ResultSet rs1=dmd.getTables("xupt", null, null, null);
		while(rs1.next())
		{
			System.out.println(rs1.getString("TABLE_NAME"));
		}
		
	}

}
