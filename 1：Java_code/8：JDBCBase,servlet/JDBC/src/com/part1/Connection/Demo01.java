package com.part1.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/**
 * 使用JDBC 三步走，Statement对象
 * @author likang
 *
 */
public class Demo01 {
	public static void main(String[] args) 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//得到数据库连接，得到对象即表示连接成功
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
			//此对象用来发送SQL语句
			Statement stat=con.createStatement();
			int a=12;
			String b="likang";
			
		 int re=stat.executeUpdate("insert into stu values("+a+",'"+b+"')");
			if(re>0)
				System.out.println("OK");
			
			con.close();
		} catch (Exception e) {
			 System.out.println("Error_001:第一种得到Connection失败");
		}
	}
}
