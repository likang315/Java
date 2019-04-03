package com.xupt.dao;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 17:50
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/admin")
public class HelloController {

    @RequestMapping("/h1")
    public String handle()
    {

        System.out.println("++++++执行+++++++");
        return "hello";
    }


}

