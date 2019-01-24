package com.xupt.controller;

import com.xupt.pojo.Book;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/24 15:56
 */

/**
 * 导入资源文件和加log4j
 */
@Controller
@RequestMapping("/admin")
@PropertySource(value = "classpath:/config/log4j-spring.properties")
    public class HelloController {

    private static Logger log= Logger.getLogger(HelloController.class);


    @Autowired
    private Book book;

    @RequestMapping("/")
    public String handle()
    {
        log.info("add log4j,.,.");
        System.out.println(book.getName()+book.getPrice());

        log.info("add log4j error .........");
        return "index";
    }

}
