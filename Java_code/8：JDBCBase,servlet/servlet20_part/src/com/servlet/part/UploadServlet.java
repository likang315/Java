package com.servlet.part;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Part(上传域)，得到一些信息
 * @author likang
 *
 */
@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		 resp.setContentType("text/html;charset=utf-8");
		 req.setCharacterEncoding("utf-8");
		 PrintWriter out=resp.getWriter();
		 Part part=req.getPart("pic");//根据文件上传域的name，得到上传域
		 
		 out.println("<ul>");
		 
		 out.println("<li>描述:"+req.getParameter("dis")+"</li>");
		 out.println("<li>getContentType:"+part.getContentType()+"</li>");
		 out.println("<li>getName:"+part.getName()+"</li>");
		 out.println("<li>getSize:"+part.getSize()+"</li>");
		 
		 java.util.Collection<java.lang.String> allkey=part.getHeaderNames();
		 	for(String key:allkey)
		 		out.println("<li>"+key+":"+part.getHeader(key)+"</li>");
		 	
		 out.println("</ul>");
		 
		 String realpath=this.getServletContext().getRealPath("ups");
		 part.write(realpath+"/hi.txt");
		 out.println("<h1>成功<h1>");
		 
		 out.close();	
	}

}
