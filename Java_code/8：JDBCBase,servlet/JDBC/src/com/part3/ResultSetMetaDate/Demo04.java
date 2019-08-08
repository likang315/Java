package com.part3.ResultSetMetaDate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
/**
 * ResultSetMetaData 由结果集的原数据得到表的数据
 * @author likang
 *
 */
public class Demo04 {

	public static void main(String[] args) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt", "root", "mysql");
		Statement stat=con.createStatement();
		
		
		ResultSet rs=stat.executeQuery("select * from person");
		
		//封装了结果集的元数据
		ResultSetMetaData rsm=rs.getMetaData();
		
		int ccount=rsm.getColumnCount();//列数
		for(int i=0;i<ccount;i++)
		{
			System.out.println(rsm.getColumnName(i+1)+"\t"+rsm.getColumnType(i+1));
		}
		
		
		while(rs.next())
		{
			for(int i=0;i<ccount;i++)
			{
				int type=rsm.getColumnType(i+1);
				switch (type) {
				case Types.INTEGER:
					System.out.print(rs.getInt(i+1)+"\t");
					break;
				case Types.CHAR:
					System.out.print(rs.getString(i+1)+"\t");
					break;
				default:
					System.out.print(rs.getObject(i+1)+"\t");
					break;
				}
				
			}
			System.out.println("\n");
			
		}
		
	}

}
