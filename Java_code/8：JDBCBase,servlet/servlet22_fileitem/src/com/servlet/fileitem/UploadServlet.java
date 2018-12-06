package com.servlet.fileitem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Apache commons Fileupload
 * @author likang
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
	
	 out.println("<ul>");
	 try {
		 // Create a factory for disk-based file items
		 DiskFileItemFactory factory = new DiskFileItemFactory();
		 ServletContext sc = this.getServletContext();
		 File repository = (File) sc.getAttribute("javax.servlet.context.tempdir");
		 factory.setRepository(repository);
		 // Create a new file upload handler
		 ServletFileUpload upload = new ServletFileUpload(factory);
		 
		 List<FileItem> items = upload.parseRequest(req);
		 if(null!=items && items.size()>0)
		 {
			 for(FileItem it:items)
			 {
				if(it.isFormField())
				{
					if("dis".equals(it.getFieldName()))
						out.println("<li>描述："+it.getString("utf-8")+"</li>"); //req.getparamter(..)
				}else
				{
					out.println("<li>isFormField():"+it.isFormField()+"</li>");
					out.println("<li>ContentType:"+it.getContentType()+"</li>");
					out.println("<li>getName():"+it.getName()+"</li>");//上传文件名
					//文件的name属性
					out.println("<li>getFieldName:"+it.getFieldName()+"</li>");
					out.println("<li>getSize:"+it.getSize()+"</li>"); 
								    
					String extname=this.getExtName(it.getName());
					String newname=this.getRandName()+extname;	   
					it.write(new File(sc.getRealPath("ups")+"/"+newname));
									
					out.println("<li>"+newname+"</li>");
					out.println("<img src='ups/"+newname+"'/>"); 
				}
			 }
		 }
	} catch (Exception e) {
		
	}
	 out.println("</ul>");
	 out.println("<center><a href='show'>show</a></center>");
	 out.close();
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
	public String getRandName()
	{
		String re=null;
		re=sf.format(new Date())+"_"+rand.nextInt(1000);
		return re;
	}

}
