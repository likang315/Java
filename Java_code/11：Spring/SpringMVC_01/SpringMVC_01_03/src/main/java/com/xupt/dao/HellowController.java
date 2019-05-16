package com.xupt.dao;

import com.xupt.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 10:00
 */

/**
 * @ModelAttribute在方法和参数的应用
 */
@Controller
@RequestMapping("/admin")
public class HellowController {

    @ModelAttribute
    public User handle(@RequestParam("name") String name)
    {
        User user=new User();
        user.setName(name);
        return user;
    }

    @RequestMapping("/h1")
    public String handle(@ModelAttribute("user") User user, HttpSession session)
    {

        user.setAge(23);
        session.setAttribute("user",user);


        return "hello";
    }

    @RequestMapping(value = "/h2")
    public String handle63(ModelMap modelMap)
    {
        modelMap.addAttribute("testAttr", "value1");
        return "hello";
    }


}
