package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/show")
public class ShowServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.setContentType("text/html;charset=utf-8");
		 req.setCharacterEncoding("utf-8");
		 PrintWriter out=resp.getWriter();
		 
		 File f=new File(this.getServletContext().getRealPath("ups"));
		 String [] all=f.list();
		 for(String str:all)
		 {
			 out.println("<img src='ups/"+str+"' style='width:100px;height:100px;margin:20px;float:left;'/>");
		 }
		 out.close();
	}

}
