package com.dao.imp;

import java.sql.SQLException;

import org.junit.Test;

import com.dao.ArticleDao;
import com.bean.Article;

public class TestArticle {
 @Test
 public void testArticleAdd()
 {
	 ArticleDao ad=new ArticleDaoImp();
	 Article a=new Article();
	 a.setTitle("你好中。。。。");
	 a.setAuthor("admin");
	 a.setCounts(3);
	 a.setTxt("这是仙容是是加标题进");
	 
	 try {
		ad.insert(a);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

 }
}
