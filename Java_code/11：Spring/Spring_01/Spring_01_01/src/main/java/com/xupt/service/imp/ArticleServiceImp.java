package com.xupt.service.imp;

import com.xupt.pojo.Article;
import com.xupt.service.ArticleService;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 10:27
 */
public class ArticleServiceImp extends ServiceBase implements ArticleService {


    @Override
    public void addArticle()
    {
        articleDao.add(new Article());
    }
}
