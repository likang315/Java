package com.part7.dbutils_source;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
/**
 * 通过ResultSetMetadata 得到表的数据
 * @author likang
 *
 */
public class ArrayListHandler implements ResultSetHandler{

	@Override
	public List<Object[]> handle(ResultSet rs) 
	{
		List<Object[]> list=new ArrayList<Object[]>();//集合里面每一个元素是一个集合
		try {
				
			ResultSetMetaData rmd=rs.getMetaData();//结果集的原数据
			while(rs.next())
			{
				//此集合用来存放一条记录
				Object[] obj=new Object[rmd.getColumnCount()];
				
				for(int i=0;i<rmd.getColumnCount();i++)
				{
					obj[i]=rs.getObject(i+1);
				}
				list.add(obj);//list 中每一个元素都是一行数据
			}
		} catch (Exception e) {
			System.out.println("Error_01:ArrayListHandler 失败");
		}
		return list;
	}

}
