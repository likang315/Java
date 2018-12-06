package com.part8.dbutils;

import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * dbutils 不得到连接对象
 * @author likang
 *
 */
public class Demo01 
{
	private static DruidDataSource  ds=new DruidDataSource();
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
				//ds.setMaxIdle(Integer.parseInt(res.getString("maxIdle")));

				//ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(res.getString("timeBetweenEvictionRunsMillis")));
				//ds.setMinEvictableIdleTimeMillis(Long.parseLong(res.getString("minEvictableIdleTimeMillis")));
				//ds.setValidationQuery(res.getString("validationQuery"));
				
				
			} catch (Exception e) {
				
			} 
	   }
	
	public static void main(String[] args) throws Exception
	{
		 //jdbcutils
		 QueryRunner run=new QueryRunner(ds);
		 
		 run.update("create table stu2(f1 int,f2 varchar(2))");
		 System.out.println("OK");

	}

}
