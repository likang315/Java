###### javax.servlet  

### 1：Interface  ServletContext ：

​		上下文环境，提供 servlet 与其容器 (Tomcat) 通信的一组方法

###### 特点：

- Tomcat 启动时，每个站点的 web.xml 会被解析实例化 ServletContext 对象
- 这个对象全局唯一的，而且同一个工程， servletConfig 共享 ServletContext
- ServletContext 对象的生命周期是伴随其容器的生命周期（tomcat 启动，实例化 servletContext 对象，tomcat 关闭，才释放 ServletContext）

###### 方法：

- java.lang.Object	getAttribute(java.lang.String name) 
  		根据指定的属性名返回属性,web.xml 的属性

- java.util.Enumeration<java.lang.String>	 getAttributeNames() 
  		返回所有属性名的集合 

- java.lang.String	getInitParameter(java.lang.String name) 
      	获取初始化参数

- java.util.Enumeration<java.lang.String>	getInitParameterNames() 
      	获取所有初始化参数名的集合

- ###### java.lang.String	getMimeType(java.lang.String file) 

  ​	获取指定资源 mime 类型（媒体类型-Content-Type）

- ###### java.lang.String	getRealPath(java.lang.String path)  

  ​	获取指定资源的绝对路径

- ###### RequestDispatcher  getRequestDispatcher(java.lang.String path) 

  ​	获取跳转到指定资源的 RequestDispatcher 对象

- java.io.InputStream	getResourceAsStream(java.lang.String path) 
  		获取资源的输入流



###### javax.servlet  

### 2：Interface  ServletConfig

​	Tomcat 在启动时，先加载 web.xml ，封装成ServletContext，再读取 **Servlet 类上的 Annotation(注释)的Servlet 配置，将这些配置信息封装成一个ServletConfig 对象**，所有的ServletConfig 共享一个ServletContext

- ServletContext	getServletContext() 
  - 返回ServletContext实例
-  java.lang.String	getServletName() 
  - 返回 servlet 名字

###### ServletConfig 对象中封装了：

1. Servlet 的名字
2. Servlet 的字节码在哪里
3. Servlet 对应的 URL
4. 封装了 servlet 初始参数



###### javax.servlet  

### 3：Interface  Servlet

​	定义了一些初始化 servlet 的方法，处理用户请求并且相应给用户结果

- void init(ServletConfig config)
  - 实例化 Servelt 对象后，会调用 init(),用于将 ServletConfig 对象传给 Servlet
- void    destroy()
  - 用于销毁 Servlet 所初始化的资源
- void    service (ServletRequest req, ServletResponse resp)  
  - 接收用户的请求，处理请求并响应给用户
- ServletConfig  getServletConfig()
  - 用于获取当前 ServletConfig 对象

```java
@Override
protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
{
		req.setCharacterEncoding("UTF-8");
		String method= null!=req.getParameter("action")?req.getParameter("action"):"index";
		Class[] param=new Class[] {HttpServletRequest.class,HttpServletResponse.class};
	 
		Class clazz=this.getClass();
		try {
			Method m = clazz.getDeclaredMethod(method, new Class[] {});
			if(null!=m)
			{
				this.req=req;
				this.resp=resp;
				m.invoke(this, new Object[] {});
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR_001_找不到目标方法！");
		}
}
```



###### javax.servlet 

### 4：Class GenericServlet

​    GenericServlet 实现了 Servlet 接口和 ServletConfig 接口

- abstract  void  service (ServletRequest req, ServletResponse res)         



###### javax.servlet.http 

### 5：Class HttpServlet

​		继承了 GenericServlet，实现了 servlet 接口和 ServletConfig 接口，以后写 Servlet 都继承于HttpServlet

- protected  void  doDelete (HttpServletRequest req, HttpServletResponse resp)

- ###### protected  void  doGet (HttpServletRequest req, HttpServletResponse resp)

  - 用来执行请求的方法

- protected  void  doHead (HttpServletRequest req, HttpServletResponse resp)

- protected  void  doOptions (HttpServletRequest req, HttpServletResponse resp)

- protected  void  doPost (HttpServletRequest req, HttpServletResponse resp)

- protected  void  doPut (HttpServletRequest req, HttpServletResponse resp)

- protected  void  doTrace (HttpServletRequest req, HttpServletResponse resp）

- protected  long  getLastModified (HttpServletRequest req)

- ###### protected  void  service (HttpServletRequest req, HttpServletResponse resp)

  - 被 public  service（）调用，用来自动调用 doXXX（）

- ###### void  service (ServletRequest req, ServletResponse res)

  - 客户端请求之后，被容器调用去自动调用 protected   service（）



###### javax.servlet

### 6：Interface ServletRequest

​	封装了对客户端的输入流，有一个 Map<String,Object> 成员

- ###### void	setCharacterEncoding (java.lang.String env) 

  - 设置请求编码格式			

- Enumeration<java.lang.String>  getParameterNames() 

  - 得到所有参数名的集合

- String[]    getParameterValues(java.lang.String name) 

  - 通过参数名，得到所有参数值的数组

- ###### java.lang.Object	getAttribute (java.lang.String name) 

  - 通过参数名，得到一个值

- ###### void	setAttribute (java.lang.String name, java.lang.Object o)

  - 通过 map.put 设置参数

- Map<String,Object>   getParameterMap()

  - 返回所有参数的封装

- void	removeAttribute(java.lang.String name) 

  - 移除参数



###### javax.servlet 

### 7：Interface ServletResponse

​	封装了对客户端的输出流

- void	setContentType (java.lang.String type) 
  - 设置响应内容
- java.io.PrintWriter	getWriter() 
  - 返回字符输出流
- ServletOutputStream	getOutputStream() 
  - 返回字节输出流
- ` void`  flushBuffer ( )   
  - 强制将缓存中的内容写入到客户端

```java
req.setCharacterEncoding("utf-8");
resp.setContentType("text/html;charset=utf-8");
PrintWriter out = resp.getWriter();
out.println("<script> alert('yes');window.location='success.html';</script>");
```



###### javax.servlet.http 

### 8：Interface HttpServletRequest

​	实现了ServletRequest 接口，扩展了可以获取 http 协议请求报头的信息，可以获Session 等

- Cookie[]  getCookies（） 
  - 返回请求Cookie 的值
- java.lang.String    getRequestedSessionId （）
  - 返回请求的Session ID值
- java.lang.String	getHeader(java.lang.String name) 
  - 返回指定请求报头
- java.util.Enumeration<java.lang.String>	getHeaderNames() 
  - 返回请求报头的所有属性名（key）
- java.lang.String  getMethod （）
  -  返回请求的HTTP方法
- java.lang.String	getQueryString()
  - 返回查询字符串（登录信息）
- java.lang.StringBuffer	getRequestURL() 
  - 返回 URL

###### javax.servlet.http  

### 9：Interface HttpServletResponse

​		实现了ServletResponse 接口，扩展了增加响应报头的功能，重设响应服头的功能，写cookie 的功能，增加了一个重定向的功能

- void	addHeader(java.lang.String name, java.lang.String value)

  - 增加响应报头

- void	setHeader(java.lang.String name, java.lang.String value) 

  - 设置响应报头值

- java.lang.String	getHeader(java.lang.String name) 

  - 得到响应报头

- java.util.Collection<java.lang.String>    getHeaderNames() 

  - 得到响应报头属性（key）

- ###### int	getStatus() 

  - 得到当前的状态码

- ###### void	setStatus(int sc) 

  - 设置状态码

- ###### void	sendRedirect(java.lang.String location) 

  - 重定向，可以重定向到别的服务器资源

​	

###### javax.servlet 

### 10：Interface RequestDispatcher：

​	请求调度，转发，只能是自己服务器的资源间转发，而重定向可以重定向到其它服务器的资源	

##### 获得 RequestDispatcher 对象（三种方法）

1. ###### ServletReqeust.getRequestDispatcher( "绝对路径" )：也可以用相对路径

2. ServletContext.getRequestDispatcher(“/ 相对路径”) ：一定要从根开始

3. ServletContext. getNamedDispatcher(“Servlet 的名字”) ：用 servlet 的 name 来找 目标Servlet

- void	forward (ServletRequest request, ServletResponse response) 
  - 朝前，跳转请求资源就直接返回给客户端
- void	include(ServletRequest request, ServletResponse response) 
  - 包含，在响应中包含请求资源的内容，注意跳转的输出流不能关闭，否则无法输出

### 11：GET ，POST 传参

##### get 方法传参：在请求路径后追加请求参数

​	GET /path?param1=value1&param2=value2... HTTP/1.1
​	Host: xxxx
​	....
​	Get 传参明确、但是不够安全、而且数据量不能太大，32k

```html
方法一：超连接
	<a href= "welcome?uname=zhangsan" >welcome</a>
方法二：表单传参，如果没有指定 method，默认为 get
	<form method=”get”> <from >
```



##### Post 传参：参数做为 http 请求消息的报体传给服务器

```http
POST /welcome HTTP/1.1
Host: www.baidu.com

Name=李四&age=22&sex=F
```

POST 传参相对比较安全，可以传大量的数据

