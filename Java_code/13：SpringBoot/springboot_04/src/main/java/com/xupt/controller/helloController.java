package com.xupt.controller;

import com.xupt.dao.StuDao;
import com.xupt.pojo.Stu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/24 19:22
 */

/**
 * JPA操作数据库
 */
@Controller
@RequestMapping("/")
public class helloController {

    @Autowired
    private StuDao studao;

    @RequestMapping("/admin")
    public String handle()
    {
        Stu stu=studao.getOne(2);
        System.out.println(stu.getId()+"---------->"+stu.getName());

        return null;
    }

}
