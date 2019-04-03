package com.xzy.dao.imp;

import org.springframework.stereotype.Repository;

import com.xzy.dao.ArticleDao;
import com.xzy.pojo.Article;
@Repository
public class ArticleDaoImp implements ArticleDao {

	public void add(Article art) {
		// TODO Auto-generated method stub
		System.out.println("增加文章");
	}

}
