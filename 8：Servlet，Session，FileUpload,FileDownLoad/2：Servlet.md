###### 事先准备好的内容(HTML)称为静态内容

###### 类似由程序创建的内容称为动态内容,Web 应用则作用于动态内容之上

CGI(Common Gateway Interface)：通用网关接口，指Web服务器在接受到客户端发送过来的请求后转发给程序的一组机制
缺陷：每次收到请求，程序都要跟着启动一次，一旦访问量过大，Web服务器要承担相当大的负载

## Servlet：一种能在服务器上创建动态内容的程序

### 1：Servlet  ( 服务连接器 )：用 Java 编写的服务器端程序，主要功能在于交互式地浏览和修改数据，生成动态Web内容

​	1: 没有 main 方法，它的生命周期由 Servlet 容器 (Tomcat) 来管理
​	2: 必须实现 javax.servlet.Servlet 接口

### 2：servlet 方式（两种）

​	1：web.xml配置文件 配置字节码文件和URL的映射
​	2：用 Annotation(注解) 配置 Servlet

###### @WebServlet("/xxx")   -----其实也是站点的上一个URL

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" metedata-complete="false" >
<display-name>Servlet</display-name>
  
  <servlet>
       <servlet-name>CheckLoginServlet</servlet-name>
       <servlet-class>com.xupt.MySecondServlet</servlet-class>
  </servlet>
  
  <servlet-mapping>
       <servlet-name>CheckLoginServlet</servlet-name>
       <url-pattern>/checklogin</url-pattern>
  </servlet-mapping>
 
  <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>default.html</welcome-file>
        <welcome-file>default.htm</welcome-file>
        <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
</web-app>
```

###### 	@WebServlet(value="/xxx",name=".class名"）

```java
@WebServlet("/checkLogin")
public class CheckLoginServlet extends GenericServlet 
{
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws Exception
```



######   web.xml 的顶层标签 <web-app> 有一个 metadata-complete 属性，该属性指定当前的部署描述文件是否是完全的

  如果设置为 true，则容器在部署时将只依赖部署描述文件，忽略所有的注解
  将其设置为 false，则表示启用注解支持



### 3：ServletContext (Interface) ：提供了servlet与 其 容器(Tomcat)通信的一组方法，环境

特点：

###### 1：Tomcat启动时，每个站点的 web.xml 会被解析实例化 ServletContext 对象

###### 2：同一个 web 应用，所有 ServletConfig 共享 ServletContext

###### 3：ServletContext 对象的生命周期是伴随服务器生命周期（tomcat 启动，实例化 servletContext 对象，tomcat关闭，才释放 ServletContext）



方法：
java.lang.Object	getAttribute(java.lang.String name) 
		根据指定的属性名返回属性,web.xml 的属性
java.util.Enumeration<java.lang.String>	 getAttributeNames() 
		返回所有属性名的集合 

java.lang.String	getInitParameter(java.lang.String name) 
    	获取初始参数
java.util.Enumeration<java.lang.String>	getInitParameterNames() 
    	获取所有初始化参数名的集合

java.lang.String	getMimeType(java.lang.String file)  
	获取指定资源 mime 类型（媒体类型-Content-Type）

###### java.lang.String	getRealPath(java.lang.String path)  

​		获取指定资源的绝对路径

###### RequestDispatcher	 getRequestDispatcher(java.lang.String path) 

​		获取RequestDispatcher对象
java.io.InputStream	getResourceAsStream(java.lang.String path) 
​		获取资源的输入流



### 4：ServletConfig (Interface)

######      Tomcat 在启动时，加载 web.xml 或 读取 Servlet 类上的 Annotation(注释) 的 Servlet 配置，将这些配置信息封装成一个ServletConfig 对象，会为每组 Servlet 配置各生成一个 ServletConfig 对象

 java.lang.String	getInitParameter(java.lang.String name) 
	通过指定的参数，返回它的值
 java.util.Enumeration<java.lang.String>    getInitParameterNames() 
	返回参数的值

######  ServletContext	getServletContext() 

​      	返回ServletContext	实例

  java.lang.String	getServletName() 
         	返回servlet名字

######   ServletConfig 对象中封装了（web.xml配置文件中）：

​	1、Servlet 的名字
​	2、Servlet 的字节码在哪里
​	3、Servlet 对应的 URL
​	4、封装了 servlet 初始参数



### 5：Servlet (Interface)：定义了一些初始化 servlet 的方法，以及所有servlet必须实现的接口

void init(ServletConfig config) 
	实例化 Servelt 对象后，会调用 init(),用于将 ServletConfig 对象传给 Servlet
void    destroy() 
	用于销毁 Servlet 所初始化的资源

######  void    service(ServletRequest req, ServletResponse resp)  

​	接收用户的请求，并给用户响应

###### ServletConfig getServletConfig()

​	用于获取ServletConfig 对象


######  如果直接实现 Servlet 接口比较麻烦，我们继承 GenericServlet,因为GenericServlet 实现了 Servlet和 ServletConfig 接口

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



### 6：GenericServlet（Class）

​    GenericServlet 实现了 Servlet 接口和 ServletConfig 接口，但是 servlet 接口的 service 方法没有被实现，
​    增加了一个方法 init()，用于调用（父接口）servlet 接口中的 init(ServletConfig scf)，还增加了两个 log 做日志的方法

### 7：HttpServlet（Class）: 继承了 GenericServlet,实现了 servlet 接口和 ServletConfig 接口，以后写Servlet 都继承于HttpServlet

   方法：

###### 	   public    void	service(ServletRequest req, ServletResponse res) 

###### 			自动调用 protected  services 方法

​	protected  void  service(HttpServletRequest req, HttpServletResponse resp) 
​		自动doXXX 方法
​	protected  void	doGet(HttpServletRequest req, HttpServletResponse resp)
​		用来执行此请求方法的请求



### 8：ServletRequest 和 ServletResponse

#####    ServletRequest(Interface)：封装了对客户端的输入流，有一个 Map<String,Object> 成员

###### 获取用户请求参数

###### void	setCharacterEncoding(java.lang.String env) 

​		设置请求编码格式			

###### 	Enumeration<java.lang.String>  getParameterNames() 

​		得到所有参数名的集合

###### 	String[]     getParameterValues(java.lang.String name) 

​		通过参数名，得到所有参数值的数组

###### java.lang.Object	getAttribute(java.lang.String name) 

​			通过参数名，得到一个值

###### void	setAttribute(java.lang.String name, java.lang.Object o) 

​			通过map.put 设置参数

Map<String,Object>   getParameterMap()
​			返回所有参数的封装
void	removeAttribute(java.lang.String name) 
​    			移除参数



###    ServletResponse（Interface）：封装了对客户端的输出流

###### 	void	setContentType(java.lang.String type) 

​		设置响应内容

###### java.io.PrintWriter	getWriter() 

​		返回字符输出流

###### ServletOutputStream	getOutputStream() 

​		返回字节输出流

```java
req.setCharacterEncoding("utf-8");
resp.setContentType("text/html;charset=utf-8");
PrintWriter out=resp.getWriter();
out.println("<script> alert('yes');window.location='success.html';</script>");
```



### 9：HttpServletRequest 和 HttpServletResponse

######   HttpServletRequest(Interface)：实现了ServletRequest 接口，扩展了可以获取 http 协议请求报头的信息，可以获Session等

  方法：
	java.lang.String	getHeader(java.lang.String name) 
		返回请求报头
	java.util.Enumeration<java.lang.String>	getHeaderNames() 
		返回请求报头的所有属性名（key）
	java.lang.String	getQueryString() 
		返回查询字符串（登录信息）
	java.lang.StringBuffer	getRequestURL() 
         	返回URL

#####   HttpServletResponse（Interface）：实现了ServletResponse 接口，扩展了增加响应报头的功能，重设响应服头的功能，写cookie 的功能，增加了一个重定向的功能

 void	addHeader(java.lang.String name, java.lang.String value) 
	增加响应报头

######  void	setHeader(java.lang.String name, java.lang.String value) 

​	设置响应报头值

######  java.lang.String	getHeader(java.lang.String name) 

​     	得到响应报头

 	 java.util.Collection<java.lang.String>    getHeaderNames() 
		得到响应报头属性（key）

###### int	getStatus() 

​	得到当前的状态码

###### void	setStatus(int sc) 

​	设置状态码

###### 	 void	sendRedirect(java.lang.String location) 

###### 		设置跳转的页面，重定向

​	

### 10：RequestDispatcher（Interface)：请求调度,转发,跳转，只能是自己服务器的资源间转发，而重定向可以重定向到其它服务器的资源	

######   得到RequestDispatcher对象（三种方法）

​	 ServletReqeust.getRequestDispatcher(“绝对路径”) -----------也可用相对路径，最常用
​	 ServletContext.getRequestDispatcher(“/ 相对路径”) ---------一定要用/开始
​	 ServletContext. getNamedDispatcher(“Servlet 的名字”) ------用 servlet 的 name 来找 Servlet

  方法：
	void	forward(ServletRequest request, ServletResponse response) 
         	跳转请求资源直接返回给客户端
	void	include(ServletRequest request, ServletResponse response) 
		在响应中包含请求资源的内容，注意跳转的输出流不能关闭，否则无法输出



### 7：Servlet 生命周期

​	Step1：启动tomcat ，每个站点的 web.xml 会被解析， **实例化 ServletContext 对象**
​	Step2：读取 Servlet 类上的 Annotation ,为每一组 Servlet 的配置都生成一个 **ServletConfig 对象,**
​	             共享ServletContext对象
​	Step3：用户第一次通过 url 访问 web 资源

​	Step4：Servlet 容器（tomcat）会检查用户访问的 url 是不是对应一个 Servlet，服务器会比对服务器上每一个 **ServletConfig 对象所封装的 url 是不是和你请求的 url 相同**，如果相同就找到了目标 Servlet 对应的 ServletConfig 对象

​	Step5:实例化 Servlet 对象，并调用 init 方法把对应的 ServletConfig 对象传给 Servlet
​	Step6:自动调用 Servlet 对象的 service 方法

​	Step7:第二次访问的 URL 对应 Servlet 时，直接调用 Servlet 对象的 service 方法

###### ​		Servlet 是单实例长驻服务器内存的，只有第一次访问才实例对象，并调用 init 方法

​	Step8:当服务器宕机时，会调用 Servlet 的 destory（）

如果 loadOnStartup （Annotation上一个属性）有值,启动 tomcat 就实例化 servlet 对象并调用 init 方法
否则话，第一次访问才实例化 Servlet 对象，loadOnStartup 值越小，越优先实例化



### 11：客户端给服务器传参数

######   get 方法传参：在请求路径后追加请求参数

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

######   Post 传参：参数做为 http 请求消息的报体传给服务器

```http
POST /welcome HTTP/1.1
Host: www.baidu.com

Name=李四&age=22&sex=F
```

POST 传参相对比较安全，可以传大量的数据


