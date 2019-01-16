package com.xzy.service.imp;

import org.springframework.stereotype.Service;

import com.xzy.service.UserService;
@Service
public class UserServiceImp extends ServiceBase implements UserService {

	public void check() {
		 adminDao.checkLogin();
		 
		
	}

}
