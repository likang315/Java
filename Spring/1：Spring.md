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

​	因为当我们的需求出现变动时，工厂模式会需要进行相应的变化，就要就修改代码，而IoC是通过反射机制来实现的，它的对象都是动态生成的，允许我们不重新编译代码，其实就是**解耦，并且不用频繁的创建和销毁对象**，消耗资源

###### DI（Dependency Injection）：依赖注入

​	在运行期，由外部容器动态地将依赖对象注入到组件中，就是在运行时能对Bean对象修改属性值，依赖注入是一种优秀的解耦方式，其可以让Bean以配置文件组织在一起，而不是以硬编码的方式耦合在一起。

### 3：AOP (Aspect Oriented Programming) ：面向切面编程

​	在不修改源代码的情况下给方法的进出口，动态的统一添加功能（日志，安全）的一种技术

###### 原理：

​	通过动态代理 和 cglib代理实现的

### 4：Spring 配置的三种方式

######    1：用 XML 配置容器

```xml
<!-- 实例化此类，放入到容器中-->
<bean id="fe" class="com.xupt.FlashEmail"/>
<bean id="cp" class="com.xupt.CellPhone">
    <property name="name" value="lisi"/>
    <property name="email" ref="fe"/>
</bean>
```

######    2：通过注解（Annotation）自动装配

​	 当容器扫描到 @Autowired等注入时，就会在 IOC 容器自动查找需要的 bean，并装配给该对象的属性

```xml
<!--启用Annotation,扫描类上的注解，实例化对象，放入到容器中管理-->
<context:annotation-config />  
<!--扫描基包下所有的类的Annotation-->
<context:component-scan base-package="com.xupt" />
```

######    3：用 Java 显式配置

- 用一个配置类(MyConfig.java)来初始化容器并配置
  - @Configuration：表明此类是配置类，用于初始化Spring容器
  - @ComponentScan(basePackageClasses=MyConfig.class)：定义扫描基包

```java
@Configuration
@ComponentScan(basePackageClasses=MyConfig.class)
public class MyConfig {
  @Bean
  public Student newcellphone() {
    return new Student("lisi",23);
  }
}
```



### 5： 加载 Spring 容器配置文件（ApplicationContext.xml）

  ApplicationContext是Spring容器最常用的应用上下文接口，该接口有如下两个实现类：

- ClassPathXmlApplicationContext: 从类加载路径下搜索xml配置文件，并根据配置文件来创建Spring容器
- FileSystemXmlApplicationContext: 从文件系统的相对路径或绝对路径下去搜索配置文件，并根据配置文件来创建Spring容器

```java
public static void main(String[] args) {
		ApplicationContext context =
				new ClassPathXmlApplicationContext(new String[] {"ApplicationContext.xml"});
		CellPhone cp=(CellPhone)context.getBean("cp");
		cp.run();
}
```

### 6：单元测试

###### 除了junit4和spring的jar包，还需要spring-test.jar。引入如下依赖：

```
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-test</artifactId>  
    <version>3.1.1.RELEASE</version>  
</dependency>
```

###### 单元测试注解

- @RunWith ：Junit 提供的，用来说明此测试类的运行者，这里用了 SpringJUnit4ClassRunner，这个类是一个针对 Junit 运行环境的自定义扩展，用来标准化在 Spring 环境中 Junit4.5 的测试用例
- @ContextConfiguration：是 Spring test context 提供的，用来指定 Spring 配置信息的来源，支持指定 XML 文件位置或者 Spring 配置类名
- @Transactional：是表明此测试类的事务启用，这样所有的测试方案都会自动的 rollback，即您不用自己清除自己所做的任何对数据库的变更
- @Test：指明测试方法，可以设置测试期望异常和超时时间 
  - （expected=异常名称.class)：运行过程中，抛出此异常可以不报错
  - (timeout=毫秒)：测试时间超过此世间会报异常

###### JUnit4 常用注解

@Before：初始化方法   对于每一个测试方法都要执行一次（注意与BeforeClass区别，后者是对于所有方法执行一次）
@After：释放资源  对于每一个测试方法都要执行一次（注意与AfterClass区别，后者是对于所有方法执行一次）
@Test：测试方法，在这里可以测试期望异常和超时时间 
@Test(expected=ArithmeticException.class)检查被测方法是否抛出ArithmeticException异常 
@Ignore：忽略的测试方法 
@BeforeClass：针对所有测试，只执行一次，且必须为static void 
@AfterClass：针对所有测试，只执行一次，且必须为static void 
一个JUnit4的单元测试用例执行顺序为： 
@BeforeClass -> @Before -> @Test -> @After -> @AfterClass; 		