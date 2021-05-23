### Spring MVC

------

[TOC]

##### 01：概述

![](https://github.com/likang315/Middleware/blob/master/14%EF%BC%9ASpringMVC/photos/Spring%20MVC.png?raw=true)

- MVC 框架提供了**模型-视图-控制**的体系结构和可以用来灵活开发、松散耦合的 **web 应用程序的组件**。MVC 模式导致了应用程序的不同方面**(输入逻辑、业务逻辑和 UI 逻辑)**的分离；
- Spring（父）、Spring MVC（子） 是两个管理对象的容器，并且是**父子容器**；
- Spring MVC用于构建WEB应用，管理web组件的 bean；
- Spring 用于管理dao层、service层Bean；

##### 02：MVC Request 流程【重要】

1. **前端控制器：**请求先访问Spring的 DispatcherServlet (前端控制器)
   - Spring MVC 所有的请求都会通过一个前端控制器（front controller），是常用的 Web 应用程序模式，**一个单实例的Servlet** 将请求委托给WEB应用程序的其他组件来执行实际的处理;
   
2. **处理映射：**DispatcherServlet 将请求转发给 Spring MVC 控制器（controller）
   - 控制器是一个用于处理请求的 Spring 组件，DispatcherServlet 需要知道应该将请求发送给哪个控制器，所以 DispatcherServlet会查询一个或多个**处理器映射（handler mapping）**来确定请求的下一站在哪，处理器映射会**根据请求所携带的 URL 信息**来进行决策，Map的key；
   
3. **控制器处理：**选择合适的控制器后，DispatcherServlet 会将请求发送给选中的控制器；
   - 到了控制器，请求会卸下其负载（用户提交的信息）并等待控制器处理这些信息，将处理的结果封装成Model ，通常由POJO组成；
   
4. **模型和视图：**控制器将模型和视图名返回给前端控制器：
   - 控制器会将请求连同模型数据和用于渲染的视图名发送回 DispatcherServlet，DispatcherServlet 将根据视图名选择相应的视图解析器（ ViewResolver）；
   
5. **视图解析器**：渲染数据；
- 将它交付的模型数据，使用**视图解析器将模型数据渲染输出**，然后通过响应对象传递给客户端；

##### 03：配置示例

###### 使用Maven-archetype-webapp构建

- spring-web-4.3.7.RELEASE.jar
- spring-webmvc-4.3.7.RELEASE.jar

###### 配置 DispatcherServlet （WEB-INF/web.xml）

- SpringMVC 是强依赖 xml 文件加载的，所以如果对启动文件有个性化改动一定要改这里，另外 **web.xml** 还可以配置过滤器，自定义字符集，自定义请求拦截，鉴权等，还可以映射请求文件列表等；

```jsp
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                             http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">

    <!--在web.xml配置监听器ContextLoaderListener,
它的作用就是启动Web容器时，自动装配 ApplicationContext 的配置信息 -->
    <listener>
        <listener-class>
            org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>

    <!-- 部署applicationContext的xml文件 -->
    <!--如果在web.xml中不写任何参数配置信息，默认的路径是"/WEB-INF/applicationContext.xml，在WEB-INF目录下创建的xml文件的名称必须是applicationContext.xml。
如果是要自定义文件名可以在web.xml里的上下文初始化参数中加入contextConfigLocation这个参数：
在<param-value> </param-value>里指定相应的xml文件名，如果有多个xml文件，可以写在一起并以“,”号分隔。
也可以这样applicationContext-*.xml采用通配符。
也可以在applicationContext.xml在另外设置文件路径，如：
<import resource="classpath*:spring/spring-*.xml" />  -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/applicationContext.xml</param-value>
    </context-param>

    <!--	CharacterEncodingFilter是Spring框架提供的默认字符集过滤器，当forceEncoding为True时候，
则强制覆盖之前的编码格式，避免页面乱码。-->
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- DispatcherServlet是Spring默认的请求分发器，一般请求分发会根据请求Url的一些设置来进行不同资源请求的分发，分发到各个不同的处理器中，如静态资源分发和数据接口分发，但是静态资源分发完全可以由Tomcat代劳而不经过Spring，所以在Web.xml你经常会见到如下配置，激活Tomcat的defaultServlet处理静态文件：
将此配置写在DispatcherServlet之前，让DefaultServlet先拦截请求，不经过Spring
-->
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.css</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.jpg</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.png</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.html</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>*.xml</url-pattern>
    </servlet-mapping>


    <!-- DispatcherServlet是使用SpringMvc的重要配置，DispatcherServlet是Servlet转发器，可以配置多个DispatcherServlet来实现转发，在其配置中需要配置匹配规则，然后将拦截到的请求分发到请求目标，这里指的请求目标，实际就是Controller。
在DispatcherServlet的初始化过程中，框架会在web应用的 WEB-INF文件夹下寻找名为
[servlet-name]-servlet.xml 的配置文件，生成文件中定义的bean。也可以使用自己的配置文件名称  -->
    <servlet>
        <servlet-name>DispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--指明了配置文件的文件名，不使用默认配置文件名，而使用dispatcher-servlet.xml配置文件。-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <!--1、不写,使用默认值:/WEB-INF/<servlet-name>-servlet.xml-->
            <!--2、<param-value>/WEB-INF/classes/dispatcher-servlet.xml</param-value>-->
            <!--3、<param-value>classpath*:dispatcher-servlet.xml</param-value>-->
            <!--4、多个值用逗号分隔-->
            <param-value>classpath:spring/dispatcher-servlet.xml</param-value>
        </init-param>
        <!--是启动顺序，让这个Servlet随容器一起启动 -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <!--Servlet拦截匹配规则可以自已定义，一般根据Url正则来配置-->
        <servlet-name>DispatcherServlet</servlet-name>
        <!--会拦截所有请求。-->
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <!--首页-->
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</webapp>
```

###### 配置视图解析器（spring-mvc-servlet.xml）

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

###### 编写控制器

```java
@controller
@RequestMapping（"/hello")
public class HelloWorldController { 

    @RequestMapping("/world")
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello World!");
        return "Hello";
    }
}
```
