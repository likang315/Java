package com.xzy.service.imp;

import com.xzy.service.UserService;

public class UserServiceImp extends ServiceBase implements UserService {

	public void check() {
		 adminDao.checkLogin();
		 
		
	}

}
