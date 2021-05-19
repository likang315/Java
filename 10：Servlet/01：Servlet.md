### Servlet

------

[TOC]

##### 01：概述

###### CGI (Common Gateway Interface)：

- **通用网关接口**，指 Web服务器 在接受到客户端发送过来的请求后**转发给程序**的一组机制；
- 缺陷：每次收到请求，程序都要跟着启动一次，一旦访问量过大，Web服务器要承担相当大的负载；

###### Servlet

- 在服务器中运行的小型Java程序，用于**接收和响应来自Web客户端的请求，一个servlet就是一个服务**；
- 它的生命周期由 Servlet 容器 (Tomcat) 来管理；
- Servlet  **线程安全的**，单实例常驻于服务器内存的，使用 **ThreadLocal** 保证现场安全；
- 实现 javax.servlet.Servlet 接口，重写 services 方法；

##### 02：Servlet 作用

- **读取**客户端发送的数据，比如：网页上的 HTML 表单， cookies、媒体类型；
- **处理**数据并生成结果，这个过程可能需要访问数据库或者直接计算得出对应的响应；
- 给客户端**响应**数据，该文档的格式可以是多种多样的，包括文本文件（HTML 或 XML）、GIF 图像、Excel 等；

##### 03：引入 Servlet 

- Servlet 可以使用 **javax.servlet** 和 **javax.servlet.http** 包创建，它是 Java 企业版的标准组成部分；

```xml
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.1.0</version>
	<scope>provided</scope>
</dependency>
```

##### 04：部署描述文件是否是完全的

- web.xml 的顶层标签 < web-app > 的 metadata-complete 属性，该属性指定当前的部署描述文件是否是完全的；
  - 如果设置为 true，则容器在部署时将只依赖部署描述文件，忽略所有的注解；
  - 将其设置为 false，则表示启用注解支持；

##### 05：配置 servlet 路径

1. web.xml 配置文件，配置字节码文件 和 URL的映射；
2. 用 Annotation(注解) 配置 Servlet；
   - @WebServlet("/xxx")   


```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/XMLSchema-instance" metedata-complete="false" >
<display-name>Servlet</display-name>
  <!-- servlet 和 servlet-mappping 映射 ，就是一个ServletConfig-->
  <servlet>
       <servlet-name>CheckLoginServlet</servlet-name>
       <servlet-class>com.xupt.MySecondServlet</servlet-class>
       <init-param>
         <param-name>driver</param-name>
         <param-value>com.mysql.jdbc.Driver</param-value>
       </init-param>
       <load-on-startup>0</load-on-startup>
  </servlet>
  
  <servlet-mapping>
       <servlet-name>CheckLoginServlet</servlet-name>
       <url-pattern>/checkLogin</url-pattern>
  </servlet-mapping>
  
  <!-- 按顺序默认访问-->
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


```java
@WebServlet("/checkLogin")
public class CheckLoginServlet extends HttpServlet {
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws Exception {
			// ...
	}
}
```

##### 06：Servlet 生命周期【重要】

1. 启动 tomcat ，工程的 web.xml 会被解析， **实例化 ServletContext 对象**；
2. 扫描 Servlet 类上的 Annotation，为**每一组 Servlet 的配置都生成一个 ServletConfig** 对象，共享ServletContext对象；
   - 如果 **loadOnStartup （Annotation上一个属性）有值**，启动 tomcat 就实例化 servlet 对象并调用 init 方法，否则话，第一次访问才实例化 Servlet 对象，loadOnStartup 值越小，越优先实例化；
3. 用户第一次通过 url 访问 web 资源；
4. Servlet 容器（tomcat）会检查用户访问的 url 是不是对应一个 Servlet，请求会比对服务器上每一个 **ServletConfig 对象所封装的 url 是不是和你请求的 url 相同**，如果相同就找到了目标 Servlet 对应的 ServletConfig 对象；
5. **实例化 Servlet 对象**，并调用 init( ) 把对应的 ServletConfig 对象传给 Servlet；
6. 自动调用 Servlet 对象的 service( )；
7. 第二次访问的 URL 对应 Servlet 时，直接调用 Servlet 对象的 service 方法，**Servlet 是单实例长驻服务器内存的，只有第一次访问才实例对象，并调用 init 方法**;
8. 当服务器宕机时，会调用 Servlet 的 destory（），销毁 Servlet对象；

##### 07：services( )

- Servlet 容器自动调用 service() 方法来**处理请求，**并把格式化的响应返回给客户端；
- 每次服务器接收到一个 Servlet 请求时，服务器会**新启一个线程调用服务**；
  - service() 方法检查 HTTP 请求类型（GET、POST、PUT、DELETE 等），并在适当的时候调用你重写的 doGet、doPost、doPut，doDelete 等方法，也可以直接处理；

###### 示例

```java
public void service(ServletRequest request, ServletResponse response) 
      throws ServletException, IOException{
     // 若不处理，则调用 doXXX 方法处理
}
```