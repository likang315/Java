## Spring profile：

### Spring容器中所定义的Bean的逻辑组名称，只有当这些Profile被激活的时候，才会将Profile中所对应的Bean注册到

Spring容器中，开发时可以定义不同的配置文件

##### 1：配置 profile bean

​	@Profile("数据库名")

###### 		1：在 spring3.1  中，只能在类级别上使用@profile

​			@Configuration
​			@Profile("dev")
​			public class StandaloneDataConfig 
​			{
​				@Bean
​				public DataSource dataSource() {
​				return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
​					.addScript("classpath:com/bank/config/sql/schema.sql")
​					.addScript("classpath:com/bank/config/sql/test-data.sql").build();
​				}
​			}

###### ​	2：在 spring3.2  开始允许在方法级别上应用@profile

###### 	3：xml文件中配置名字空间添加

​			xmlns:jdbc="http://www.springframework.org/schema/jdbc"
​			xmlns:jee="http://www.springframework.org/schema/jee"
​	

```xml
	<beans profile="dev">
		<jdbc:embedded-database id="dataSource">
		<jdbc:script location="classpath:com/bank/config/sql/schema.sql"/>
		<jdbc:script location="classpath:com/bank/config/sql/test-data.sql"/>
		</jdbc:embedded-database>
	</beans>

	<beans profile="production">
		<jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
	</beans>
```

##### 2：激活 Profile

Spring 在确定哪个 profile 处理激活状态时，需要依赖两个独立的属性：
	spring.profiles.active
	spring.profiles.default
若设置spring.profiles.active,那么它的值起作用,在没有spring.profiles.active属性值时,会查找 spring.profiles.default，如果
都没有设置，就不会激活 profile
	有多种方式设置以上两个属性：

###### 		1) 在集成测试类上用@ActiveProfiles（"dev"）注解

###### 		2) 作为 DispatcherServlet 的初始参数,在web.xml中的<servlet>标签中配置

```xml
	<init-param>
			<param-name>spring.profiles.default</param-name>
			<param-value>dev</param-value>
	</init-param>
```

###### 		3) 作为 web 应用上下文的参数

```xml
<context-param>
		<param-name>spring.profiles.default</param-name>
		<param-value>dev</param-value>
</context-param>
```

###### 		4) 作为 JVM 系统属性

###### 		5) 作为环境变量

###### ​		6) 作为 JNDI 条目

##### 3：条件化 Bean（@Conditional("xxx.class")）

​	Spring 4引入了一个新的@Conditional 注解，它可以用到带@Bean 注解的方法上，如果条件计算结果为 true，就会实例化Bean

设置给@Conditional 的类可以是任意实现了 Condition 接口的类型,实现 Condition 接中的matches 方法，返回 boolean 类型的结果
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {}
	其中:

```java
ConditionContext ：
	getRegistry()方法返回的 BeanDefinitionRegistry 检查 Bean 定义：
	getBeanFactory()返回 ConfigurableListableBeanFactory 检查 Bean 是否存在
	getEnvironments()返回 Environment 检查环境变量是否存在以及它的值是什么
	getResourceLoader()返回 ResourceLoader 所加载的瓷源
	getClassLoader()返回 ClassLoder 加载并检查是否存在	
```

​	AnnotatedTypeMetadata 可以让我们检查带@Bean 注解的方法上还有什么其它注解，它也是一个接口



##### 4：处理自动装配的歧义性

​    一个接口，三个实现类，当要将接口类型自动装配置时，就出现不唯一的问题，Spring会抛出NoUniqueBeanDefinitionException	

###### 	1：标志首选 Bean	

​		@Primary，加载实现类上

###### 	2：配置限定词

​		@Qualifier("限定词")

##### 5：运行时注入

在 Spring 中，处理外部值的最简单方式就是声明属性源并通过 Spring 的 Environment 来检索属性
		App.properties文件
		testbean.name=myTestBean

@Configuration
@PropertySource("classpath:/com/myco/app.properties") //这个属性文件会加载到 Environment 中

enviroment.getProperty("testbean.name");




