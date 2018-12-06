package com.part8.dbutils;

import java.sql.Connection;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.alibaba.druid.pool.DruidDataSource;

//BeanHandler 只输出符合要求的第一条
public class Demo05 
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
		 QueryRunner run=new QueryRunner();
		 Connection con=ds.getConnection();
		
		 
		 Person p=run.query(con, "select * from person where id>?", new BeanHandler<Person>(Person.class),1);
		 System.out.println(p.getId()+"\t"+p.getName()+"\t"+p.getAge());
		

	}

}
