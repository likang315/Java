package com.servlet.param;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionBase extends HttpServlet {

	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action=null!=req.getParameter("action")?req.getParameter("action"):"index";
		
		Class[] paras=new Class[] {HttpServletRequest.class,HttpServletResponse.class};
		Object [] val=new Object[] {req,resp};
		Class clazz=this.getClass();
		try {
			Method method=clazz.getDeclaredMethod(action);
	       if(null!=method)
	       {
	    	   //把req 和 resp 和全局变量对应起来，就不用传参数
	    	   this.req=req;
	    	   this.resp=resp;
	    	   method.invoke(this);
	       }else
	       {
	    	   System.out.println("ERROR_00100___没有对应的方法");
	       }
		
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * action判为null时，方法
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract void index() throws ServletException, IOException;

	
	public HttpServletRequest getReq() {
		return req;
	}
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}
	
	public HttpServletResponse getResp() {
		return resp;
	}
	public void setResp(HttpServletResponse resp) {
		this.resp = resp;
	}
	/**
	 * 封装params
	 * @param param
	 * @return
	 */
	public String getString(String param)
	{
		String re="";
		re=null!=req.getParameter(param)?req.getParameter(param):"";
		return re;
	}
	
	public String[] getStringArray(String param)
	{
		String[] re=req.getParameterValues(param);
		return re;
	}
	/**
	 * 得到param参数对应的int值 
	 * @param param
	 * @return
	 */
	public int getInt(String param)
	{
		int re=-1;
		String str=this.getString(param);
		if(str.matches("\\d+"))
		{
			re=Integer.parseInt(str);
		}
		return re;
	}
	
	/**
	 * 封装需要得到多次param参数时，和对象绑定，直接得到
	 * @param bean
	 */
	public void getBean(Object bean)
	{
		try {
			Class clazz=bean.getClass();
			Field all[]=clazz.getDeclaredFields();
			if(null!=all&&all.length>0)
			{
				for(Field f:all)
				{
					String fname=f.getName();
					Class types=f.getType();
					f.setAccessible(true);
					if(null!=types&&types==String.class)
					{
						f.set(bean, this.getString(fname));
					}else if(null!=types&&(types==Integer.class||types==int.class))
					{
						f.set(bean, this.getInt(fname));
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 封装调度
	 * @param path
	 */
	public void forward(String path)
	{
		try {
			req.getRequestDispatcher(path).forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
