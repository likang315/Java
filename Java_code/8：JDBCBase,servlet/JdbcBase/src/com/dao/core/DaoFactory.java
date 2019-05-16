package com.dao.core;

import com.dao.AdminDao;

public class DaoFactory {

	/**
	 * 根据接口的class，得到接口的实现类对象
	 * @param clazz
	 * @return
	 */
	public static Object getDao(Class clazz)
	{
		Object re=null;
		try {
			String classname=clazz.getName();
			int lastdot=classname.lastIndexOf(".");
			String str=classname.substring(0, lastdot)+".imp"+classname.substring(lastdot)+"Imp";
			re=Class.forName(str).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return re;
	}
	
	public static void main(String[] args) {
		Object obj=DaoFactory.getDao(AdminDao.class);
		System.out.println(AdminDao.class);
		System.out.println(obj);
	}
}
