## Spring Framework：是一个轻量级的解决方案，也是构建企业级应用程序的潜在一站式解决方案

支持声明式事务管理，通过RMI或Web服务远程访问您的逻辑，以及用于持久保存数据的各种选项

###### 1：LOC：控制反转（Inversion of Control）

​	应用本身不负责依赖对象的创建及维护，依赖对象的创建及维护是由外部容器负责的,控制权就由应用转移到了外部容器，控制权的转移就是所谓反转

不用频繁的创建和销毁对象，消耗资源

######    DI：依赖注入（Dependency Injection）

​	在运行期，由外部容器动态地将依赖对象注入到组件中，就是在运行时能对Bean对象设置属性值

###### AOP：面向切面编程(Aspect Oriented Programming) 通过预编译方式和运行期动态代理,实现在不修改源代码的情况下给程序动态统一添加功能的一种技术

2：Spring Module
	大约20个模块组成的功能组成,这些模块分为核心容器，数据访问/集成，Web，AOP（面向方面​​编程），仪器，消息传递和测试

   核心容器:spring-core， spring-beans，spring-context，spring-context-support，和spring-expression （弹簧表达式语言）

##### 3：Spring 三种配置方式

######    1>:用 XML 配置容器

```xml
	<bean id="fe" class="com.sp01.FlashEmail"/>
  	<bean id="cp" class="com.sp01.CellPhone">
        	<property name="name" value="XXPHONE"/>
        	<property name="email" ref="fe"/>
  	</bean>
```

######    2>:用Annotation 自动扫描装配 当容器扫描到@Autowired，就会在IoC容器自动查找需要的bean，并装配给该对象的属性


​	< context:annotation-config />  ：表明启用Annotation
​	<context:component-scan base-package="com.xupt" />，扫描基包下所有的类的Annotation
​	测试时，需配置
​		@RunWith(SpringJUnit4ClassRunner.class)
​		@ContextConfiguration(value={"/ApplicationContext.xml"})

######    3>:用Java 显式配置

​	1：用一个类(MyConfig.java)来初始化容器并配置
​		@Configuration
​		@ComponentScan(basePackageClasses=MyConfig.class)
​	2：测试时配置
​		@RunWith(SpringJUnit4ClassRunner.class)
​		@ContextConfiguration(classes={MyConfig.class})



##### 4： 初始化Spring Ioc 容器：ApplicationContext.xml

Spring 自带了多种类型的应用上下文
ClassPathXmlApplicationContext：从类路径下的一个或多个XML配置文件中加载上下文定义,把应用上下文的定义文件作为类资源

FileSystemXmlapplicationcontext：从文件系统下的一个或多个 XML 配置文件中加载上下文定义AnnotationConfigApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring 应用上下文
AnnotationConfigWebApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring Web 应用上下文
XmlWebApplicationContext： 从 Web 应用下的一个或多个XML 配置文件中加载上下文定义

```java
例：
ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"services.xml","daos.xml"});
context.getBean("对象名");
或者@ContextConfiguration(value={"/ApplicationContext.xml"})
```


