package com.part2.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Demo01 {

	public static void main(String[] args)throws Exception {
		
		String title="";
		String content="<pre>public interface lisi's stu";
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		Statement  stat=con.createStatement();
		
		String sql="insert into tt6(title,content) values('"+title+"','"+content+"')";
		System.out.println(sql);
		int re=stat.executeUpdate(sql);
		if(re>0)
			System.out.println("OK");
		
		con.close();
		

	}

}
