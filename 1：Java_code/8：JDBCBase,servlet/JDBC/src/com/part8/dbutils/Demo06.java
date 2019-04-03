package com.part8.dbutils;

import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * ArrayListHandler
 * @author likang
 *
 */
public class Demo06 
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
		
		 
		 List<Object[]> list=run.query(con, "select * from person where id>?", new ArrayListHandler(),1);
		 for(Object[] p:list)
		 {
			 for(Object o:p)
			 System.out.print(o+"\t");
			 System.out.println("");
		 }

	}

}
