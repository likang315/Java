package com.hw;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class HelloWorldController 
{
    @RequestMapping("/hi")
    public String helloWorld(Model model) 
    {
        model.addAttribute("message", "Hello World!");
        return "hello";
    }
    
    //传递参数
    @RequestMapping(value="/{day}", method=RequestMethod.GET)
    public String getForDay(@PathVariable int day, Model model) 
    {
    	model.addAttribute("day",day);
        return "hello";
    }
    
    //@RequestParam
    @RequestMapping("/hi1")
	public String handle1
	(
			@RequestParam("uname")String userName,
			@RequestParam("upwd") String password,
			@RequestParam("urea") String realName,Model model
	) 
    {
    	System.out.println(userName);
    	System.out.println(password);
    	System.out.println(realName);
    	
    	model.addAttribute("uname",userName);
		return "hello";
	}
   
    //url传递的参数自动和此方法参数配置
    @RequestMapping("/hi2")
	public String handle2
	(
		String uname,String upwd,String urea,Model model
	) 
    {
    	System.out.println(uname);
    	System.out.println(upwd);
    	System.out.println(urea);
    	
    	model.addAttribute("uname",uname);
		return "hello";
	}
    
     //@CookieValue 和@RequestHeader
    @RequestMapping("/hi3")
	public String handle3
	(
		@CookieValue("JSESSIONID") String sessionId,
		@RequestHeader("Accept-Language") String accpetLanguage,
		Model model
	) 
    {
       model.addAttribute("cook", sessionId+"&nbsp;&nbsp;"+accpetLanguage);
		return "hello";
	}

     //自动和对象属性匹配
    @RequestMapping("/hi4")
	public String handle4(User user) {
    	System.out.println(user.getUname());
    	System.out.println(user.getAge());
		return "hello";
	}
    
    
    @RequestMapping("/hello.htm")
    public void hello(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException
    {
    	resp.setContentType("text/html;charset=gbk");
    	PrintWriter out=resp.getWriter();
    	
    	HttpSession session=req.getSession();
    	session.setAttribute("aa","aa");
    	out.println("<h1>hello</h1>");
    	
    	out.close();
    }
}