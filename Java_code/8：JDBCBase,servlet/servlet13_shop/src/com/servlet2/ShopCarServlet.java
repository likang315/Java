package com.servlet2;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.db.ConnectionManager;
import com.pojo.Goods;

/**
 * 购物车，一个servlet 当多个servlet用
 * @author likang
 *
 */
@WebServlet("/shopcar")
public class ShopCarServlet extends HttpServlet 
{
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(null==req.getSession().getAttribute("loged"))
		{
			resp.sendRedirect("login");
			return ;
		}

		req.setCharacterEncoding("utf-8");
		String action=null!=req.getParameter("action")?req.getParameter("action"):"index";
		
		Class clazz=this.getClass();
		 try {
			Method method=clazz.getDeclaredMethod(action, new Class[] {HttpServletRequest.class,HttpServletResponse.class});
			 
			 if(null!=method)
			 {
				 method.invoke(this, new Object[] {req,resp});
			 }
		} catch (Exception e) {
			System.out.println("Error_08:反射操作商品失败");
		}	 
	}
	
	
	/**
	 * 添加到购物车
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addcar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		 req.setCharacterEncoding("utf-8");
     	 try {
			 String gid=null!=req.getParameter("gid")?req.getParameter("gid"):"";
			 String num=null!=req.getParameter("num")?req.getParameter("num"):"";
			
			 Goods goods=new QueryRunner(ConnectionManager.ds).query("select * from goods where id=?", new BeanHandler<Goods>(Goods.class),Integer.parseInt(gid));
	         goods.setCounter(Integer.parseInt(num));//买了多少个
	         
	         //用来存放商品的集合
	         List<Goods> car=(List<Goods>)req.getSession().getAttribute("car");
	         if(null==car)
	        	 car=new ArrayList<Goods>();
	         car.add(goods);
	         	
	         req.getSession().setAttribute("car", car);
     	     resp.sendRedirect("shopcar");
     	 
     	 } catch (Exception e) {
			System.out.println("Error_09:添加商品失败");
		}
	}
	
	
	public void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String index=null!=req.getParameter("index")?req.getParameter("index"):"";
		List<Goods> cars=(List<Goods>)req.getSession().getAttribute("car");
		if(null!=cars)
			cars.remove(Integer.parseInt(index));
		req.getSession().setAttribute("car", cars);
		resp.sendRedirect("shopcar"); 
	}
	
	
	//先调用
	protected void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("	<head>");
		out.println("		<meta charset='UTF-8'>");
		out.println("		<title>用户购物车</title>");
		out.println("		<link rel='stylesheet' href='css/bootstrap.min.css' />");
		out.println("		<style type='text/css'>");
		out.println("			td{text-align: center;}");
		out.println("			");
		out.println("		</style>");
		out.println("	</head>");
		out.println("	<body>");
		out.println("		<div class='container'>");
		out.println("			<div class='row'>");
		out.println("				<div class='col-sm-6 col-lg-offset-3' style='padding-top: 30px;'>");
		out.println("<div class='panel panel-info'>");
		out.println("  <div class='panel-heading'>用户购物车：</div>");
		out.println("  <div class='panel-body'>");
		
		out.println(" <!---------start form------------>");
		out.println("<table class='table table-hover table-responsive table-striped'>");
		out.println("	<tr><th>ID</th><th>图片</th><th>标题</th><th>单价</th><th>数量</th><th>管理</th></tr>");
		
		List<Goods> cars=(List<Goods>)req.getSession().getAttribute("car");
		BigDecimal zero=BigDecimal.ZERO;
		
		if(null!=cars&&cars.size()>0)
		{
		
			for(int i=0;i<cars.size();i++)
			{
				Goods g=cars.get(i);
				out.println("	<tr>");
				out.println("		<td>"+(i+1)+"</td>");
				out.println("		<td><img src='img/"+g.getPic()+"' width='80' height='80'/></td>");
				out.println("		<td>"+g.getName()+"</td>");
				out.println("		<td>￥"+g.getPrice()+"元</td>");
				out.println("		<td>"+g.getCounter()+"</td>");
				out.println("		<td>");
				out.println("			<a href='shopcar?action=del&index="+i+"' class='btn btn-sm btn-danger'>删除</a>");
				out.println("		</td>");
				out.println("		");
				out.println("	</tr>");
				//总价
				zero=zero.add(g.getPrice().multiply(BigDecimal.valueOf(g.getCounter())));
			}
		}
		
		out.println("	<tr>");
		out.println("		<td colspan='6' align='right' style='text-align: right;'>");
		out.println("			<a href='index' class='btn btn-sm btn-info'>返回</a>");
		out.println("			总共:"+zero.toString()+"元</td>");
		out.println("	</tr>");
		out.println("</table>");
		out.println(" <!---------end form----------->");
		out.println("  </div>");
		out.println("</div>");
		out.println("					");
		out.println("				</div>");
		out.println("				");
		out.println("			</div>");
		out.println("		</div>");
		out.println("		");
		out.println("		");
		out.println("		");
		out.println("");
		out.println("	</body>");
		out.println("</html>");
		
		out.close();
	}

}
