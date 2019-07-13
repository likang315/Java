### Spring AOP（Aspect Oriented Programming）：面向切面编程

​	在修改原类的基础上，动态的添加一些逻辑代码（动态代理），应用于多处的功能被称为横切关注点

##### 1：AOP 相关术语

1. 连接点（Joinpoint）

   - 一个类或一段程序执行的某个特定的位置，这个特定的位置就称为“连接点”
   - Spring  仅支持方法的连接点，即仅能在方法调用前、方法调用后、方法抛出异常时以及方法调用前后这些程序执行点织入增强

2. 切点（Pointcut ）

   - 每个程序类都拥有多个连接点，AOP通过 "切点" 定位特定的连接点,切点和连接点不是一对一的关系，一个切点可以匹配多个连接点

3. ###### 增强（Advice）

   - 是织入到目标类连接点上的一段程序代码

4. 引介（Introduction）

   - 引介是一种特殊的增强，它为类添加一些属性和方法

5. 目标对象（Target）

   - 增强逻辑的织入目标类

6. 织入（Weaving ）

   - 将增强添加到目标类具体连接点的过程，**Spring 采用动态代理织入，而AspectJ 采用编译期织入和类装载器织入**
   - 三种织入的方式：
     1. 编译期织入，这要求使用特殊的 Java 编译器
     2. 类装载器织入，这要求使用特殊的类装载器
     3. 动态代理织入，在运行期为目标类添加增强生成子类的方式

7. 切面（Aspect ）

   - 切面由切点和增强组成类
   - Spring AOP  就是负责实施切面的框架，它将切面所定义的横切逻辑程序织入到切面所指定的连接点中

##### 2：Spring 与 AspectJ 的关系

Spring 和 AspectJ 项目之间有大量的协作，而且对 AOP 的支持在很多方面都借鉴了AspectJ 项目

需要导入的jar包：aspectjrt,aspectjweaver,cglib

##### 3：启用 AspectJ 

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
//xml	 文件配置，启用注解
<aop:aspectj-autoproxy />
```

##### 4：基于注解的AOP配置

首先配置@Aspect

- @Before：方法执行前
- @After  ：方法执行后
- @AfterReturning：方法返回后
- @AfterThrowing：方法抛出异常后

```java
<bean id="Logging" class="org.xupt.service">
</bean>

//定义切面
import org.aspectj.lang.annotation.Aspect;
@Aspect
public class Logging {
   //定义一个空方法作为切点
   @Pointcut("execution(* com.xupt.service.*(..))")
   private void selectAll(){}
  
   @Before("selectAll()")
   public void beforeAdvice(){
      System.out.println("Going to setup service profile.");
   }
  
   @After("selectAll()")
   public void afterAdvice(){
      System.out.println("service profile has been setup.");
   }
   //retVal 指切点方法返回的值 
   @AfterReturning(pointcut = "selectAll()", returning="retVal")
   public void afterReturningAdvice(Object retVal){
      System.out.println("Returning:" + retVal.toString() );
   }
   
   @AfterThrowing(pointcut = "selectAll()", throwing = "ex")
 	 public void AfterThrowingAdvice(IllegalArgumentException ex){
      System.out.println("There has been an exception: " + ex.toString());   
   }
  
   @Around("selectAll()")
	 public void AroundTask(ProceedingJoinPoint invocation){
 			  System.out.println("已经记录下操作日志@Around 方法执行前");
        invocation.proceed();
        System.out.println("已经记录下操作日志@Around 方法执行后");
   }
}
```

##### 5：基于xml文件的配置

 一个 **aspect** 是使用元素声明的，支持的 bean 是使用 **ref** 属性引用的

```xml
<aop:config>
   <aop:aspect id="myAspect" ref="Aspect">
      <aop:pointcut id="businessService"
         						expression="execution(* com.xyz.myapp.service.*.*(..))"/>
      <!-- a before advice definition -->
      <aop:before pointcut-ref="businessService" method="doRequiredTask"/>
      <!-- an after advice definition -->
      <aop:after pointcut-ref="businessService" method="doRequiredTask"/>
      <!-- an after-returning advice definition -->
      <!--The doRequiredTask method must have parameter named retVal -->
      <aop:after-returning pointcut-ref="businessService" returning="retVal"
         									 method="doRequiredTask"/>
      <!-- an after-throwing advice definition -->
      <aop:after-throwing pointcut-ref="businessService"
         									throwing="ex" method="doRequiredTask"/>
      <!-- an around advice definition -->
      <aop:around pointcut-ref="businessService" method="doRequiredTask"/>
   </aop:aspect>
</aop:config>
```

##### 6：定义切点

​	通过切点来选择连接点，使用 Execution 表达式来定义切点，表达式中可以使用  &&   ||   !

- execution()：用于匹配连接点的执行方法

###### Execution 语法：

-  *：任意
-  ..：子包

```java
execution(public * *(..)) // 任意 public 的方法，访问权限修饰符可有可无
execution(* set*(..)) //任意以 set 开始的方法
execution(* com.xyz.service.AccountService.*(..)) //AccountService 接口下的任意方法
execution(* com.xyz.service.*.*(..)) //service 包下的任意类任意方法
execution(* com.xyz.service..*.*(..)) //service 任意子包下所有类和方法
```

##### 7：处理增强中的参数

```java
@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation(int) && args(account)")
public void validateAccount(int account) {
	// 在运行此dataAccessOperation(int)时，先把他的参数传给account执行
}
```

### 把想织入的方法，设置为统一的前缀，然后扫面整个类




