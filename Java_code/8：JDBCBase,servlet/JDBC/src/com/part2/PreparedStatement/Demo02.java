package com.part2.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
/**
 * Statement 对象 执行静态的SQL语句，若含有 ' 会破坏SQL语句，使用PreparedStatement，预编译的SQL语句
 * @author likang
 *
 */
public class Demo02 {

	public static void main(String[] args)throws Exception {
		
		Integer in=new Integer(10);
		String name="lisi's stu";
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		
		String sql="insert into stu(id,name) values(?,?)";
		
		PreparedStatement  ps=con.prepareStatement(sql);
		
		ps.setInt(1, in.intValue());
		ps.setString(2, name);
		
		int re=ps.executeUpdate();
		
		if(re>0)
			System.out.println("OK");
		
		con.close();
		

	}

}
