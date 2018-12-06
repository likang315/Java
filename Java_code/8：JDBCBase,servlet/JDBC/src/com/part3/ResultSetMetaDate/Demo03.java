package com.part3.ResultSetMetaDate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * 可以操作ResultSet 结果集的方式
 * @author likang
 *
 */
public class Demo03 {

	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		Statement stat=con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
		
		
		ResultSet rs=stat.executeQuery("select * from person");
		
		rs.absolute(5);//移动指针
		rs.previous();
		rs.deleteRow();
		
		System.out.println("OK");
	}
}
