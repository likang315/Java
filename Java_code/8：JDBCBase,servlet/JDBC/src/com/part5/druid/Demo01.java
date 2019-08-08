package com.part5.druid;

import java.sql.Connection;
import java.sql.ResultSet;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * druid 连接池 静态加载
 * @author likang
 *
 */
public class Demo01 {
	//javax.sql.DatafSource
	
	private static DruidDataSource  ds = new DruidDataSource();
	static  {
		try {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl("jdbc:mysql://localhost:3306/xupt");
			ds.setUsername("root");
			ds.setPassword("mysql");
			ds.setMaxActive(10);
			ds.setInitialSize(3);
		} catch (Exception e) {
			System.out.println("Rrror_01:druid 连接池加载失败");
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		Connection con=ds.getConnection();
		ResultSet rs=con.createStatement().executeQuery("select * from person");
		while(rs.next()) {
			System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
		}
		
		System.out.println(con);
		con.close();
		
	}

}
