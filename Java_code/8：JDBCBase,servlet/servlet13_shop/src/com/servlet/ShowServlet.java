package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pojo.Admin;
import com.pojo.Goods;
import com.utils.PageDiv;


/**
 * 网站进去显示操作 ，有登录，注册功能，分页显示
 * @author likang
 */
@WebServlet("/show")
public class ShowServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("	<head>");
		out.println("		<meta charset='utf-8' />");
		out.println("		<title>购物车首页面</title>");
		//引入外部样式表
		out.println("		<link rel='stylesheet' href='css/bootstrap.min.css' />");
		//内部样式表
		out.println("		<style type='text/css'>");
		out.println("		    #content{}");
		out.println("		    #content .g_list{ float: left; width: 215px; height: 245px; border: 1px #ccc solid; border-radius: 5px; margin: 10px 26px; position: relative;}");
		out.println("		    #content .g_list img{ width: 200px; height: 200px; position: absolute; left: 8px; top:8px;}");
		out.println("		    #content .g_list p{width: 200px; height: 32px; line-height: 32px; position: absolute; left: 8px; top: 176px; background: rgba(0,0,0,0.6); color: #FFF;  text-align: center;}");
		out.println("		    #content .g_list form{ width: 200px; height: 36px; line-height:36px; position: absolute; left: 8px; top: 208px;}");
		out.println("		</style>");
		
		out.println("	</head>");
		
		out.println("	<body>");
		//自动调整尺寸
		out.println("		<div class='container'>");
		out.println("			<div class='row' style='border-bottom: 2px #EEE solid; padding: 10px 0;'>");
		out.println("				<div class='col-sm-12 text-right'>");
		
		
		//从Session得到登录信息，验证是否登录过
		HttpSession hs=req.getSession();
		Admin admin=(Admin)hs.getAttribute("loged");
		
		if(null!=admin)
		{
			out.println("					你好，"+admin.getName()+"！ ");
			//传参
			out.println("					<a href='shopcar?action=index' class='btn btn-sm btn-primary'>购物车</a>");
			out.println("					<a href='goods?action=toaddgoods'  class='btn btn-sm btn-info'>增加商品</a>");
		}else
		{
			out.println("					<a href='login'  class='btn btn-sm btn-warning'>登录</a>");
			out.println("					<a href='regist'  class='btn btn-sm btn-danger'>注册</a>");	
		}	
		out.println("				</div>");
		out.println("			</div>");

		
		
		
	//--------------------------------------------------------------------------------------------------------------
		
		out.println("			<div id='content'>");	//ID 选择器	
		//分页显示，选择器的优先级
		PageDiv<Goods> pd=(PageDiv<Goods>)req.getAttribute("pd");
		if(null!=pd&&null!=pd.getDatas()&&pd.getDatas().size()>0)
		{
			//显示所有的商品
			for(Goods g:pd.getDatas())
			{	
				//class 选择器
				out.println("				   <div class='g_list'>");
				// 标签名选择器
				out.println("				   	  <img src='img/"+g.getPic()+"' />");
				out.println("				   	  <p>"+g.getName()+"</p>");
				out.println("				   	  <form action='shopcar' method='post'>");
				
				//隐藏组件，value传输指定的ID和addcar
				out.println("<input type='hidden' name='gid' value='"+g.getId()+"'/>");
				out.println("<input type='hidden' name='action' value='addcar'/>");
				
				out.println("				   	  	<label>￥<span style='color: red;'>"+g.getPrice()+"</span>元</label>");
				//下拉菜单，选择数量
				out.println("				   	  	<select name='num'>");
				for(int i=1;i<5;i++)
				out.println("				   	  		<option value='"+i+"'>"+i+"</option>");
				out.println("				   	  	</select>");
				
				out.println("				   	  	<input type='submit' value='加入购物车' class='btn btn-sm btn-success' />");
				out.println("				   	  </form>");
				out.println("				   </div>");	
			}	
		}
		out.println("			</div>");
		
		
		
		out.println("			<div style='clear: both;'></div>");
		out.println("			<div class='row' style='text-align: center;'>");
		out.println("<nav >");
		//无序列表
		out.println("  <ul class='pagination'>");
		out.println("    <li>");
		//上一页功能，判断当前页是不是第一页
		if(pd.getPageNo()-1>0)
		    out.println("<a href='index?pageNo="+(pd.getPageNo()-1)+"' aria-label='Previous'>");
		else
			out.println("<a href='index?pageNo=1' aria-label='Previous'>");
		
		out.println("        <span aria-hidden='true'>&laquo;</span>");
		out.println("      </a>");
		out.println("    </li>");
		
		//页号
		for(int i=pd.getStart();i<=pd.getEnd();i++)
		{
			out.println("    <li><a href='index?pageNo="+i+"'>"+i+"</a></li>");
		}
	
		out.println("    <li>");
		//下一页功能，判断当前页是不是最后一页
		if(pd.getPageNo()+1<pd.getTotalPage())
			out.println("<a href='index?pageNo="+(pd.getPageNo()+1)+"' aria-label='Next'>");
		else
			out.println("<a href='index?pageNo="+(pd.getTotalPage())+"' aria-label='Next'>");
		
		out.println("        <span aria-hidden='true'>&raquo;</span>");
		out.println("      </a>");
		out.println("    </li>");
		
		//页数
		out.println("<li>当前"+pd.getPageNo()+"/"+pd.getTotalPage()+"页&nbsp;&nbsp;共"+pd.getTotal()+"条</li>");
		out.println("  </ul>");
		out.println("</nav>");
		out.println("						");
		out.println("			</div>	");
		out.println("		</div>");
		out.println("	</body>");
		out.println("</html>");
		
		out.close();
	}
	
}



