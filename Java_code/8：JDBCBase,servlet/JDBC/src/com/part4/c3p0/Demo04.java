package com.part4.c3p0;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 从propertise配置文件中导入
 * @author likang
 *
 */
public class Demo04 {

	private static ComboPooledDataSource  ds=new ComboPooledDataSource();
	

	public static void main(String[] args)throws Exception {
		Connection con=ds.getConnection();
		ResultSet rs=con.createStatement().executeQuery("select * from person");
		
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+"\t"+rs.getString(2));
		}
		
		con.close();//放回连接池
		System.out.println(con);
	
	}

}
