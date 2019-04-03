package com.servlet.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 继承servletBase其实还是一个servlet 
 * @author likang
 *
 */
@WebServlet("/article")
public class ArticleServlet extends ActionBase {

	/**
	 * 当获取的参数，为null时，访问此方法
	 */
	@Override
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out=resp.getWriter();
        
        out.println("<h1>index....</h1>");     
        out.close();
	}
	/**
	 * 数据库增加方法
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        
	        out.println("<h1>add....</h1>");
	        out.close();

	}
	/**
	 * 修改方法
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        
	        out.println("<h1>update....</h1>");
	        out.close();
	}
	
	/**
	 * 删除数据库对应的方法
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.setContentType("text/html;charset=utf-8");
	        PrintWriter out=resp.getWriter();
	        
	        out.println("<h1>delete....</h1>");
	        out.close();
	}



}
