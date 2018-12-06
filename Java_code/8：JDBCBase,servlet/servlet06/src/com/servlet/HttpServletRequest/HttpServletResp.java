package com.servlet.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse, http 报文操作
 * @author likang
 *
 */
@WebServlet("/hi")
public class HttpServletResp extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		resp.setHeader("Server", "xzy web server 1.0");
		resp.addHeader("aaa", "bbb");
		
		
	
	/*	resp.setStatus(302);
		resp.setHeader("Location", "index.html");
		*/
		
		//resp.sendRedirect("abc.html");
		
		resp.sendError(500,"服务器错误...");
		
		out.close();
		
	}

}
