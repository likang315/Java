package com.part7.dbutils_source;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * 第三种 通过集合 存储对象，直接从结果集中getXXX()
 * @author likang
 *
 */
public class PersonHandler implements ResultSetHandler<List<Person>> {
	@Override
	public List<Person> handle(ResultSet rs) {
		
		List<Person> list=new ArrayList<Person>();
		try {
			while(rs.next())
			{
				Person p=new Person();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setAge(rs.getInt("age"));
				p.setSex(rs.getString("sex"));
				list.add(p);
			}
		} catch (SQLException e) {
			System.out.println("Error_01:personHandler 失败");
		}
		
		return list;
	}	
}
