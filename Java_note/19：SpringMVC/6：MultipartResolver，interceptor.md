### 1：文件上传（MultipartResolver）

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"
	p:defaultEncoding="UTF-8"
	p:maxUploadSize="5000000"
	p:uploadTempDir="upload/temp"/>
```

  Spring MVC 会将上传文件绑定到 MutipartFile 对象中

```java
@RequestMapping(value = "/upload")
public String updateThumb(@RequestParam("name") String name,@RequestParam("file") MultipartFile file) throws Exception{
		if (!file.isEmpty()) 
		{
			file.transferTo(new File("d:/temp/"+file.getOriginalFilename()));
			return "redirect:success.html";
		}else
		{
			return "redirect:fail.html";
		}
  }
```

### 2：拦截器(interceptor)

HandlerInterceptor
HandlerInterceptorAdapter：自定义拦截器，需要继承此类，然后编写拦截逻辑业务
 	afterCompletion：目标完全执行后
	postHandle： 目标执行后，视图前
	preHandle：目标执行前

```xml
FormattingConversionServiceFactoryBean<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.0.xsd
		http://www.springframework.org/schema/util
		http://www.springframework.org/schema/util/spring-util-3.0.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

<context:component-scan base-package="com.xupt"/>
<!--默认自动注册DefaultAnnotationHandlerMapping与AnnotationMethodHandlerAdapter 两个bean和  FormattingConversionServiceFactoryBean -->
<mvc:annotation-driven />

<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/page/"/>
	<property name="suffix" value=".jsp"/>
</bean>

<mvc:interceptors>
<!-- 多个拦截器,顺序执行 -->
	<mvc:interceptor>
	<mvc:mapping path="/*" /><!-- 如果不配置或/*,将拦截所有的 Controller -->
		<bean class="com.xupt.interceptor.MyInterceptor"></bean>
	</mvc:interceptor>
</mvc:interceptors>
</beans>
```






