### Spring Annotation：基于注解的配置

 Spring 和 JSR 的 Annotaion 两套Annotation，用Spring



### 1：启用 Annotaion 配置：

##### 	< context:annotation-config />

### 2：实例化Bean

###### @Repository, @Service, @Controller, @Component 

​	四个Annotation 功能相同都是用在类上的 Annotation，说明让 spring 实例化此类，并把对象放入 spring 容器中
（@Repository 声明 Dao,@Service 声明 Service，@controller 声明控制器, @componet(“id”)其中 id 声明 bean 对像的名字)

### 3：设置扫描的基础包 ( 四种方式 )

@Configuration
@ComponentScan(“基包名”)
Public class AppConfig {}

@Configuration
@ComponentScan(basepackages="基包名")
Public class AppConfig{}		

@Configuration
@ComponentScan(basePackageClasses={App1Config.class,App2Config.class})
Public class AppConfig{}

##### <context:component-scan base-package="com.xupt"/>



### 4：自动注入

**@Autowired： **自动装配，从容器中取出此类的对象赋给它，**按照byType注入,适用于bean属性构造方法、setter 方法**

**@Resource ：**默认按照**byName自动注入策**略，如果使用type属性时则使用byType自动注入策略，如果既不指定name也不指定type属性，这是将通过反射机制使用byName自动注入

**@Qualifier**  选择装配 bean 的标识，用于多个 bean 无法确定装配哪个的情况,起别名	，**通过byName获取对象**					

```java
@Qualifier("dev")
protected ArticleDao articleDao;
```

可以用<bean >配置或者 @Component("main")
<bean id="" class="com.SimpleMovieCatalog">
	<qualifier value="main"/>
</bean>

**@Primary** 用于声明 bean 是首选的，用在多个 bean，无法选择装配谁的情况
	<bean id="" class="example.SimpleMovieCatalog" primary="true">



### 5：Bean 的初始化方法与销毁

###### 	1：@PostConstruct ，@PreDestroy

​		在方法上添加，构造之后，销毁之前

###### 	2：通过 .xml 文件

​		<bean id="s" class="com.xxx" init-method="" destroy-method="" />

###### 	3：Java显式配置初始化

​	   @Bean(initMethod = "init")
​		public Foo foo() {
​			return new Foo();
​		}
​	   @Bean(destroyMethod = "cleanup")



### 6：通过 java 代码装配 bean

@Configuration 注解表明这个类是一个配置类，该类包含了 spring 上下文环境中如何创建 bean 的细节(初始化Spring容器)
@Bean 注解会告诉 Spring 此方法返回一个对象，该对象注册为 spring 容器中



### 7：AnnotationConfigApplicationContext（加载配置类的，4种方式）

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService myService = context .getBean(MyService.class);
	
ApplicationContext context = new AnnotationConfigApplicationContext(MyServiceImpl.class,Dependency1.class);
MyService myService = context .getBean(MyService.class);

AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
context .register(AppConfig.class, OtherConfig.class);
context .register(AdditionalConfig.class);
context .refresh();
MyService myService = context .getBean(MyService.class);

AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
ctx.scan("com.acme");
ctx.refresh();
MyService myService = context.getBean(MyService.class);
```



### 8： 导入和混合配置类

在典型的 Spring 应用中，我们可能会同时使用**自动和显式配置**，在 spring 中这些配置方案都不是互斥的

###### 1：在 JavaConfig中引用 XML  配置

​		@Configuration
​		@import(AppConfig.class)
​		@importResource("classpath:ApplicationContext.xml")
​		Public class AppConfig2{}

###### 2：在XML中引用 JavaConfig

```xml
<beans>
	<import resource="ApplicationContext.xml" />
	<bean id="Myconfig“” class="com.AppConfig" />
</beans>
```








