package com.xzy.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.xzy.dao.AdminDao;
import com.xzy.dao.ArticleDao;

public class ServiceBase {

	@Autowired
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
@Autowired
@Required
public void setArticleDao(ArticleDao articleDao) {
	this.articleDao = articleDao;
}
 
 
 
}
