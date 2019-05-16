package com.servlet.unrepeat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *校验令牌后，销毁
 * @author likang
 *
 */
@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet 
{
	@Override
	public void service( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		 req.setCharacterEncoding("utf-8");
		 resp.setContentType("text/html;charset=utf-8");
		 PrintWriter out=resp.getWriter();
		 String uname=null!=req.getParameter("uname")?req.getParameter("uname"):"";
		 String upwd=null!=req.getParameter("upwd")?req.getParameter("upwd"):"";
		 String token=null!=req.getParameter("token")?req.getParameter("token"):"";
		 
		 HttpSession hs=req.getSession();
		 String stoken=(String)hs.getAttribute("token");
		 if(token.equals(stoken))
		 {
			 hs.removeAttribute("token");
			 out.println("uname:"+uname+"--->upwd:"+upwd);
		 }else
		 {
			 out.println("<h1>重复提交无效...</h1>");
		 }
		 out.close();
	}

}
