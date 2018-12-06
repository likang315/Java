package com.part4.c3p0;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * 创建一个数据库池，初始化容量
 * @author likang
 *
 */
public class Demo01 {

	public static Connection [] pools;
	static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			pools=new Connection[10];
			for(int i=0;i<10;i++)
			{
				pools[i]=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt","root","mysql");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		Thread.sleep(6000);

	}

}
