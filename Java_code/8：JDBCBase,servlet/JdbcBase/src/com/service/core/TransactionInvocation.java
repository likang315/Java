package com.service.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

import com.dao.core.ConnectionManager;

/**
 * 代理对象，在dao层之上增加了事务
 * @author likang
 *
 */
public class TransactionInvocation implements InvocationHandler {
    
	private Object org;
	public TransactionInvocation(Object org)
	{
		this.org=org;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		ConnectionManager cm=ConnectionManager.newInstance();
		
		cm.beginTransaction();
		Object obj=null;
		try
		{
			obj=method.invoke(org, args);
	   		cm.commint();
		}catch(Exception e)
		{
			cm.rollback();
			e.printStackTrace();
		}
		
		return obj;
	}
	
	

}
