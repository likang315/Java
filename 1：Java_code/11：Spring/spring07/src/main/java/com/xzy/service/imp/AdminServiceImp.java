package com.xzy.service.imp;

import org.springframework.stereotype.Service;

import com.xzy.service.AdminService;
@Service
public class AdminServiceImp extends ServiceBase implements AdminService {

	public void addArticle() {
		 adminDao.checkLogin();
		  articleDao.add(null);
		
	}

}
