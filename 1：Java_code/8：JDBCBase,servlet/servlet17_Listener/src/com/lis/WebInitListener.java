package com.lis;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener 
public class WebInitListener implements ServletContextListener,ServletContextAttributeListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		 System.out.println("------------------ServletContext contextDestroyed....."+arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		  System.out.println("------------------ServletContext init....."+arg0);
	}

	
	@Override
	public void attributeAdded(ServletContextAttributeEvent arg0) {
		  System.out.println("ServletContext Add..."+arg0.getName()+"::"+arg0.getValue());
		
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		  System.out.println("ServletContext Removed..."+arg0.getName()+"::"+arg0.getValue());
		
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		 System.out.println("ServletContext Replace..."+arg0.getName()+"::"+arg0.getValue());
		
	}

}
