package com.xupt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/2 12:23
 */

@Controller
public class HelloController
{
    @RequestMapping("/admin")
    public String handle(Model model)
    {
        model.addAttribute("hello", "SpringBoot Hello!");
        return "index";
    }
}
