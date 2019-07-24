### 视图解析器

------

##### 1：MVC 一次请求流程流程

Spring MVC 控制的资源发起请求时，所有请求都会被 DispatcherServlet 处理，接着 Spring 会分析看哪一个**Handler Mapping**，定义的所有请求映射中存在对该请求的最合理的映射,然后通过该HandlerMapping 取得其对应的 **Handler(处理程序映射)**，接着再通过相应的 **HandlerAdapter 处理该 Handler**，处理之后会返回一个 **ModelAndView** 对象(逻辑视图名),获得ModelAndView 对象之后，Spring 就需要把该 View 渲染后返回给用户，即返回给浏览器

在这个渲染的过程中，发挥作用的就是 **ViewResolver 和 View**。当 Handler 返回的 ModelAndView 中不包含真正的视图，只返回一个**逻辑视图名称的时候，根据逻辑视图匹配相应的ViewResolver**，把其解析为真正的视图 View 对象,View进行视图渲染，把结果返回给浏览器

### 2：ViewResolver 和 View：Spring MVC 处理视图最重要的两个接口

ViewResolver：把一个逻辑上的视图名称解析为一个真正的视图 View ：用于处理视图，然后返回给客户端

### 3：视图解析器 ( ViewResolver )

###### AbstractCachingViewResolver ( 抽象类，支持视图缓存的 )

会**把它曾经解析过的视图保存起来**，然后每次要解析视图的时候先从缓存里面找，如果找到了对应的视图就直接返回，如果没有就创建一个新的视图对象，然后把它放到一个用于缓存的 map 中，接着再把新建的视图返回，性能最优

###### UrlBasedViewResolver

继承了AbstractCachingViewResolver，主要就是提供的一种拼接 URL的方式来解析视图，它可以通**过 prefix 属性指定一个指定的前缀，通过 suffix 属性指定一个指定的后缀，然后把返回的逻辑视图名称加上指定的前缀和后缀就是指定的视图 URL**，默认的 prefix 和 suffix 都是空串，支持返回的视图名称中包含 redirect：前缀(RedirectView) ，forword：缀( InternalResourceView )

使用 UrlBasedViewResolver 的时候必须指定属性**viewClass**，表示解析成哪种视图

一般使用较多的就是 InternalResourceView

多个视图解析器组成试图渲染连链，可以通过其 order 属性来指定在 ViewResolver 链中它所处的位置，order 的值越小优先级越高

```
xml 配置 ViewResolver
<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver">
		<property name="prefix" value="/WEB-INF/" />
		<property name="suffix" value=".jsp" />
		<property name="viewClass" value="org.springframework.web.servlet.view.InternalResourceView"/>
		<property name="order" value="1"/>
</bean>
```

###### InternalResourceViewResolver：URLBasedViewResolver 的子类，所以URLBasedViewResolver 支持的特性它都支持(最常用)

InternalResourceViewResolver(内部资源视图解析器)把返回的视图名称都解析为InternalResourceView 对象，可以解析所有View

###### InternalResourceView：

会把 Controller 处理器方法返回的**模型属性都存放到对应的request属性**中，然后通过RequestDispatcher 在服务器端把**请求 forward 重定向到目标 URL**

```
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/"/>
	<property name="suffix" value=".jsp" />
</bean>
```

### 4：FreeMarkerViewResolver 和 VolocityViewResolver

两个视图解析器都继承了 UrlBasedViewResolver ,FreeMarkerViewResolver 会把 Controller 处理方法返回的逻辑视图解析为FreeMarkerView，而 VolocityViewResolver 会把返回的逻辑视图解析为 VolocityView

###### FreeMarkerViewResolver：它会按照 UrlBasedViewResolver 拼接 URL 的方式进行视图路径的解析,但是在使用 FreeMarkerxxx时，不需要我们指定其 viewClass，因为 FreeMarkerViewResolver 中已经把viewClass规定为FreeMarkerView

```
 <bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
	<property name="prefix" value="fm_"/>
	<property name="suffix" value=".ftl"/>
	<property name="order" value="1"/>
</bean>
```

对于FreeMarkerView 我们需要给定一个 FreeMarkerConfig 的 bean 对象来定义 FreeMarker 的配置信息 FreeMarkerConfig 是一个接口，Spring 已经为我们提供了一个实现类，FreeMarkerConfigurer，我们可以通过在 SpringMVC 的配置文件里面定义该 bean 对象来定义FreeMarker 的配置信息，该配置信息将会在 FreeMarkerView 进行渲染的时候使用

###### 对于FreeMarkerConfigurer 而言，最简单的配置就是配置一个 templateLoaderPath，告诉 Spring应该到哪里寻找 FreeMarker的模板文件。这个 templateLoaderPath 也支持使用“classpath:”和“file:”前缀。

当 FreeMarker 的模板文件放在多个不同的路径下面的时候，我们可以使用templateLoaderPaths 属性来指定多个路径

```
<bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
	<property name="templateLoaderPath" value="/WEB-INF/freemarker/template"/>
</bean>
```

### 5：ViewResolver 链

在SpringMVC中可以同时定义多个ViewResolver视图解析器，然后它们会组成一个 ViewResolver 链。当 Controller 处理器方法返回一个逻辑视图名称后，ViewResolver 链将根据其中ViewResolver 的优先级来进行处理