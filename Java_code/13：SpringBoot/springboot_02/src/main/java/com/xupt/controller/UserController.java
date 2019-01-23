package com.xupt.controller;

import com.xupt.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/23 19:59
 */
@Controller
public class UserController {

    @RequestMapping("/admin")
    public String handle(Model model)
    {
        List<User> list=new ArrayList<User>();
        User user=new User();
        user.setAge(12);
        user.setName("likang");

        list.add(user);
        list.add(new User("lisi",20));
        model.addAttribute("lists",list);

        return "index";
    }


}
