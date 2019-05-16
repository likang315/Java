package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/show")
public class ShowServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Student stu=new Student("ÀîËÄ",23);
        req.setAttribute("stu",stu);
        req.setAttribute("msg", "Hello EL");
        req.setAttribute("sex", "M");
		req.getRequestDispatcher("show.jsp").forward(req, resp);
		
	}

}
