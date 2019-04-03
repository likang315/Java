package com.part3.ResultSetMetaDate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
/**
 * CallableStatement 调用存储过程
 * @author likang
 *
 */
public class Demo02 {
	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
        
		CallableStatement  call=con.prepareCall("call add_person(?,?,?,?,?)");//调用存储过程
		
		call.registerOutParameter(5, Types.INTEGER);
		
		call.setString(1, "lisa");
		call.setString(2, "234");
		call.setInt(3, 25);
		call.setString(4, "F");
		
		call.executeUpdate();
		
		
		int re=call.getInt(5);
		System.out.println(re);
		
		con.close();
		
	}

}
