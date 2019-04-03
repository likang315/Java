package com.xzy.service.imp;

import com.xzy.service.AdminService;

public class AdminServiceImp extends ServiceBase implements AdminService {

	public void addArticle() {
		 adminDao.checkLogin();
		  articleDao.add(null);
		
	}

}
