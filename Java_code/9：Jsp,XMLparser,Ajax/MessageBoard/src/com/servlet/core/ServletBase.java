package com.servlet.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet的一个基类
 * @author Administrator
 *
 */
public abstract class ServletBase extends HttpServlet {
	
	//当参数判为 null时，调用
	public abstract  void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    
	/**
	 * 根据反射调用，方...当几个servlet用
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		String action= null!=req.getParameter("action")?req.getParameter("action"):"index";
		
		Class clazz=this.getClass();
		try {
			Method target=clazz.getDeclaredMethod(action, new Class[] {HttpServletRequest.class,HttpServletResponse.class});
			if(null!=target)
			{
				req.setCharacterEncoding("utf-8");
				target.invoke(this, new Object[] {req,resp});
			}
		} catch (Exception e) {
			 System.out.println("ERROR_01:目标方法不存在...");
		}
	}
	
	/**
	 * 封装了params ,和obj 形参对象绑定
	 * @param req
	 * @param obj
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getBean(HttpServletRequest req,Object obj) throws ServletException, IOException
	{
		//得到的时obj 参数的属性集合
		Class clazz=obj.getClass();
		Field all[]=clazz.getDeclaredFields();
		for(Field field:all)
		{
			try {
				field.setAccessible(true);
				String fname=field.getName();
				//属性是String
				if(field.getType()==String.class)
				{
					field.set(obj, this.getString(req, fname));
				}
				//属性是Int 或者 Integer
				if((field.getType()==int.class||field.getType()==Integer.class)&&this.getString(req, fname).matches("\\d+"))
				{
					field.set(obj,Integer.parseInt(this.getString(req, fname)));
				}
				
				if(field.getType()==Date.class)
				{
					String str=this.getString(req, fname);
					if(!("".equals(str)))
					{
						field.set(obj, new Date(java.sql.Date.valueOf(str).getTime()));
					}else
					{
						field.set(obj, new Date());
					}
				}
			} catch (Exception e) {
				System.out.println("ERROR_002:flect 错误");
			} 
		}
	}
	
	
	/**
	 * 封装了获取参数方法，做了判null处理
	 * @param req
	 * @param param
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String getString(HttpServletRequest req,String param) throws ServletException, IOException
	{
		req.setCharacterEncoding("UTF-8");
		return (null!=req.getParameter(param)?req.getParameter(param):"");
	}
	 
	/**
	 * 获取 int 参数
	 * @param req
	 * @param param
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
		public int getInt(HttpServletRequest req,String param) throws ServletException, IOException
		{
			int re=0;
			String strp=null!=req.getParameter(param)?req.getParameter(param):"";
			if(strp.matches("\\d+"))
				re=Integer.parseInt(strp);
			return re;
		}
		
	/**
	 * 封装了调度方法，传递绝对路径
	 * @param req
	 * @param resp
	 * @param path
	 * @throws ServletException
	 * @throws IOException
	 */
	public void forwards(HttpServletRequest req, HttpServletResponse resp,String path)throws ServletException, IOException
	{
		RequestDispatcher rd=req.getRequestDispatcher("/WEB-INF/pages/"+path);
		rd.forward(req,resp);
	}

}
