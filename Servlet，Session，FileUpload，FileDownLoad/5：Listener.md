### 监听器（观察者设计模式）

###### 监听器的配置

​	1：@WebListener 
​	2：Web.xml，参考ppt

```xml
@WebListener 配制在具体是实现类上
<web-app>
	<listener>
		<listener-class>com.oracle.WebinitListener</listener-class>
	</listener>
</web-app>
```

###### 1：事件源：事件发生的厂所，tomcat

​	事件：ServletContextEvent
​	事件处理器：

```java
@WebListener 
public class WebInitListener implements ServletContextListener{
    	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		 System.out.println("------------------ServletContext contextDestroyed....."+arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		  System.out.println("------------------ServletContext init....."+arg0);
	}
}
```



##### ServletContextListener（Interface）

​		void	contextDestroyed(ServletContextEvent sce) 
​			只要 Servlet 容器，销毁 ServletContext 对象，contextDestroyed（）自动调用
​    		void	contextInitialized(ServletContextEvent sce)
​			只要 servlet 容器，实列化 ServletContext 对象，contextInitialized()方法自动调用 

##### ServletContextAttributeListener(Interface）

​		void	attributeAdded(ServletContextAttributeEvent event) 
​          		添加属性自动调用
 		void	attributeRemoved(ServletContextAttributeEvent event) 
​         		移除属性时自动调用
​		void attributeReplaced(ServletContextAttributeEvent event)
​			属性改变时，自动调用

##### ServletRequestListener

​	 void	requestDestroyed(ServletRequestEvent sre) 
​	 void	requestInitialized(ServletRequestEvent sre) 

##### HttpSessionBindingListener(Interface )

​	void	valueBound(HttpSessionBindingEvent event) 
​     		属性key-value被绑定时通知
​	void	valueUnbound(HttpSessionBindingEvent event) 	
​		属性key-value被解绑（移除）通知

##### HttpSessionAttributeListener(Interface)

​	void	attributeAdded(HttpSessionBindingEvent event) 
  		void	attributeRemoved(HttpSessionBindingEvent event) 
​	void	attributeReplaced(HttpSessionBindingEvent event) 

###### 实现：	

Step1：实现处理器接口,编写事件处理器
Step2: 注册给 Servlet 容器

