package com.xupt.dao.imp;

import com.xupt.dao.AdminDao;
import com.xupt.dao.ArticleDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 14:33
 */
public class ServiceBase {

    @Autowired
    protected AdminDao adminDao;
    @Autowired
    protected ArticleDao articleDao;

    public AdminDao getAdminDao()
    {
        return adminDao;
    }

    public void setAdminDao(AdminDao adminDao)
    {
        this.adminDao = adminDao;
    }

    public ArticleDao getArticleDao()
    {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao)
    {
        this.articleDao = articleDao;
    }
}
