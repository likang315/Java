package com.part1.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * 对ResultSet 从结果集中，根据列序号和列名取值
 * @author likang
 *
 */
public class Demo04 {

	public static void main(String[] args) throws Exception
	{

		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		Statement  stat=con.createStatement();
		
		ResultSet  rs=stat.executeQuery("select * from stu2");
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+"\t"+rs.getString("name"));
		}
		
		con.close();
	}

}
