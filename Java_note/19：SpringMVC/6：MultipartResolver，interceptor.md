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
<?xml version="1.0" encoding="UTF-8"?>
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

### 3：Filter：用来统一编码格式的

```xml
<!--配置过滤器，实例化这个类--> 
<filter>
	<filter-name>FilterDemo01</filter-name>
	<filter-class>com.xupt.filter</filter-class>
</filter>
 
<!--映射过滤器-->
<filter-mapping>
<filter-name>FilterDemo01</filter-name>
	<!--“/*”表示拦截所有的请求 -->
	<url-pattern>/*</url-pattern>
</filter-mapping>
```



### 4：定时器：@Scheduled

##### 执行原理：

1：spring在初始化bean后，通过**postProcessAfterInitialization”拦截到所有的用到“@Scheduled”注解的方法**

2：将对应类型的**定时器放入相应的“定时任务列表**”中

3：定时任务先执行corn，**判断定时任务的执行时间，计算出相应的下次执行时间，放入线程中，到相应的时间后进行执行**，之后执行**按“频率”（fixedRate）执行的定时任务，直到所有任务执行结束**

4：如果多个定时任务定义的是同一个时间，那么也是**顺序执行的，会根据程序加载Scheduled方法的先后来执行**

```xml
Spring 配置文件中namespace
<beans xmlns:task="http://www.springframework.org/schema/task"
		http://www.springframework.org/schema/task  
		http://www.springframework.org/schema/task/spring-task-3.2.xsd
/>
<task:annotation-driven/> 启动定时器

<!-- 配置线程池，否则多线程下会有延时，应为定时器时单线程的 -->
<task:annotation-driven scheduler="myScheduler"/>
<task:scheduler id="myScheduler" pool-size="5"/> 
@Scheduled(cron="0 0 1 * * ?")
```

### 4：Cron Expressions

CronTriggers 往往比 SimpleTrigger 更有用，如果您需要基于日历的概念，而非SimpleTrigger 完全指定的时间间隔，复发的发射工作的时间表

Cron 的表达式被用来配置CronTrigger实例，cron的表达式是字符串，实际上是由**七子表达式**，描述个别细节的时间表。这些子表达式是分开的空白："0 0 12 ? * WED" 在每星期三下午12:00 执行

###### Seconds (秒)           ：可以用数字0－59 表示，

###### Minutes**(**分)             ：可以用数字0－59 表示，

###### Hours**(**时)                  ：可以用数字0-23表示,

###### Day-of-Month(天) ：可以用数字1-31 中的任一一个值，但要注意一些特别的月份

###### Month**(**月)                 ：可以用0-11 或用字符串  “JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV and DEC” 表示

###### Day**-**of**-**Week(每周)：可以用数字1-7表示（1 ＝ 星期日）或用字符口串“SUN, MON, TUE, WED, THU, FRI and SAT”表示

###### .年份（1970－2099）

“/”：为特别单位，表示为“每”如“0/15”表示每隔15分钟执行一次,“0”表示为从“0”分开始, “3/20”表示表示每隔20分钟执行一次，“3”表示从第3分钟开始执行

“?”：表示每月的每一天，或第周的每一天

“*”：字符代表所有可能的值

###### 每分钟执行一次：0 1 * * * ?





