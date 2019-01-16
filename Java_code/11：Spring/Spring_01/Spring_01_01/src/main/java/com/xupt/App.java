package com.xupt;

import com.xupt.service.ArticleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //加载Spring容器的上下文环境
        ApplicationContext context=new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
        ArticleService ari=(ArticleService) context.getBean("ari");
        ari.addArticle();

    }
}
