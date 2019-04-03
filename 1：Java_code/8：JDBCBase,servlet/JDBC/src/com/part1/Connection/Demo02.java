package com.part1.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
/**
 * 用Propertise 得到Connection连接对象，拼接SQL语句
 * @author likang
 *
 */
public class Demo02 {
	public static void main(String[] args) 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Properties p=new Properties();
			p.setProperty("user", "root");
			p.setProperty("password", "mysql");
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt",p);
			Statement stat=con.createStatement();
			int a=13;
			String b="lihui";
			
		 int re=stat.executeUpdate("insert into stu values("+a+",'"+b+"')");
			if(re>0)
				System.out.println("OK");
			
			con.close();
		} catch (Exception e) {
			 System.out.println("Error_001:Propertise 方式得到Connection 失败");
		}
	}
}
