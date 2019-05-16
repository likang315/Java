## Spring AOP（Aspect Oriented Programming）



### 1：面向切面编程：在补修改原类的基础上，动态的统一的添加一些逻辑代码（动态代理）

应用中多处的功能被称为横切关注点（cross-cutting concern），如：安全，事务
横切关注点可以被描述为影响应用多处的功能

### 2：AOP 相关术语（ Dynamic Proxy ）

1 ）连接点（Joinpoint）
	一个类或一段程序执行的某个特定的位置，这个特定的位置就称为“连接点”
	Spring  仅支持方法的连接点，即仅能在方法调用前、方法调用后、方法抛出异常时以及方法调用前后这些程序执行点织入增强
	

###### 2 ）切点（Pointcut ）

​	每个程序类都拥有多个连接点,AOP通过“切点”定位特定的连接点,切点和连接点不是一对一的关系，一个切点可以匹
​	配多个连接点

###### 3 ）增强（Advice）

​	增强是**织入到目标类连接点上的一段程序代码**，并且还拥有一个和连接点相关的信息，这便是执行点的方位
​	结合执行点方位信息（excution(xxx)）和切点信息，我们就可以找到特定的连接点

###### 4 ）引介（Introduction）

​	引介是一种特殊的增强，它为类添加一些属性和方法

5 ）目标对象（Target）
	增强逻辑的织入目标类
	在AOP的帮助下，目标业务类只实现那些非横切逻辑的程序逻辑，而安全和事务管理等这些横切逻辑则可以使用AOP
	动态织入到特定的连接点上

6 ）织入（Weaving ）
织入：将增强添加对目标类具体连接点上的过程,**Spring 采用动态代理织入,而AspectJ 采用编译期织入和类装载器织入**
	三种织入的方式：
		a、编译期织入，这要求使用特殊的 Java 编译器
		b、类装载器织入，这要求使用特殊的类装载器
		c、动态代理织入，在运行期为目标类添加增强生成子类的方式

###### 7 ）切面（Aspect ）

​	切面由切点和增强组成类
​	Spring AOP  就是负责实施切面的框架，它将切面所定义的横切逻辑程序织入到切面所指定的连接点中



### 3：Spring 与 AspectJ 关系

Spring 和 AspectJ 项目之间有大量的协作，而且对 AOP 的支持在很多方面都借鉴了AspectJ 项目
	需要导入的jar包：aspectjrt,aspectjweaver,cglib
     

### 4：AspectJ 配置

​    Annotation：
​	@Configuration
​	**@EnableAspectJAutoProxy**
​	public class AppConfig 
​	{
​	}
​    xml
<aop:aspectj-autoproxy />

### 5：定义切面@Aspect

```java
<bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">

</bean>

package org.xyz;
import org.aspectj.lang.annotation.Aspect;
@Aspect
public class NotVeryUsefulAspect {
}
```

### 6：增强 (Advice) 的类型与声明，添加到方法上

首先配置@Aspect

###### ​		1:@Before：方法执行前

###### 		2: @After  ：方法执行后

###### ​		3:@AfterReturning：方法返回后

###### ​		4:@AfterThrowing：方法抛出异常后



```java
After returning (advice)：
1:@AfterReturning("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")  void
2:@AfterReturning( pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",returning="retVal")

After throwing  (advice)：
1:@AfterThrowing("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
2:@AfterThrowing(pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",throwing="ex")

After (finally) (advice)：
Around (advice)：执行前后（围绕）
	@Around("com.xyz.myapp.SystemArchitecture.businessService()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable
	{
		// start stopwatch
		Object retVal = pjp.proceed();
		// stop stopwatch
		return retVal;
	}
```



### 7：定义切点来选择连接点,在 Execution表达式中也可以使用  &&   ||   !

```java
execution()：用于匹配连接点的执行方法
this()：限制连接点匹配AOP，代理的 Bean 引用为指定类型的类
target()：限制连接点匹配目标对象为指定类型的类
arg()： 限制连接点匹配参数为指定类型的参数

within()：限制连接点匹配指定的类型
@args()：限制连接点匹配参数由指定注解标注的执行方法
@target：限制连接点匹配特定的执行对象，这些对象的类要具有指定类型的注解
@within()：限制匹配带有指定注解所标注的类型（当使用 Spring AOP 时，方法定义在由指定的注解所标注的类里）
@annotation：限制匹配带有指定注解的连接点
```

Execution 语法：* 任意和 ..子包
	execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern (param-pattern) throws-pattern?)
	execution(public * *(..)) // 任意 public 的方法，访问权限修饰符可有可无
 	execution(* set*(..)) //任意以 set 开始的方法
 	execution(* com.xyz.service.AccountService.*(..)) //AccountService 接口下的任意方法
	execution(* com.xyz.service.*.*(..)) //service 包下的任意类任意方法
 	execution(* com.xyz.service..*.*(..)) //service 任意子包下所有类和方法
 	this(com.xyz.service.AccountService) //代理必须是 AccountService 的实现类
 	target(com.xyz.service.AccountService)//目标对像必须是 AccountService 实现类

 	args(java.io.Serializable) //一个参数，参数为 Serializable 类型
	within(com.xyz.service.*) //service 包中任意方法
 	within(com.xyz.service..*) //service 包,子包中任意方法

 	@target(org.springframework.transaction.annotation.Transactional)//目标对像上有一个Transactional 的 Annotation
  	@within(org.springframework.transaction.annotation.Transactional) //Transactional 标注的类中的方法
 	@annotation(org.springframework.transaction.annotation.Transactional)//有 Transactional注解的方法
	@args(com.xyz.security.Classified) //方法参数上有 Classified Annotatoin



### 8：定义切点

```java
@Pointcut("within(com.xyz.web..*)")  //定义一个空方法，来定义 Pointcut
public void pointCut() {}

@Before("pointCut()")
```



### 9：处理增强中的参数

​	在运行此dataAccessOperation(int)时，先把他的参数传给account执行
​	@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation(int) && args(account)")
​	public void validateAccount(int account) {
​		// ...
​	}

### 10：AOP 的xml配置

​	参考Aop.xml
​	xmlns:aop="http://www.springframework.org/schema/aop"
​	

```java
<aop:aspect>   切面类
<aop:pointcut> 切点
	expression="execution(*com.xupt.xxx)"
	id="起名字"
<aop:advice>   需要注入的方法  
<aop:advisor>  适配器，把注入的方法注入切入点的位置，是连接pointcut跟advice的工具
```



### 把想织入的方法，设置为统一的前缀，然后扫面整个类











