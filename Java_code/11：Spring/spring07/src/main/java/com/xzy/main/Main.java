package com.xzy.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xzy.dao.Email;
import com.xzy.pojo.Tear;

public class Main {

	public static void main(String[] args) 
	{
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
	    
		Email cp=(Email)context.getBean("gmail");
	   System.out.println(cp);
	   Email cp1=(Email)context.getBean("gmail");
	   System.out.println(cp1);
		
		Tear aa=(Tear)context.getBean("aa");
		System.out.println(aa);
		Tear aa1=(Tear)context.getBean("aa");
		System.out.println(aa1);
		
	}

}
