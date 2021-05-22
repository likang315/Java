### Spring Bean

------

[TOC]

##### 01：xmlns

- 表示默认的XML namespace；
- 使用语法：xmlns:namespace-prefix="namespaceURI"，其中namespace-prefix为自定义前缀，只要在这个XML文档中保证前缀不重复即可；namespaceURI 是这个前缀对应的XML namespace的定义；

###### xsi:schemaLocation

- xsi:schemaLocation：其实是 namespace 为 http://www.w3.org/2001/XMLSchema-instance 里的 schemaLocation 属性值
- 为了解决元素命名冲突的；

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
```

- xsi:schemaLocation：它定义了XML Namespace和对应的XSD（Xml Schema Definition）**文档的位置的关系**。它的值由一个或多个URI引用对组成，**两个URI之间以空白符分隔（空格和换行均可）**
  - 第一个URI：定义的XML Namespace 的值；
  - 第二个URI：指出 Schema 文档的位置，Schema 处理器将从这个位置读取Schema文档

##### 02：Bean 的定义【重要】

###### Bean 的属性

- id="对象名"
- class="实例化此类，放入 Spring容器中" 
- scope= "作用域" 
  - **singleton**：单例模式，默认的，Spring IOC容器中只会存在一个共享的bean实例，并且所有对bean的请求，只要id与该bean定义相匹配，则只会返回bean的同一实例；
  - 虽然 Spring 的 Bean 是单例的，但它内部使用**ThreadLocal** ，每一个线程对应一个变量副本，所以是**线程安全的**；
  - prototype：原型模式，每次都会产生一个新的bean实例，克隆操作；
- lazy-init="true" ：懒加载，初始化容器的时候不会立刻加载，用时再加载；
- init-method="方法名" ：初始化对象之前调用此方法；
- destory-method="方法名" ：销毁对象之前调用此方法； 
- abstract="true" ：抽象的被用来继承
- parent="id值" ：继承bean的id，与abstract联用
- **primary="true"** ：首选的，容器中有两个相同对象时，优先选择
- factory-bean="实例化工厂对象名" 
- factory-method="实例化对象的静态方法名"：使用静态方法实例化Bean

```xml
<bean id="aa" class="com.xzy.pojo.Tear" lazy-init="true" scope="prototype"
      init-method="init">
   
</bean>
```

###### Bean 作用域（scope）

```java
@Bean
@Scope(ConfigurableBeanFactory.Scope_PROTOTYPE)
Public Notepad notepad(){
     return new Notepad();
}

@Bean
@Scope(“prototype”)
Public Notepad notepad() {
    return new Notepad();
}

<bean id="" class="" scope="prototype">
```

| 作用域         | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| singleton      | 在spring IoC容器仅存在一个Bean实例，Bean以单例方式存在，**默认值，Threadlocal** |
| prototype      | 每次从容器中调用Bean时，都返回一个新的实例，即每次调用getBean()时，相当于执行newXxxBean()  ，克隆 |
| request        | 每次HTTP请求都会创建一个新的Bean，该作用域**仅适用于WebApplicationContext环境** |
| session        | 同一个HTTP Session共享一个Bean，不同Session使用不同的Bean，仅适用于WebApplicationContext环境 |
| global-session | 一般用于Portlet应用环境，该运用域仅适用于WebApplicationContext环境 |

##### 03：实例化Bean【三种】

###### 类构造器实例化

- 默认使用**无参的构造方法**实例化 Bean 对像；

```xml
<bean id="exampleBean" class="com.xupt.ExampleBean />
```

###### 静态工厂方法实例化

- 直接使用时是静态方法实例化

```xml
<bean id="对象名"  class="com.xupt.ClientService" 
      factory-method="静态方法名（createInstance)"/>

public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}
    public static ClientService createInstance() {
    	return clientService;
    }
}
```

###### 实例工厂方法实例化

```xml
<bean id="serviceLocator" class="com.xupt.DefaultServiceLocator">
</bean>
<bean id="clientService" factory-bean="serviceLocator" factory-method="实例方法名"/>
</bean>
```

##### 04：Bean 的生命周期【重要】

- Bean的定义——Bean的初始化——Bean的使用——Bean的销毁。


1. Spring 先对 bean 进行实例化，调用构造器；
2. Spring 将 值 和 bean 的引用注入到 bean 对应的属性中
3. Bean 初始化的两种方式
   1. init-method="方法名" ，通过xml配置调用自定义方法
   2. 实现 InitializingBean  接口，重写afterPropertiesSet方法
4. bean 已经准备就绪， 可以被应用程序使用了， 它们将一直驻留在应用上下文中， 直到该应用上下文被销毁
5. 上下文被销毁时调用
   1. destory-method="方法名" ，通过xml 文件配置销毁对象
   2. 实现 DisposableBean 接口，重写destory（）

###### 默认的初始化和销毁方法

​	如果你有太多具有相同名称的初始化或者销毁方法的 Bean，那么你不需要在每一个 bean 上声明初始化方法和销毁方法。框架使用 元素中的 **default-init-method** 和 **default-destroy-method** 属性提供了灵活地配置

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" 
    default-init-method="init"
    default-destroy-method="destroy">
```

##### 05：Bean 后置处理器 (BeanPostProcessor)

- 实现 **BeanPostProcessor**  接口，重写 **postProcessBeforeInitialization 和 postProcessAfterInitialization** 可以对 bean实例之前或者之后进行一些逻辑处理；
- ApplicationContext 会自动检测由 BeanPostProcessor 接口的实现类定义的 bean，注册这些 bean 为后置处理器，然后通过在容器中创建 bean，在适当的时候调用它；

```java
public class InitHelloWorld implements BeanPostProcessor {
   public Object postProcessBeforeInitialization(Object bean, String beanName) 
     throws BeansException {
       if (bean instanceof HelloWord) {
            HelloWord HelloWord hello = new HelloWord();
            HelloWord.setMessage(beanName);
            return hello;
        }
       return bean;  // you can return any other object as well
   }
  
   public Object postProcessAfterInitialization(Object bean, String beanName) 
     throws BeansException {
      System.out.println("AfterInitialization : " + beanName);
      return bean;  
   }
}
```

##### 06：Bean 定义继承 

```xml
<!--可以定义成抽象的，也可以不用定义成抽象的，实体类直接被继承-->
<bean id="beanTeamplate" abstract="true">
  <property name="message1" value="Hello World!"/>
  <property name="message2" value="Hello Second World!"/>
  <property name="message3" value="Namaste India!"/>
</bean>


<bean id="helloIndia" class="com.tutorialspoint.HelloIndia" parent="beanTeamplate">
  <property name="message1" value="Hello India!"/>
  <property name="message3" value="Namaste India!"/>
</bean>
```

##### 07：条件化 Bean

- @Conditional 使用在方法上
  - 可以用到带 @Bean 注解的方法上，如果条件计算结果为 true，就会实例化Bean；
- @Conditional 使用在类上
  - 任意实现了 Condition 接口，重写 matches 方法，返回 boolean 类型的结果；

```java
@Conditional({ProfileCondition.class})
public @interface Profile {
    String[] value();
}
// 返回true ，则实例化，反之不实例化
public class ProfileCondition implements Condition {
	@Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (context.getEnvironment() != null) {
			return true;
        }
        return false;
    }
}
```

###### ConditionContext ：

- getRegistry()：返回的 BeanDefinitionRegistry 检查 Bean 定义
- getBeanFactory()：返回 ConfigurableListableBeanFactory 检查 Bean 是否存在
- getEnvironments()：返回 Environment 检查环境变量是否存在以及它的值是什么
- getResourceLoader()：返回 ResourceLoader 所加载的资源
- getClassLoader()：返回 ClassLoder 加载并检查是否存在

###### AnnotatedTypeMetadata 

- 可以让我们检查带@Bean 注解的方法上还有什么其它注解，它也是一个接口



