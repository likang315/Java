package com.dao.imp;

import java.sql.SQLException;

import com.bean.T1;
import com.service.UserService;
import com.service.core.ServiceFactory;

public class TestT1 {
public static void main(String[] args) {
	
	UserService us=(UserService)ServiceFactory.getService(UserService.class);
	
	T1 t1=new T1();
	t1.setF1(10);
	t1.setF2("张三");
	
	try {
		us.addT1(t1);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
