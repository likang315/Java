package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pojo.Admin;
@WebServlet("/success")
public class SuccessServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>Insert title here</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>成功!</h1>");
		
		ServletContext sc=this.getServletContext();
		Integer counter=(Integer)sc.getAttribute("counter");
		if(null==counter)
			counter=1;
	    out.println("<h2 align='center'>页面被访问"+(counter++)+"次</h2>");
		sc.setAttribute("counter", counter);
		
		//增加用户
		out.println("<form action='addadmin' method='post'>name:<input type='text' name='uname'/>");
		out.println("pwd:<input type='text' name='upwd'/><input type='submit' value='add'/></form><br/>");
		
		
		out.println("<table border=1>");
		out.println("<tr><th>Id</th><th>Name</th><th>pwd</th><th>管理</th></tr>");
		
		//通过属性得到结果集
		List<Admin> list=(List<Admin>)req.getAttribute("alladmin");
		if(null!=list&&list.size()>0)
		{
			for(Admin admin:list)
			{
				out.println("<form action='updateAdmin' method='post'>");
				out.println("<input type='hidden' name='id' value='"+admin.getId()+"'/>");
					out.println("<tr>");
							out.println("<td>"+admin.getId()+"</td>");
							out.println("<td><input type='text' name='uname' value='"+admin.getName()+"'/></td>");
							out.println("<td>"+admin.getPwd()+"<br/> <input type='text' name='upwd' /></td>");
							out.println("<td>");
							out.println("<input type='submit' value='修改'/>");
							out.println("<a href='del?id="+admin.getId()+"'>删除</a>");
				            out.println("</td>");
					out.println("</tr>");
				out.println("</form>");
			}
		}
		out.println("</table >");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

}
