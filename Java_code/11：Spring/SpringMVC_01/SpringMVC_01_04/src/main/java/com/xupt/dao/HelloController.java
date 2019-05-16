package com.xupt.dao;

import com.xupt.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 15:17
 */

/**
 * Converter，@InitBinder
 */
@Controller
@RequestMapping(value="/admin")
public class HelloController {

    @RequestMapping("h1.form")
    public String handle(@RequestParam("info") User user, HttpSession session)
    {
        session.setAttribute("user",user);
        return "hello";

    }


//    @InitBinder
//    public void initBinder(WebDataBinder dataBinder)
//    {
//        dataBinder.registerCustomEditor(User.class,new UserEditor());
//    }


}
