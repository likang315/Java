package com.dao.imp;

import java.sql.SQLException;

import com.bean.Admin;
import com.service.UserService;
import com.service.core.ServiceFactory;

public class TestService {

	public static void main(String[] args) throws SQLException 
	{
	    UserService us=(UserService)ServiceFactory.getService(UserService.class);
	    Admin a=new Admin();
	    a.setUname("angel");
	    a.setUpwd("233");
	    a.setUpur("0000101010");
	    
	    us.addUser(a);

	}

}
