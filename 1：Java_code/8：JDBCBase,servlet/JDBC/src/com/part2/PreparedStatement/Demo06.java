package com.part2.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * PreparedStatement 发送sql语句结构
 * @author likang
 *
 */
public class Demo06 {

	public static void main(String[] args)throws Exception {
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		
		//标准SQL语句，PreparedStatement 发送SQL语句结构
		String sql="select * from admin where name=? and pwd=?";
		PreparedStatement ps=con.prepareStatement(sql);
		
		ps.setString(1, "likang");
		ps.setString(2, "mysql");//密文
		ResultSet rs=ps.executeQuery();
		if(rs.next())
		{
			System.out.println("YES");
		}else
		{
			System.out.println("NO");
		}
		con.close();
		

	}

}
