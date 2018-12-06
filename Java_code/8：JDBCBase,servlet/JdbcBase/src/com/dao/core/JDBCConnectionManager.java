package com.dao.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JDBCConnectionManager extends ConnectionManager {

	@Override
	public Connection getRealConnection()
	{
		ResourceBundle res=ResourceBundle.getBundle("jdbc");
		
		Connection con=null;
		try {
			Class.forName(res.getString("driverClassName"));
			con=DriverManager.getConnection(res.getString("url"),res.getString("username"),res.getString("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}

}
