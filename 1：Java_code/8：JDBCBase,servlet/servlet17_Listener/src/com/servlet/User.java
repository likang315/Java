package com.servlet;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class User implements HttpSessionBindingListener {

	private String name;	
	public User() 
	{
	}
	
	public User(String name)
	{
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent arg0) {
	   System.out.println("ValueBound...."+arg0.getName()+"---"+arg0.getValue());
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		System.out.println("valueUnbound...."+arg0.getName()+"---"+arg0.getValue());
	}
	
	@Override
	public String toString() {
		return "###"+name+"###";
	}

}
