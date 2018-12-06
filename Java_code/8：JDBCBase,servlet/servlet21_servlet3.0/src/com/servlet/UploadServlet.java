package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * servlet 3.0 upload
 * @author likang
 *
 */
@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		PrintWriter out=resp.getWriter();
		//得到文件上传域
		Part part=req.getPart("file1");
	 
		//得到绝对路径
		String realpath=this.getServletContext().getRealPath("ups");
		 
		String fname=getFileName(part);
		String newname=newName()+getExtName(fname);
		part.write(realpath+"/"+newname);
		
	 out.println("<ul>");
	 
	 out.println("<li>描述："+req.getParameter("dis")+"</li>");
	 out.println("<li>getContentType:"+part.getContentType()+"</li>");
	 out.println("<li>getName:"+part.getName()+"</li>"); //得到文件上传域的name属性值
	 out.println("<li>getSize:"+part.getSize()+"</li>");
	 
	 java.util.Collection<java.lang.String> allkey=part.getHeaderNames();
	 for(String key:allkey)
		 out.println("<li>"+key+":"+part.getHeader(key)+"</br>"+"</li>");
	 
	 out.println("<li>"+newname+"</li>");
	 
	 out.println("</ul>");
	
	 out.println("<img src='ups/"+newname+"'/>");
	 out.println("<center><a href='show'>show</a></center>");
	 out.close();
	}
	
	//得到文件名
	public String getFileName(Part part)
	{
		String re="";
		String headv=part.getHeader("Content-Disposition");
		Pattern pa=Pattern.compile("(form-data; name=\"(.*?)\"; filename=\"(.*?)\")");
		Matcher ma=pa.matcher(headv);
		if(ma.find())
		{
			re=ma.group(3);
		}
		return re;
	}
	//得到文件扩展名   .xxx
	public String getExtName(String name)
	{
		String ex="";
		int index=name.toLowerCase().lastIndexOf(".");
		ex=name.substring(index);
		return ex;
	}
	
	/**
	 * 给文件去名字，大量的业务可能会造成并发性，在同一时间内可能会造成相同的值，因此加入随机数
	 */
	SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	Random rand=new Random();
	
	public String newName()
	{
		String re=null;
		re=sf.format(new Date())+"_"+rand.nextInt(1000);
		return re;
	}

}
