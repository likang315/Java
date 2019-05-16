package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.bean.T1;
import com.dao.core.BaseDao;

public interface T1Dao extends BaseDao<T1> {

	
	public List<T1> findByXX()throws SQLException;
}
