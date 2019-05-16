package com.dao.core;

import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> 
{
	/**
	 * Id自增,不用添加
	 * @param t 要增加的对象
	 * @return  新加对象的id
	 */
	 public int insert(T t) throws SQLException;
	 /**
	  * 一定要有id值，根据id修改
	  * @param t
	  */
	 public void update(T t) throws SQLException;
	 
	 public void delete(int id,Class<T> clazz) throws SQLException;
	 
	 public T get(int id,Class<T> clazz) throws SQLException;
	 
	 public List<T> getAll(Class<T> clazz) throws SQLException;
	 
	 public PageDiv<T> getByPage(Class<T> clazz,int pageNo,int pageSize) throws SQLException;

}
