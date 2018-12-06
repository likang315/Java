package com.part2.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;
/**
 * PreparedStatement 命令行批处理
 * @author likang
 *
 */
public class Demo04 {

	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		String sql="insert into person(id,name,sex,age) values(?,?,?,?)";
		PreparedStatement ps=con.prepareStatement(sql);
		
		for(int i=1;i<10;i++)
		{
			ps.setInt(1, i);
			ps.setString(2, "name"+i);
			ps.setString(3, i%2==0?"M":"F");
			ps.setInt(4, 20+i);
			ps.addBatch();
		}
		int[] re=ps.executeBatch();
		System.out.println(Arrays.toString(re));
		con.close();
	}

}
