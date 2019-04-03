package com.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从 index.html文件中读取， 并打印
 * @author likang
 *
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		ServletContext sc=this.getServletContext();
		
		String path=sc.getRealPath("WEB-INF/page/login.html");
		FileInputStream fi=new FileInputStream(path);
		BufferedReader br=new BufferedReader(new InputStreamReader(fi,"UTF-8"));
		String str=null;
		while(null!=(str=br.readLine()))
		{
			out.println(str);
		}
		br.close();
		out.close();
	}

}
