###  过滤器的配置：

​	1：@WebFilter("/admin/*")，通过调用顺序决定过滤顺序
​	2：Web.xml 配置文件,通过优先配置决定过滤顺序

##### 1：Filter（InterFace）：类似于多层代理模式，逐层过滤

​	作用：主要在访问 Servlet 之前和之后，可以增加一些我们的业务

void	destroy() 
    		销毁 Filter

######  void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 

​          	执行过滤
 void init(FilterConfig filterConfig) 
​        	初始化，一旦实例化对象调用

```java
@WebFilter("/admin/*")  //过滤检查登录
public class CheckLogedFilter implements Filter {
@Override
public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)  throws IOException, ServletException 
    {
        HttpServletRequest  req=(HttpServletRequest)arg0;
        HttpServletResponse resp=(HttpServletResponse)arg1;

        String url=req.getRequestURL().toString();

        HttpSession hs=req.getSession();
        Admin admin=(Admin)hs.getAttribute("loged");
        if(null!=admin||url.indexOf("admin/login")!=-1)
        {
            arg2.doFilter(req, resp);
        }else
        {
            resp.sendRedirect("login");
        }
    }
}
```



##### 2：FilterChain（InterFace ）：过滤链

void	doFilter(ServletRequest request, ServletResponse response) 
	调用下一个过滤器的doFilter()

##### 3：FilterConfig（InterFace）：过滤器配置文件

```xml
主要是重写dofilter()
<filter>
     <filter-name>welcome</filter-name>
     <filter-class>com.xupt.filter.WelcomeFilter</filter-class>
</filter>
<filter-mapping>
       <filter-name>welcome</filter-name>
       <url-pattern>/admin/*</url-pattern>
</filter-mapping>
```



##### 4：请求，响应包装类( Wrapper )

ServletRequestWrapper（Class） 和 ServletResponseWrapper（Class）
HttpServletRequestWrapper（Class）和 HttpServletResponseWrapper(Class)

######  作用：如果想重写 Request 和 Response 中的方法，那么就可以继承以上 4 个包装类

```java
public class MyRequest extends HttpServletRequestWrapper {
	public MyRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return "&lt;&lt;"+super.getParameter(name)+"&gt;&gt;";
	}
}
```



### 监听器（观察者设计模式）

######   监听器的配置

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


​         







