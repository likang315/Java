package com.xupt.dao.imp;

import com.xupt.dao.ArticleDao;
import com.xupt.pojo.Article;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 9:48
 */

public class ArticleDaoImp implements ArticleDao {

    @Override
    public void add(Article art)
    {
        System.out.println("增加文章....");
    }

    //@PostConstruct
    public void init()
    {
        System.out.println("init .......");
    }
    //@PreDestroy
    public void destory()
    {
        System.out.println("destory.........");
    }
}
