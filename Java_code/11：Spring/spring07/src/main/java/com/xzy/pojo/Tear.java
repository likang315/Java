package com.xzy.pojo;

import java.util.Properties;

public class Tear {

	private Properties pop;
	
	
	public Tear()
	{
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}
	
	
	public void init()
	{
		System.out.println("创建对像后调用");
	}
	
	public void dey()
	{
	   System.out.println("destory.....");
	}
	
	
	public void display()
	{
		pop.list(System.out);
	}



	public Properties getPop() {
		return pop;
	}



	public void setPop(Properties pop) {
		this.pop = pop;
	}
	
	
	
}
