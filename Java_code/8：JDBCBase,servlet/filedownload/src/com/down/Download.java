package com.down;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class Download extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		
		//给下载文件起名,大量的下载请求
		String fname=req.getParameter("fname");
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssSS");
		Date date=new Date();
		Random random=new Random();
		int index=fname.lastIndexOf(".");
		String fname1=sf.format(date)+random.nextInt(1000)+fname.substring(index);
		
		byte[] tem=fname1.getBytes();
		String fname2=new String(tem,"iso-8859-1");
		resp.setHeader("Content-disposition", "attachment;filename="+fname2);
		
		//得到文件输出流
		ServletContext sc=this.getServletContext();
		String path=sc.getRealPath("downs");
		File file=new File(path,fname);
		FileInputStream fi=new FileInputStream(file);
		
		//得到响应输出流
		ServletOutputStream out=resp.getOutputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		while((len=fi.read(buffer))>0)
		{
			out.write(buffer, 0, len);
		}
		
		fi.close();
		out.close();
	
		
	}
	
}
