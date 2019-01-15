package com.sp01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//Spring 的第二种配置扫描基包
public class Main {

	public static void main(String[] args) 
	{
		ApplicationContext context =
			new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
	    
		CellPhone cp=(CellPhone)context.getBean("cellPhone");
		cp.run();
	}

}
