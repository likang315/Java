package com.dao.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * Druid 的方式连接数据库
 * @author Administrator
 *
 */
public class DruidConnectionManger extends ConnectionManager 
{
   private static DruidDataSource ds=null;
   static{
	   try {
			ResourceBundle res=ResourceBundle.getBundle("jdbc");

			ds=new DruidDataSource();
			ds.setUrl(res.getString("url"));
			ds.setDriverClassName(res.getString("driverClassName"));
			ds.setUsername(res.getString("username"));
			ds.setPassword(res.getString("password"));
			ds.setFilters(res.getString("filters"));
			ds.setMaxActive(Integer.parseInt(res.getString("maxActive")));
			ds.setInitialSize(Integer.parseInt(res.getString("initialSize")));
			ds.setMaxWait(Long.parseLong(res.getString("maxWait")));
			ds.setMinIdle(Integer.parseInt(res.getString("minIdle")));
			//ds.setMaxIdle(Integer.parseInt(res.getString("maxIdle")));

			//ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(res.getString("timeBetweenEvictionRunsMillis")));
			//ds.setMinEvictableIdleTimeMillis(Long.parseLong(res.getString("minEvictableIdleTimeMillis")));
			//ds.setValidationQuery(res.getString("validationQuery"));
			
			
		} catch (Exception e) {
			System.out.println("ERROR_001_初始化连接池失败");
		} 
   }
	@Override
	public Connection getRealConnection() 
	{
		Connection con=null;
		try {
			con=ds.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR_002_得到连接失败");
			e.printStackTrace();
		}
		return con;
	}

}
