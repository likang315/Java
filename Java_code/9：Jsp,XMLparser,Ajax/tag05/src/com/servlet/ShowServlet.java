package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Student;

@WebServlet("/show")
public class ShowServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Student> stus=new ArrayList<Student>();
		stus.add(new Student(1,23,"李四","男"));
		stus.add(new Student(2,24,"张三","男"));
		stus.add(new Student(3,22,"小明","女"));
		stus.add(new Student(4,26,"王强","男"));
		stus.add(new Student(5,21,"李张","女"));
		
		req.setAttribute("stus", stus);
		req.getRequestDispatcher("show.jsp").forward(req, resp);
		
	}
}


