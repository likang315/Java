package com.hw;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController 
{
    @RequestMapping("/admin/hi")
    public String helloWorld(Model model) 
    {
        model.addAttribute("message", "Hello World!");
        return "hello";
    }
    
    @RequestMapping("/admin/hello.htm")
    public void hello(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException
    {
    	resp.setContentType("text/html;charset=utf-8");
    	PrintWriter out=resp.getWriter();
    	
    	
    	out.println("<h1>hello</h1>");
    	out.close();
    }
}