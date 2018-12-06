package com.servlet.param;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.bean.Admin;

@WebServlet("/article")
public class ArticleServlet extends ActionBase {

	@Override
	public void index() throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out=resp.getWriter();
        
        out.println("<h1>index....</h1>");
        
        out.close();
	}
	
	/**
	 * 把params 封装，和对象绑定，直接通过对象获得参数
	 * @throws ServletException
	 * @throws IOException
	 */
	public void add() throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        //params 和对象绑定
	        Admin admin=new Admin();
	        this.getBean(admin);
	        
	        out.println("<h1>add.."+admin.getName()+".."+admin.getPwd()+"</h1>");
	        out.close();
	}
	
	public void update() throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        Admin admin=new Admin();
	        this.getBean(admin);
	        
	        out.println("<h1>update...."+admin.getName()+"</h1>");
	        out.close();
	}
	
	public void delete() throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        
	        out.println("<h1>del....</h1>");
	        
	        out.close();

	}




}
