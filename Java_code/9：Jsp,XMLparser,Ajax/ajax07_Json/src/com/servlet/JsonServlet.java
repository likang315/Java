package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
@WebServlet("/jsondata")
public class JsonServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		 req.setCharacterEncoding("UTF-8");  
		 resp.setContentType("application/json;charset=UTF-8");
		 PrintWriter out=resp.getWriter();
		
		 User user=new User();
		 user.setAge(29);
		 user.setName("李四");
		 user.setEmails(new String[] {"admin@qq.com","admin@tt.com"});
		 
		 String json= JSONObject.toJSONString(user);	 
		 out.println(json);
			
		 out.close();
	}

}
