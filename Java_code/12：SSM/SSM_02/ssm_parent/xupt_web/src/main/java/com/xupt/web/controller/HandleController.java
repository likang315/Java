package com.xupt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 15:40
 */
@Controller
@RequestMapping("/admin")
public class HandleController {

    @RequestMapping("/handle")
    public String handle(Model model)
    {

        model.addAttribute("hello","SpringMVC");
        return "hello";
    }

}
