### Spring ：其实就是一个容器，由容器来负责控制对象的生命周期和对象间的关系

- 独立于各种应用服务器，基于Spring框架的应用，可以真正实现Write Once，Run Anywhere
- Spring的IoC容器降低了业务对象替换的复杂性，提高了组件之间的**解耦**
- Spring的AOP支持允许将一些通用任务如**安全、事务、日志**等进行集中式管理，从而提供了更好的复用
- Spring的ORM和DAO提供了与第三方持久层框架的良好整合，并简化了底层的数据库访问

![](https://github.com/likang315/Java-and-Middleware/blob/master/Spring/Spring/spring-module.png?raw=true)

### 1：Spring Module

###### 	大约20个模块功能组成，核心模块为：

​		数据访问(持久层)，Web，AOP（面向切面编程），核心容器，消息传递和测试

###### 	核心容器需导入的Jar：

- spring-core
- spring-beans
- spring-context
- spring-context-support
- spring-expression

### 2：IoC（Inversion of Control）：控制反转

​		应用本身不负责依赖对象的创建及维护，依赖对象的创建及维护是由外部容器负责的，控制权就由应用转移到了外部容器，控制权的转移就是所谓控制反转

###### 原理：

​	Spring 就是通过反射来实现注入的

###### 作用：

​	因为当我们的需求出现变动时，工厂模式会需要进行相应的变化，就要就修改代码，而IoC是通过反射机制来实现的，它的对象都是动态生成的，允许我们不重新编译代码，其实就是解耦，不用频繁的创建和销毁对象，消耗资源

###### DI（Dependency Injection）：依赖注入

​	在运行期，由外部容器动态地将依赖对象注入到组件中，就是在运行时能对Bean对象修改属性值

### 3：AOP (Aspect Oriented Programming) ：面向切面编程

​	在不修改源代码的情况下给方法的进出口，动态的统一添加功能（日志，安全）的一种技术

###### 原理：

​	通过动态代理 和 cglib代理实现的

### 4：Spring 三种配置方式

######    1：用 XML 配置容器

```xml
<bean id="fe" class="com.xupt.FlashEmail"/>
<bean id="cp" class="com.xupt.CellPhone">
    <property name="name" value="lisi"/>
    <property name="email" ref="fe"/>
</bean>
```

######    2>:用Annotation 自动扫描装配 当容器扫描到 @Autowired，就会在 IOC 容器自动查找需要的 bean，并装配给该对象的属性

​	< context:annotation-config />  ：表明启用Annotation
​	<context:component-scan base-package="com.xupt" />，扫描基包下所有的类的Annotation
​	测试时，需配置
​		@RunWith(SpringJUnit4ClassRunner.class)
​		@ContextConfiguration(value={"/ApplicationContext.xml"})

######    3>:用 Java 显式配置

​	1：用一个类(MyConfig.java)来初始化容器并配置
​		@Configuration
​		@ComponentScan(basePackageClasses=MyConfig.class)
​	2：测试时配置
​		@RunWith(SpringJUnit4ClassRunner.class)
​		@ContextConfiguration(classes={MyConfig.class})



##### 4： 初始化Spring Ioc 容器：ApplicationContext.xml

Spring 自带了多种类型的应用上下文
**ClassPathXmlApplicationContext**：从**类路径下的一个或多个XML配置文件中加载上下文定义**,把应用上下文的定义文件作为类资源

**FileSystemXmlapplicationcontext：从文件系统下的一个或多个 XML 配置**文件中加载上下文定义**AnnotationConfigApplicationContext：**从一个或多个基于 **Java 的配置类中加载 Spring 应用上下文**
AnnotationConfigWebApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring Web 应用上下文
XmlWebApplicationContext： 从 Web 应用下的一个或多个XML 配置文件中加载上下文定义

```java
ApplicationContext context =
    new ClassPathXmlApplicationContext(new String[] {"services.xml","daos.xml"});
context.getBean("对象名");
//或者
@ContextConfiguration(value={"/ApplicationContext.xml"})
```

