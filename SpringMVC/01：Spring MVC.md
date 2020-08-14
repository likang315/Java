### Spring MVC

------

![](https://github.com/likang315/Middleware/blob/master/SpringMVC/SpringMVC/Spring%20MVC.png?raw=true)

##### 1：MVC Request 流程（五大步）

1. ######  前端控制器：请求先访问Spring的 DispatcherServlet (前端控制器)

   - Spring MVC 所有的请求都会通过一个前端控制器（front controller），是常用的 Web 应用程序模式，一个单实例的Servlet 将请求委托给WEB应用程序的其他组件来执行实际的处理

2. ######  处理映射：DispatcherServlet 将请求转发给 Spring MVC 控制器（controller）

   - 控制器是一个用于处理请求的 Spring 组件，DispatcherServlet 需要知道应该将请求发送给哪个控制器，所以 DispatcherServlet会查询一个或多个**处理器映射（handler mapping）**来确定请求的下一站在哪，处理器映射会根据请求所携带的 URL 信息来进行决策，Map的key

3. ######  控制器处理：选择合适的控制器后，DispatcherServlet 会将请求发送给选中的控制器 

   - 到了控制器，请求会卸下其负载（用户提交的信息）并等待控制器处理这些信息，将处理的结果封装成Model ，通常由POJO组成

4. ######  控制器将模型和视图名返回给前端控制器：

   - 控制器会将请求连同模型数据和用于渲染的视图名发送回 DispatcherServlet，DispatcherServlet 将根据视图名选择相应的视图解析器（ ViewResolver）

5. ######  视图的实现（可能是JSP）：

   - 将它交付的模型数据，使用视图解析器将模型数据渲染输出，然后通过响应对象传递给客户端

##### 2：Spring MVC 配置

###### 1：在 WEB-INF/lib 中导入 jar

- spring-web-4.3.7.RELEASE.jar
- spring-webmvc-4.3.7.RELEASE.jar

###### 2：配置 DispatcherServlet （WEB-INF/web.xml）

```jsp
<!--初始化一个前端控制器-->
<servlet>
  <servlet-name>名字与xxx-servlet.xml 对应</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
    <param-name>contextConfigLocation</param-name>	
    <param-value>classpath:spring-servlet-mvc.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>
<!-- 所有的请求都会转发给DispatchServlet-->
<servlet-mapping>
  <servlet-name>名字与xxx-servlet.xml 对应</servlet-name>
  <url-pattern>/</url-pattern>
</servlet-mapping>

<!--监听器-->
<listener>
  <listener-class>
    org.springframework.web.context.ContextLoaderListener
  </listener-class>
</listener>
<!--不想使用默认文件名 [servlet-name]-servlet.xml 和默认位置 WebContent/WEB-INF，可以添加 servlet 监听器 ContextLoaderListener 自定义该文件的名称和位置-->
<context-param>
   <param-name>contextConfigLocation</param-name>
   <param-value>/WEB-INF/spring-mvc-servlet.xml</param-value>
</context-param>

<listener>：会在整个Web应用程序启动的时候运行一次，并初始化传统意义上的Spring的容器
<context-param>：加载全局化配置文件
```

###### 3：配置视图解析器（spring-mvc-servlet.xml）

- /WEB-INF/views/hello.jsp :前缀+视图名+后缀

```XML
<!--通过Web.xml 引入-->
xmlns:mvc="http://www.springframework.org/schema/mvc"

<bean id="jspViewResolver"
      class="org.springframework.web.servlet.view.InternalResourceViewResolver">
 	<property name="prefix" value="/WEB-INF/views/"/>
	<property name="suffix" value=".jsp"/>
</bean>
```

##### 3：编写控制器

```java
@controller
@RequestMapping（"/hello")
public class HelloWorldController { 

		@RequestMapping("/helloWord")
		public String helloWorld(Model model) {
			model.addAttribute("message", "Hello World!");
			return "Hello";
		}
}
```

##### 4：Spring、Spring MVC的关系

- Spring（父）、Spring MVC（子） 是两个管理对象的容器，并且是**父子容器**
- Spring MVC用于构建WEB应用，管理web组件的 bean
- Spring 用于管理dao层、service层Bean
