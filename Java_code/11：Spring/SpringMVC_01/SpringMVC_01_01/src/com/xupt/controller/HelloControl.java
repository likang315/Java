package com.xupt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/19 16:34
 */

@Controller
public class HelloControl {

    @RequestMapping("/form/hi")
    public String helloWorld(Model model)
    {
        model.addAttribute("message", "Hello World!");
        return "hello";
    }

}
