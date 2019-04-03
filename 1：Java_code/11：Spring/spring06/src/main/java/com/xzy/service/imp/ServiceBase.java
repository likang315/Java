package com.xzy.service.imp;

import com.xzy.dao.AdminDao;
import com.xzy.dao.ArticleDao;

public class ServiceBase {

	
 protected	AdminDao adminDao;
	
 protected	ArticleDao articleDao;

public AdminDao getAdminDao() {
	return adminDao;
}

public void setAdminDao(AdminDao adminDao) {
	this.adminDao = adminDao;
}

public ArticleDao getArticleDao() {
	return articleDao;
}

public void setArticleDao(ArticleDao articleDao) {
	this.articleDao = articleDao;
}
 
 
 
}
