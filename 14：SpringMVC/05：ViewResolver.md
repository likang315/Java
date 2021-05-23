### ViewResolver

------

[TOC]

##### 01：概述

- 所有的请求，经 Controller 处理之后，会返回一个 **ModelAndView 对象(逻辑视图名)**，获得 ModelAndView 对象之后，视图解析器就会选择相应的View，渲染后返回给浏览器；
- 渲染的过程中，发挥作用的就是 **ViewResolver 和 View**，当 handler 返回的 ModelAndView 中**只是逻辑视图名和对象值**，Spring MVC 根据逻辑视图匹配相应的 ViewResolver，把其解析为真正的视图 View 对象，View 进行视图渲染，把结果返回给浏览器；

##### 02：ViewResolver、View

- ViewResolver：把一个逻辑上的视图名称解析为一个真正的视图；
- View ：用于渲染视图，然后返回给客户端；

##### 03：ViewResolver API

- 视图解析器组视图渲染链，可以通过 order 属性来指定其在 ViewResolver 链中的位置，order 的值越小优先级越高；

###### AbstractCachingViewResolver

- 会把曾经解析过的视图保存起来，然后每次解析视图时先从缓存里面找，如果找到了对应的视图就直接返回，如果没有就创建一个新的视图对象，然后把它放到一个用于**缓存的 map** 中，接着再把新建的视图返回，性能最优；

###### UrlBasedViewResolver

- 继承 AbstractCachingViewResolver，主要提供了一种**拼接 URL的方式来解析视图**，通过 prefix 属性指定前缀，通过 suffix属性指定后缀，然后**把返回的逻辑视图名称加上指定的前缀和后缀就是指定的视图 URL**；
- 默认的 prefix 和 suffix 都是空串，支持返回的视图名称中包含；
  - redirect：前缀(RedirectView) ，return "redirect:/user"; 
  - forword：前缀( InternalResourceView )
- 使用 UrlBasedViewResolver 时，必须**指定属性viewClass，表示解析成哪种视图**

###### InternalResourceViewResolver

- URLBasedViewResolver 的子类，所以 URLBasedViewResolver 支持的特性它都支持，最常用；
- 把返回的视图名称都解析为 InternalResourceView 对象，**可以解析所有View，必须置于最后**，因为它可以处理所有的请求；

```xml
<!--xml 配置 ViewResolver -->
<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver">
    <property name="prefix" value="/WEB-INF/" />
    <property name="suffix" value=".jsp" />
    <!-- 其视图解析器必须指定 viewClass 属性-->
    <property name="viewClass"
              value="org.springframework.web.servlet.view.InternalResourceView"/>
    <property name="order" value="1"/>
</bean>
```

- InternalResourceView：
  - 会把 Controller 返回的模型属性都存放到对应的另一个request属性中，然后通过RequestDispatcher 在服务器端把请求 forward 重定向到目标 URL；

```xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/"/>
	<property name="suffix" value=".jsp" />
</bean>
```

###### FreeMarkerViewResolver 、VolocityViewResolver

- 两个视图解析器都继承了 UrlBasedViewResolver ，FreeMarkerViewResolver 会把 Controller 处理方法返回的**逻辑视图解析为 FreeMarkerView**，而 VolocityViewResolver 会把返回的逻辑视图解析为 VolocityView；
- FreeMarkerViewResolver：按照 UrlBasedViewResolver 拼接 URL 的方式进行视图路径的解析；
- 不需要我们指定 viewClass 属性，因为 FreeMarkerViewResolver 中已经把viewClass规定为FreeMarkerView；

```xml
<bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
  <property name="prefix" value="/WEB-INF"/>
  <property name="suffix" value=".jsp"/>
  <property name="order" value="1"/>
</bean>
```

- 对于FreeMarkerView 我们需要给定一个 FreeMarkerConfig 的 bean 对象来定义 FreeMarker 的配置信息 
- **FreeMarkerConfig**：是一个接口，Spring 已经为我们提供了一个实现类，FreeMarkerConfigurer 
  - 可以在 SpringMVC 的配置文件里面定义该 bean 对象（FreeMarkCongigurer）来定义FreeMarker 的配置信息，该配置信息将会在 FreeMarkerView 进行渲染的时候使用；
  - 最简单的配置就是配置一个 templateLoaderPath，告诉 Spring应该到哪里寻找 FreeMarker的模板文件；
    -  templateLoaderPath：支持使用“classpath:”和“file:”前缀
    - 若 FreeMarker 的模板文件存放在多个不同的路径下面的时候，可以使用templateLoaderPaths 属性来指定多个路径

``` xml
<bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
	<property name="templateLoaderPath" value="/WEB-INF/freemarker/template"/>
</bean>
```

##### 04：ViewResolver Chain

- SpringMVC 中可以同时定义多个ViewResolver，然后它们会组成一个 ViewResolver 链。当 Controller 处理器方法返回一个逻辑视图名称后，ViewResolver 链将根据其中 ViewResolver 的优先级来进行处理。
- 添加多个Bean 即可，配置多个视图解析器；

```xml
<!-- 配置freeMarker视图解析器 -->
<bean id="viewResolverFtl"
      class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
    <property name="viewClass"
              value="com.sojson.core.freemarker.extend.FreeMarkerViewExtend" />
    <!-- 把 Freemarker 扩展一下，把相关属性加入进去 -->
    <property name="contentType" value="text/html; charset=utf-8" />
    <property name="cache" value="true" />
    <property name="suffix" value=".ftl" />
    <property name="order" value="0" />
</bean>

<bean id="viewResolverCommon"
      class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".jsp" />
    <!-- 可为空，方便实现自已的依据扩展名来选择视图解释类的逻辑-->
    <property name="viewClass">
        <value>
            org.springframework.web.servlet.view.InternalResourceView
        </value>
    </property>
    <property name="order" value="1" />
</bean>
```
