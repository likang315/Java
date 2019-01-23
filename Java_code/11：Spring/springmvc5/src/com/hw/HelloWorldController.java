package com.hw;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller 的方法参数为Model 和ModelAndView
 * @author likang
 *
 */
@Controller
@RequestMapping("/admin")
public class HelloWorldController 
{

    @RequestMapping("/hi")
    public String helloWorld(Model model) 
    {
        User user=new User();
        user.setUname("lisi");
        user.setAge(23);
    	
    	model.addAttribute("message", "Hello World!");
    	model.addAttribute("user",user); 
        return "hello";
    }
    
    @RequestMapping("/hello")
    public ModelAndView helloWorld() 
    {
        User user=new User();
        user.setUname("wangwu");
        user.setAge(21);
        
    	ModelAndView  mv=new ModelAndView();
        mv.addObject("user", user);
    	mv.setViewName("hello");
    	
        return mv;
    }
 
}