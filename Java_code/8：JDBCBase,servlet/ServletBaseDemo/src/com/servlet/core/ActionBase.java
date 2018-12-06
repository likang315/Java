package com.servlet.core;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 通过action参数来访问对应的方法
 * @author likang
 *
 */
public abstract class ActionBase extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String action=null!=req.getParameter("action")?req.getParameter("action"):"index";
		//反射访问方法
		Class[] paras=new Class[] {HttpServletRequest.class,HttpServletResponse.class};
		Object [] val=new Object[] {req,resp};
		
		Class clazz=this.getClass();
		try {
			Method method=clazz.getDeclaredMethod(action,paras);
	       
			if(null!=method)
	       {
	    	   method.invoke(this, val);
	       }else
	       {
	    	   System.out.println("ERROR_00100___没有对应的方法");
	       }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	  }
	
	public abstract void index(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException ;
}
