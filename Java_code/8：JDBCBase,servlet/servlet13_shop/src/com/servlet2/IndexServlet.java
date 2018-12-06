package com.servlet2;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.db.ConnectionManager;
import com.pojo.Goods;
import com.utils.PageDiv;

/**
 * 分页操作（完成商品信息的查询），传给show
 * @author Administrator
 *
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		int pageSize=8;
		int pageNo=1;
		
		//字符串转换为int
		if(null!=req.getParameter("pageNo"))
			pageNo=Integer.parseInt(req.getParameter("pageNo"));
		
		try {
			
			//统计有多少个商品
			String sql="select count(id) from goods";
			Object otat=new QueryRunner(ConnectionManager.ds).query(sql,new ArrayHandler())[0];
			
			//降序，把每页的商品返回查询结果
			List<Goods> goods=new QueryRunner(ConnectionManager.ds).query("select * from goods order by id desc limit ?,?",new BeanListHandler<Goods>(Goods.class),(pageNo-1)*pageSize,pageSize);
		    
			int total=0;
			//返回long 或者 Biginteger
			if(null!=otat&&otat instanceof BigInteger)
			{
				total=((BigInteger)otat).intValue();
			}else if(null!=otat&&otat instanceof Long)
			{
				total=((Long)otat).intValue();
			}
			
			//把页的信息加入到报头中，里面存储商品的集合
			PageDiv<Goods> pd=new PageDiv<>(pageNo, pageSize, total, goods);
		    req.setAttribute("pd", pd);
		    
		} catch (SQLException e) {
			System.out.println("Error_03:查询商品信息失败");
		}
		//请求调度
		req.getRequestDispatcher("show").forward(req, resp);
	}
}
