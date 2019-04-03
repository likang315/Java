package com.part7.dbutils_source;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * 通过反射得到属性，重新创建对象赋值，然后加入集合
 * @author Administrator
 *
 * @param <T>
 */
public class BeanListHandler<T> implements ResultSetHandler {
    
	private Class classes;
	public BeanListHandler()
	{
		
	}
	public BeanListHandler(Class classes)
	{
		this.classes=classes;
	}
	
	@Override
	public List<T> handle(ResultSet rs) {
		List<T> list=new ArrayList<T>();
		try {
			while(rs.next())
			{
				T t=(T)classes.newInstance();//重新创建了一个新实例
				
				Field [] allfiled=classes.getDeclaredFields();//得到所有属性名的集合
				for(Field f:allfiled)
				{
					String fname=f.getName();
					f.setAccessible(true);
					f.set(t, rs.getObject(fname));
				}
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
