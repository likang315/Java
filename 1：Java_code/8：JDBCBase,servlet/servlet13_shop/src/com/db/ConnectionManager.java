package com.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid 连接池
 * @author likang
 *
 */
public class ConnectionManager {
	public static DruidDataSource  ds=new DruidDataSource();
	static{
		   try {
				ResourceBundle res=ResourceBundle.getBundle("druid");
				ds.setUrl(res.getString("url"));
				ds.setDriverClassName(res.getString("driverClassName"));
				ds.setUsername(res.getString("username"));
				ds.setPassword(res.getString("password"));
				ds.setFilters(res.getString("filters"));
				ds.setMaxActive(Integer.parseInt(res.getString("maxActive")));
				ds.setInitialSize(Integer.parseInt(res.getString("initialSize")));
				ds.setMaxWait(Long.parseLong(res.getString("maxWait")));
				ds.setMinIdle(Integer.parseInt(res.getString("minIdle")));
			} catch (Exception e) {
				System.out.println("Error_01: druid 配置文件加载错误");
			} 
	   }
	
	public static Connection getConnection()
	{
		Connection con=null;
		 try {
			con=ds.getConnection();
		} catch (SQLException e) {
			System.out.println("Error_02:连接数据库失败");
		}
		return con;
	}
	
	public static void closeDataSource()
    {
	   if(null!=ds)
		   ds.close();
	}
}
