package com.part6.transaction;

import java.sql.Connection;
import java.sql.Statement;

import com.alibaba.druid.pool.DruidDataSource;

public class Demo01 {


	private static DruidDataSource  ds=new DruidDataSource();
	static
	{
		try {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl("jdbc:mysql://localhost:3306/xupt");
			ds.setUsername("root");
			ds.setPassword("mysql");
			ds.setMaxActive(10);
			ds.setInitialSize(3);
			
		} catch (Exception e) {
			
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
	
      Connection con=ds.getConnection();
      
      con.setAutoCommit(false);//mysql 数据库默认自动提交
      try {
      Statement sta=con.createStatement();
      sta.executeUpdate("update person set age=age-5 where id=1");
      sta.executeUpdate("update person set age=age+5 where id=2");
     
      con.commit();
      }catch(Exception e)
      {
    	  con.rollback();
      }
      con.close();
      System.out.println("OK");


	}

}
