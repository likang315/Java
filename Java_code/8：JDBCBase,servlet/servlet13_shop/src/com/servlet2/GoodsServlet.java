package com.servlet2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;

import com.db.ConnectionManager;
import com.pojo.Admin;

/**
 * 增加商品，一个servlet实现两个servlet
 * @author likang
 *
 */
@WebServlet("/goods")
public class GoodsServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//
		if(null==req.getSession().getAttribute("loged"))
		{
			resp.sendRedirect("login");
			return ;
		}
		req.setCharacterEncoding("utf-8");
		//增加商品按钮，传参
		String action=null!=req.getParameter("action")?req.getParameter("action"):"";
		
		Class clazz=this.getClass();
		 try {
			 //通过反射返回指定（action）的方法
			 Method method=clazz.getDeclaredMethod(action, new Class[] {HttpServletRequest.class,HttpServletResponse.class});
			 if(null!=method)
			 {
				 method.invoke(this, new Object[] {req,resp});
			 }
		} catch (Exception e) {
			System.out.println("Error_06:反射错误");
		} 
		 
	}
	
	public void toaddgoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		 req.getRequestDispatcher("WEB-INF/page/addgoods.html").forward(req, resp);;
	}
	
	
	public void savegoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	      	 String name=null!=req.getParameter("name")?req.getParameter("name"):"";
		     String price=null!=req.getParameter("price")?req.getParameter("price"):"";
			 String pic=null!=req.getParameter("pic")?req.getParameter("pic"):"";
			 
			 String sql="insert into goods(name,price,total,pic,admin_id) values(?,?,?,?,?)";
			 try {
				new QueryRunner(ConnectionManager.ds).update(sql,
						 name,new BigDecimal(price),1000,pic,((Admin)req.getSession().getAttribute("loged")).getId());
			} catch (SQLException e) {
				System.out.println("Error_07:添加商品信息到数据库");
			}
			 resp.sendRedirect("index");
	}
	
}
