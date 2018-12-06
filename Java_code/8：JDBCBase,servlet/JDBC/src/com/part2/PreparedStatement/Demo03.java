package com.part2.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 通配符 % _ 通配符的使用
 * @author likang
 *
 */
public class Demo03 {

	public static void main(String[] args)throws Exception {
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		
		//Statement  stat=con.createStatement();
		String sql="select * from stu where name like ?";
		PreparedStatement  ps=con.prepareStatement(sql);
	 
		 ps.setString(1, "%i%");
		 ResultSet rs=ps.executeQuery();
		 while(rs.next())
		 {
			 System.out.println(rs.getString(2)+"\t"+rs.getString("name"));
		 }
		
		con.close();
		

	}

}
