package com.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.poji.Admin;
import com.utils.ConnectionUtils;
import com.utils.MD5;

/**
 * µÇÂ¼ÑéÖ¤µÄ
 * @author Administrator
 */
public class CheckLoginUtils {
   public static void checkLogin(Admin admin,PageContext pc)
   {
	   Connection con=ConnectionUtils.getConnection(pc);
	   String sql="select * from admin where uname=? and upwd=?";
	   
	   HttpSession hs=pc.getSession();
	   HttpServletResponse resp=(HttpServletResponse)pc.getResponse();
	   
	   try {
		   PreparedStatement ps=con.prepareStatement(sql);
		   ps.setString(1, admin.getUname());
		   ps.setString(2, MD5.tomd5(admin.getUpwd()));
		   ResultSet rs=ps.executeQuery();
		   if(rs.next())
		   {
			   hs.setAttribute("loged", admin);
			   resp.sendRedirect("welcome.jsp");
		   }else
		   {
			   resp.sendRedirect("index.jsp");
		   }
	}catch (Exception e) {
		e.printStackTrace();
	}
   }
   
}
