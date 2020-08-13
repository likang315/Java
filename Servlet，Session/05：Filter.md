### Filter

------

#####  1：过滤器的配置

1. @WebFilter ("/admin/*")，通过调用顺序决定过滤顺序
2. web.xml 配置文件，通过优先配置决定过滤顺序

###### javax.servlet  

##### 2：Interface Filter：

​		逐层过滤，在访问 Servlet 之前和之后，可以增加一些我们的业务

###### 原理

​	Filter.doFilter（） 中不能直接调用 Servlet 的 service（），而是调用 FilterChain.doFilter 方法来激活目标 Servlet 的 service（），**FilterChain** 对象时通过 Filter.doFilter 方法的参数传递进来的

- void init (FilterConfig filterConfig) 
  - 初始化对象调用，实例化再调用
- void destroy( )  
  - 销毁 Filter
- void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
  - 执行过滤

```java
@WebFilter("/admin/*")  
public class CheckLogedFilter implements Filter {
  @Override
  public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)  					throws IOException, ServletException {
    
      HttpServletRequest  req  = (HttpServletRequest)arg0;
      HttpServletResponse resp = (HttpServletResponse)arg1;
      String url = req.getRequestURL().toString();
      HttpSession session = req.getSession();
      Admin admin = (Admin) session.getAttribute("loged");
      if(null != admin || url.indexOf("admin/login") != -1) {
        // 调用访问的 Servlet
        arg2.doFilter(req, resp);
      } else {
        resp.sendRedirect("login");
      }
  }
}
```

###### javax.servlet  

##### 3：Interface FilterChain：

​	过滤链，用来调用 Servlet 的 services（）

- void doFilter (ServletRequest request, ServletResponse response) 
  - 调用下一个过滤器的doFilter() 或者servlet的services( )；

###### javax.servlet

##### 4：FilterConfig：

​	封装了 ServletContext 对象和 Filter 的配置参数信息，以及获取在 web.xml 文件中为 Filter 设置的 filter-name 和初始化参数。

```java
public interface FilterConfig {
  String getFilterName();
  ServletContext getServletContext();
  String getInitParameter(String var1);
  Enumeration<String> getInitParameterNames();
}
```

##### 5：Filter 注册和映射

###### 注解配置

- @WebFilter("/admin/*")  

###### xml配置

- Filter 的注册

  ```xml
  <filter>
      <filter-name>FirstFilter</filter-name>
      <filter-class>FirstFilter</filter-class>
      <init-param>
        	<!-- 可有多个 init-param  -->
          <param-name>encoding</param-name>
          <param-value>GB2312</param-value>
      </init-param>
  </filter>
  ```

- Filter 的映射

  - 指定资源的访问路径

    ```xml
    <filter-mapping>
        <filter-name>FirstFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```

  - 指定 Servlet 的名称

    ```xml
    <filter-mapping>
        <filter-name>FirstFilter</filter-name>
        <servlet-name>default</servlet-name>
        <dispatcher>INCLUDE</dispatcher>
        <dispatcher>REQUEST</dispatcher>
    </filter-mapping>
    ```

- < servlet-name> 元素与 < url-pattern> 元素是二选一的关系，其值是某个 Servlet 在 web.xml 文件中的注册名

- < dispatcher> 元素的设置值有 4 种，分别对应 **Servlet 容器调用资源的** 4 种方式
  - REQUEST：通过正常的访问请求调用
  - INCLUDE：通过 RequestDispatcher.include 方法调用
  - FORWARD：通过 RequestDispatcher.forward 方法调用
  - ERROR：作为错误响应资源调用
  
- 如果没有设置 < dispatcher> 子元素，则等效于 REQUEST 的情况，也可以设置多个 < dispatcher> 子元素，用于指定 Filter 对资源的多种调用方式都进行拦截。

- **Chain 顺序**

  - 以filter-mapping在web.xml中的顺序为准；
    - 首先遍历url-pattern符合的filter加入chain；
    - 其次遍历servlet-name符合的filter加入chain；
  - @WebFilter注解无法指定顺序；

##### 6：Servlet 和Filter 的区别

```xml
监控的配置：web.xml
<!-- filter 用于过滤请求的，过滤之后会传递给下一个过滤器-->
<filter>
	<filter-name>DruidWebStatFilter</filter-name>
	<filter-class>com.alibaba.druid.support.http.WebStatFilter</filter-class>
	<init-param>
		<param-name>exclusions</param-name>
		<param-value>*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>DruidWebStatFilter</filter-name>
	<url-pattern>/</url-pattern>
</filter-mapping>

<!-- servlet 是用来处理请求过来的事件，然后直接跳转到指定的界面-->
<servlet>
	<servlet-name>DruidStatView</servlet-name>
	<servlet-class>com.alibaba.druid.support.http.StatViewServlet</servlet-class> 
</servlet>
<servlet-mapping>
	<servlet-name>DruidStatView</servlet-name>
	<url-pattern>/druid/*</url-pattern>
<!--该配置可以访问监控界面，配置好后，访问 <http://ip:端口号/项目名/druid/index.html> 即可监控数据库访问性能-->
</servlet-mapping>
```

##### 7：请求，响应包装类( Wrapper )

- Class HttpServletRequestWrapper 、Class HttpServletResponseWrapper
- 如果想重写 Request 和 Response 中的方法，那么就可以继承以上包装类

```java
public class MyRequest extends HttpServletRequestWrapper {
    public MyRequest (HttpServletRequest request) {
      super(request);
    }
  
    // 重写了getParameter
    @Override
    public String getParameter(String name) {
      return "&lt;&lt;"+super.getParameter(name)+"&gt;&gt;";
    }
}
```
