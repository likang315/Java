package com.service;

import java.sql.SQLException;

import com.bean.Admin;
import com.bean.T1;

public interface UserService 
{
    /**
     * 增加一个用户，同时还要加日志
     * @param admin
     */
	public void addUser(Admin admin)throws SQLException;
	
	public void addT1(T1 t1)throws SQLException;
	
}
