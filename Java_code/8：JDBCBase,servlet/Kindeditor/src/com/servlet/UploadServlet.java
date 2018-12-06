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

import org.json.simple.JSONObject;

@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 resp.setContentType("text/html;charset=utf-8");
		 req.setCharacterEncoding("utf-8");
		 PrintWriter out=resp.getWriter();
		 //通过Content—disposition 的name属性，得到图片域
		 Part part=req.getPart("imgFile");

		 String realpath=this.getServletContext().getRealPath("ups");
		 
		 String fname=getFileName(part);
		 String newname=randname()+getExtName(fname);
		 part.write(realpath+"/"+newname);
	 
//	    JSONObject obj = new JSONObject();
//		obj.put("error", 0);
//		obj.put("url", req.getContextPath() + "/ups/"+newname);
//		out.println(obj.toJSONString());
	
	    out.print("{\"error\":0,\"url\":\""+req.getContextPath()+ "/ups/"+newname+"\"} ");
	    out.close();
	    
	}
	
	
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
	
	public String getExtName(String name)
	{
		String ex="";
		int index=name.toLowerCase().lastIndexOf(".");
		ex=name.substring(index);
		
		return ex;
	}
	
	SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	Random rand=new Random();
	public String randname()
	{
		String re=null;
		re=sf.format(new Date())+"_"+rand.nextInt(1000);
		return re;
	}

	
	
	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
