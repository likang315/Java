package com.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class CheckLoginUtils {
   public static void checkLogin(String uname,String upwd,PageContext pc)
   {
	   Connection con=ConnectionUtils.getConnection(pc);
	   String sql="select * from admin where uname=? and upwd=?";
	   
	   HttpSession hs=pc.getSession();
	   HttpServletResponse resp=(HttpServletResponse)pc.getResponse();
	     
	   try {
		   PreparedStatement ps=con.prepareStatement(sql);
		   ps.setString(1, uname);
		   ps.setString(2, MD5.tomd5(upwd));
		   ResultSet rs=ps.executeQuery();
		   if(rs.next())
		   {
			 
			   hs.setAttribute("loged", uname);
			   resp.sendRedirect("welcome.jsp");
			   
		   }else
		   {
			   resp.sendRedirect("index.jsp");
		   }
	} catch (Exception e) {
		e.printStackTrace();
	} 

   }
}
