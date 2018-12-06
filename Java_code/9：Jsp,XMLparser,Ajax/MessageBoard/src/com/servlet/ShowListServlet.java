package com.servlet;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.bean.Msg;
import com.dbutils.Db;
import com.dbutils.PageDiv;
import com.servlet.core.ServletBase;

@WebServlet("/admin/show")
public class ShowListServlet extends ServletBase {

	@Override
	public void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 从数据库取出问题列表
		
		PageDiv<Msg> pd=null;
		
		int pageSize=10;
		int pageNo=1;
		int repageNo=this.getInt(req, "pageNo");
		if(repageNo>0)
			pageNo=repageNo;

		String sql1="select * from msg order by id desc limit ?,?";
		String sql2="select count(id) from msg";
		
		try {
			List<Msg> list=Db.query(sql1, new BeanListHandler<Msg>(Msg.class),(pageNo-1)*pageSize,pageSize);
	        Object obj=Db.query(sql2, new ArrayHandler())[0];
	        int totalCount=0;
	        if(null!=obj&&obj instanceof BigInteger)
	        {
	        	totalCount=((BigInteger)obj).intValue();
	        }
	        if(null!=obj&&obj instanceof Long)
	        {
	        	totalCount=((Long)obj).intValue();
	        }
	        pd=new PageDiv<Msg>(repageNo, pageSize, totalCount, list);
	        req.setAttribute("pd", pd);

		} catch (SQLException e) {
			System.out.println("ERROR_003:分页算法错误");
		}
		
		this.forwards(req, resp, "index.jsp");

	}

}
