package com.part5.druid;

import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;

public class Demo02 {
	private static DruidDataSource  ds=new DruidDataSource();
	static{
			//从配置文件中导入根据值导入
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
				//ds.setMaxIdle(Integer.parseInt(res.getString("maxIdle")));

				//ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(res.getString("timeBetweenEvictionRunsMillis")));
				//ds.setMinEvictableIdleTimeMillis(Long.parseLong(res.getString("minEvictableIdleTimeMillis")));
				//ds.setValidationQuery(res.getString("validationQuery"));
				
			} catch (Exception e) {
				
			} 
	   }
	
	public static void main(String[] args) throws Exception{
		
		ResultSet rs=ds.getConnection().createStatement().executeQuery("select * from person");
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
		}
	
	}
}
