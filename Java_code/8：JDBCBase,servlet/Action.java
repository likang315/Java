package com.hq.servlet.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;


public abstract class Action extends HttpServlet
{
	
	private static final long serialVersionUID = 898189292634920380L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// 防止IE缓存
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setCharacterEncoding("UTF-8");
		String method= null!=req.getParameter("action")?req.getParameter("action"):"index";
		//Class[]param=new Class[] {HttpServletRequest.class,HttpServletResponse.class};
		//Class[]methodparam=new Class[] {Mapping.class};
	 
		Class<? extends Action> clazz=this.getClass();
		//Mapping map=new Mapping(req,resp);
		try {
			//访问一个带Mapping参数的方法
			Method m=clazz.getDeclaredMethod(method, Mapping.class);
			if(null!=m)
			{
				m.invoke(this, new Mapping(req,resp));
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			 System.out.println("ERROR_001_找不到目标方法！"+e.getMessage());
			 //跳转到找不到页面
			 req.getRequestDispatcher("error/error404.jsp").forward(req, resp);
		
		}
	}
	
	
	//默认要实现的方法
	public abstract void index(Mapping map) throws ServletException, IOException;
	
	/**
	 * 用于封装内部类，因为成员变在多线程模式中可能会出问题
	 * @author Administrator
	 *
	 */
	public class Mapping 
	{
		 HttpServletRequest req;
		 HttpServletResponse resp;
		 
		 public Mapping(HttpServletRequest req, HttpServletResponse resp) 
		 {
				this.req = req;
				this.resp = resp;
		 }
		 
		protected SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		//取字符串
		public String getString(String param)
		{
			return null!=req.getParameter(param)?req.getParameter(param):"";
		}
	    //取字符串数组
		public String[] getStringArray(String param)
		{
			return req.getParameterValues(param);
		}
		//返回一个数字参数
		public int getInt(String param)
		{
			int re=0;
			String str=this.getString(param);
			if(str.matches("\\d+"))
			{
				re=Integer.parseInt(str);
			}
			return re;
		}
		public long getLong(String param)
		{
			long re=0;
			String str=this.getString(param);
			if(str.matches("\\d+"))
			{
				re=Long.parseLong(str);
			}
			return re;
		}
		//填充请求参数到Bean对像
		public void getBean(Object bean)
		{
			Class<? extends Object> clazz=bean.getClass();
			Field [] all=clazz.getDeclaredFields();
			if(null!=all&&all.length>0)
			{
				try {
					for(Field f:all)
					{
						f.setAccessible(true);
						String fname=f.getName();//字段名
						Class<?> types=f.getType();//属性和类型
						String paramv=this.getString(fname);
						if(types==String.class)
						{
						     f.set(bean, paramv);
						}else if(types==Integer.class||types==int.class||types==Integer.TYPE)
						{
							f.set(bean, this.getInt(fname));
						}else if(types==Long.class||types==long.class||types==Long.TYPE){
							if(paramv.matches("\\d+"))
							f.set(bean, Long.parseLong(paramv));
						}else if(types==Date.class)
						{
							if(paramv.matches("\\d{4}[-]\\d{2}[-]\\d{2}[ ]\\d{2}[:]\\d{2}[:]\\d{2}"))
							{
								f.set(bean, sf.parse(paramv));
							}
						}
					}
				} catch (Exception e) {
					 System.out.println("ERROR_002_填充对像值出错。。。。");
				} 
			}
		}
		//对 request的访问
		public Object getAttr(String key)
		{
			return req.getAttribute(key);
		}
		//设置 
		public void setAttr(String key,Object value) {
			req.setAttribute(key, value);
		}
		//跳转
		public void forward(String path) throws ServletException, IOException
		{	
				req.getRequestDispatcher("/WEB-INF/"+path).forward(req, resp);
		}
		//跳转
		public void redirect(String path) throws ServletException, IOException
		{	
				resp.sendRedirect(path);
		}
		//直接输出值
		public void randerText(String msg)throws ServletException, IOException
		{
			resp.setContentType("text/html;charset=UTF-8");
			
			PrintWriter out=resp.getWriter();
			 
			out.println(msg);
			
			out.close();
		}
		//输出json
		public void randerJson(Object obj)throws ServletException, IOException
		{
			resp.setContentType("application/json;charset=UTF-8");
			
			PrintWriter out=resp.getWriter();
			 
			out.print(JSON.toJSONString(obj));
			
			out.close();
		}
		//从Session中拿值
		public Object getSessionAttr(String param)
		{
			return req.getSession().getAttribute(param);
		}
		//设置Session值
		public void setSessionAttr(String key,Object val)
		{
			req.getSession().setAttribute(key, val);
		}
		public void removeSessionAttr(String key)
		{
			req.getSession().removeAttribute(key);
		}
		public void invalidateSession()
		{
			req.getSession().invalidate();
		}
		//-------------------------------
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
		
	}//end Mapping
	

}
