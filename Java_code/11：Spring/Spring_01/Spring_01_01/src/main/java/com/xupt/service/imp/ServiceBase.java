package com.xupt.service.imp;

import com.xupt.dao.AdminDao;
import com.xupt.dao.ArticleDao;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 10:19
 */

/**
 * 对数据库的多表操作（dao层的操作），添加事务
 */
public class ServiceBase {

    protected ArticleDao articleDao;
    protected AdminDao adminDao;

    public ArticleDao getArticleDao()
    {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao)
    {
        this.articleDao = articleDao;
    }

    public AdminDao getAdminDao()
    {
        return adminDao;
    }

    public void setAdminDao(AdminDao adminDao)
    {
        this.adminDao = adminDao;
    }

}
