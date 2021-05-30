### SpringBoot 启动过程

------

[TOC]

##### 01：初始化Tomcat

###### ContextConfig#processServletContainerInitializers

- Tomcat 加载容器初始化；

###### SpringBootServletInitializer#onStartup

- 在SpringBoot中，其初始化器 ServletInitializer 可以认为大体相当于SpringMvc中的Web.xml角色，都是负责Tomcat启动时触发Spring各组件的初始化；
- ServletInitializer实现了SpringBootServletInitializer的方法，而SpringBootServletInitializer实现WebApplicationInitializer。最终WebApplicationInitializer和其子类，共同替代了Web.xml的加载工作。
- 通过SPI机制被Tomcat；

##### 02：初始化ApplicationContext

###### ApplicationContext

- 初始化上下文

###### SpringBootServletInitializer#configure

- 使用外部Tomcat时需要重写

###### SpringApplication#run

- 执行run，获取上下文

##### 03：执行SpringBoot Run

###### SpringBoot过程

- 初始化并开启Spring监听器；
- 初始化Profile环境信息，获取环境配置；
- 打印Banner

###### AnnotationConfigServletWebServerApplicationContext

- 实例化上下文

##### 04：Spring IOC 初始化

- Spring IOC （资源加载器）
- Spring IOC（抽象上下文）
  - 获取ClassLoader、DefaultResourceLoader
- Spring IOC（通用上下文）
  - 初始化BeanFactory、GenericApplicationContext
- Spring IOC （servlet上下文）
  - 初始化父类上下文 ServletWebServletApplicationContext
- Spring IOC （支持注解配置的Servlet上下文）
  - AnnotatedConfigServletWebServerApplicationContext
- Spring IOC 读取器和扫描器

##### 05：SpringBoot 初始化异常分析器

- SpringBootExceptionReporter

##### 06：Spring-Boot 完善上下文信息

- BeanFactory 处理

##### 07：Spring 收尾工作

###### Spring IOC 国际化

- initMessageSource( )

###### Spring IOC 初始化服务资源

- SpringBoot 启动Web服务器

###### Spring IOC 实例化所有的单例

###### Spring IOC 刷新缓存

- resetCommonCaches( )

##### 08：SpringBoot 启动

###### Spring-Boot 发布事件

- 标记初始化上下文完成
- 容器运行中

###### 启动SpringBoot 

- SprignBootServletInitializer#onStartup





