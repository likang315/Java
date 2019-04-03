package com.part7.dbutils_source;

import java.sql.ResultSet;
/**
 * 把ResultSet 的集合拿出来 重新创建一个ResultSet
 * @author Administrator
 * @param <T>
 */
public interface ResultSetHandler<T> {
	//将结果集封装成对象，使用
	public T handle(ResultSet rs);
}
