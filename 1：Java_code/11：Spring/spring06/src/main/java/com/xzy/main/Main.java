package com.xzy.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xzy.service.UserService;

public class Main {

	public static void main(String[] args) 
	{
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
	    
		UserService cp=(UserService)context.getBean("us");
		cp.check();
		
		
	}

}
