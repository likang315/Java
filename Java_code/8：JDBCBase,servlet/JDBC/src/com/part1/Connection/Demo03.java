package com.part1.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;

/**
 * SQL语句批处理，addBatch()
 * @author likang
 *
 */
public class Demo03 {

	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		Statement  stat=con.createStatement();
		
		stat.addBatch("create table stu9(id int ,name varchar(20))");//发送SQL语句，创建表
		for(int i=1;i<101;i++)
		{
			stat.addBatch("insert into stu2 values("+i+",'name"+i+"')");
		}
		
		int [] re=stat.executeBatch();//执行批处理语句，返回执行执行结果标志的数组
		System.out.println(Arrays.toString(re));
		con.close();
	}

}
