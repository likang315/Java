package com.service.core;

import java.lang.reflect.Proxy;

public class ServiceFactory
{
	/**
	 * 通过Service接口得到service实现类的代理对象，增加了事务的功能
	 * @param clazz
	 * @return
	 */
	public static Object getService(Class clazz)
	{
		Object re=null;
		try {
			String classname=clazz.getName();
			int lastdot=classname.lastIndexOf(".");
			String str=classname.substring(0, lastdot)+".imp"+classname.substring(lastdot)+"Imp";
			Object tem=Class.forName(str).newInstance();
			
			TransactionInvocation tr=new TransactionInvocation(tem);
			re=Proxy.newProxyInstance(tem.getClass().getClassLoader(), tem.getClass().getInterfaces(), tr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return re;
	}

}
