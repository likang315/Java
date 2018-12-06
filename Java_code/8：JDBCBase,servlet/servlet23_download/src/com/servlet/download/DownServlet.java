package com.servlet.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 利用响应报头Content-Disposition，来弹出下载框，用bit流输出
 * @author likang
 *
 */
@WebServlet("/download")
public class DownServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//给下载文件取默认名
		String fname=req.getParameter("fname");
		String showfname="美好.jpg";
		byte [] tem=showfname.getBytes();
		String showcname=new String(tem,"iso-8859-1");

		resp.setHeader("Content-Disposition", "attachment;filename="+showcname);
		
		//得到文件的输入流
		ServletContext sc=this.getServletContext();
		String path=sc.getRealPath("res");
		File file=new File(path,fname);
		FileInputStream fi=new FileInputStream(file);
		
		ServletOutputStream  out=resp.getOutputStream();
		byte [] buffer=new byte[1024];
		int len=-1;
		while((len=fi.read(buffer))>0)
		{
			out.write(buffer, 0, len);
		}
		fi.close();
		out.close();
	}

}
