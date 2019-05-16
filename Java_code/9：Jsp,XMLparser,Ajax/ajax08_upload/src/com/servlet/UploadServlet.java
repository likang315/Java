package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	 req.setCharacterEncoding("utf-8");
	 resp.setContentType("text/html;charset=utf-8");
	 PrintWriter out=resp.getWriter();
	
	 Part part=req.getPart("uppic");
	 if(null!=part)
	 {
		String path=this.getServletContext().getRealPath("ups")+"/";
		String newname=randName()+getExtName(getFileName(part));
		part.write(path+newname);
		out.print("{newname:'"+newname+"',error:0}");
	 }else
	 {
		 out.print("{error:1}");
	 }
	 out.close();
	}
	
	/**
	 * 从part中解析出原文件名
	 * @param part
	 * @return
	 */
	public String getFileName(Part part)
	{
		String fname=null;
		String hvalue=part.getHeader("Content-Disposition");
	
		Pattern pattern=Pattern.compile("(form-data; name=\"(.*?)\"; filename=\"(.*?)\")");
		Matcher  mat=pattern.matcher(hvalue);
		if(mat.find())
		{
			fname=mat.group(3);
		}
		return fname;
	}
	
	public String getExtName(String fname)
	{
		String re=null;
		
		re=fname.toLowerCase().substring(fname.lastIndexOf("."));
		return re;
	}
	
	private SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public String randName()
	{
		String re=null;
		re=sf.format(new Date())+"_"+(int)(Math.floor(Math.random()*1000));
		
		return re;
	}

}
