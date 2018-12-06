package com.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.bean.T1;
import com.dao.T1Dao;
import com.dao.core.BaseDaoImp;
import com.dao.core.ConnectionManager;

public class T1DaoImp extends BaseDaoImp<T1> implements T1Dao {

	@Override
	public List<T1> findByXX() throws SQLException {
		String sql="select * from t1 where f1>10";
		Connection cn=ConnectionManager.newInstance().getConnection();
		List<T1> list=new QueryRunner().query(cn, sql, new BeanListHandler<T1>(T1.class));
		return list;
	}

	

}
