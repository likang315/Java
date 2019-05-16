package com.servlet.unrepeat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 发令牌机制
 * @author likang
 *
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

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
		out.println("<center>");
		
		out.println("  <form action='checkLogin' method='post'>");
		out.println(" 用户名 :<input type='text' name='uname'/><br/>");
		out.println("  密码: <input type='password' name='upwd'/><br/>");
		
		HttpSession session=req.getSession();
		//得到全球唯一的id
		String uuid=UUID.randomUUID().toString();
		out.println("<input type='hidden' name='token' value='"+uuid+"'/>");//传值
		session.setAttribute("token", uuid);
		
		out.println("  <input type='submit' value='OK'/>");
		out.println("  </form>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}

}
