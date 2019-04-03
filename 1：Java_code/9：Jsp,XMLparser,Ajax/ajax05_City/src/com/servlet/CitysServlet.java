package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.db.Db;
@WebServlet("/city")
public class CitysServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		String pid=null!=req.getParameter("pid")?req.getParameter("pid"):"0";
		
		try {
			//parentid=0 时查出所有省份
			List<City> list=Db.query("select * from area where style=0 and parentid=?", new BeanListHandler<City>(City.class),pid);

			//=[{id:89,name:"万州区",parentid:5},{id:90,name:"万州区",parentid:5}]，将随想转换为Json
			StringBuilder sb=new StringBuilder();
			if(null!=list&&list.size()>0)
			{
				sb.append("[");
				int index=0;
				for(City c:list)
				{
					sb.append("{");
					sb.append("id:"+c.getId()+",");
					sb.append("name:\""+c.getName()+"\",");
					sb.append("parentid:"+c.getParentid());
				
					if(index++==list.size()-1)
						sb.append("}");
					else
						sb.append("},");
				}
				sb.append("]");
			}	
			out.print(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.close();
	}

}
