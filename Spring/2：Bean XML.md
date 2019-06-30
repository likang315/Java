### xmlns：表示默认的XML Namespace的缩写

使用语法： xmlns:namespace-prefix="namespaceURI"，其中namespace-prefix为自定义前缀，只要在这个XML文档中保证前缀不重复即可；namespaceURI 是这个前缀对应的XML Namespace的定义

###### xsi:schemaLocation

- xsi:schemaLocation 其实是namespace为 http://www.w3.org/2001/XMLSchema-instance 里的schemaLocation 属性值

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
```

- xsi:schemaLocation：它定义了XML Namespace和对应的XSD（Xml Schema Definition）文档的位置的关系。它的值由一个或多个URI引用对组成，**两个URI之间以空白符分隔（空格和换行均可）**
  - 第一个URI：定义的XML Namespace 的值
  - 第二个URI：指出 Schema 文档的位置，Schema 处理器将从这个位置读取Schema文档

### 1：Bean 的定义

##### 构成 Bean 的属性（property）：

- id="对象名"
- class="实例化此类，放入 Spring容器中" 
- scope= "作用域" 
  - singleton：单例模式，默认的，Spring IOC容器中只会存在一个共享的bean实例，并且所有对bean的请求，只要id与该bean定义相匹配，则只会返回bean的同一实例
  - prototype：原型模式,每次都会产生一个新的bean实例，克隆操作
- lazy-init="true" ：懒加载，初始化容器的时候不会立刻加载，用时再加载 
- init-method="方法名" ：初始化对象之前调用此方法
- destory-method="方法名" ：销毁对象之前调用此方法
- abstract="true" ：抽象的被用来继承
- parent="id值" ：继承bean的id，与abstract联用 
- primary="true" ：首选的，有两个相同对象时，优先选择
- factory-bean="实例化工厂对象名" 
- factory-method="实例化对象的静态方法名"：使用静态方法实例化Bean

```xml
<bean id="aa" class="com.xzy.pojo.Tear" lazy-init="true" scope="prototype" init-method="init">
   
</bean>
```

### 2：Bean 作用域（scope）三种配置方式：

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
| singleton      | 在spring IoC容器仅存在一个Bean实例，Bean以单例方式存在，默认值，Threadlocal |
| prototype      | 每次从容器中调用Bean时，都返回一个新的实例，即每次调用getBean()时，相当于执行newXxxBean()  ，克隆 |
| request        | 每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境 |
| session        | 同一个HTTP Session共享一个Bean，不同Session使用不同的Bean，仅适用于WebApplicationContext环境 |
| global-session | 一般用于Portlet应用环境，该运用域仅适用于WebApplicationContext环境 |



### 3：实例化Bean的三种方式

###### 1：使用类构造器实例化，默认使用无参的构造方法实例化 Bean 对像

```xml
<bean id="exampleBean" class="com.xupt.ExampleBean />
```

###### 2：使用静态工厂方法实例化，直接使用时是静态方法实例化

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

###### 3：使用实例工厂方法实例化

```xml
<bean id="serviceLocator" class="com.xupt.DefaultServiceLocator">
</bean>
<bean id="clientService" factory-bean="serviceLocator" factory-method="实例方法名"/>
</bean>
```



### 4：Bean 的生命周期

​	Bean的定义——Bean的初始化——Bean的使用——Bean的销毁 

1. Spring 先对 bean 进行实例化，调用构造器
2. Spring 将 值 和 bean 的引用注入到 bean 对应的属性中
3. Bean 初始化的两种方式
   1. init-method="方法名" ，通过xml配置调用自定义方法
   2. 实现 InitializingBean  接口，重写afterPropertiesSet方法
4. bean 已经准备就绪， 可以被应用程序使用了， 它们将一直驻留在应用上下文中， 直到该应用上下文被销毁
5. 上下文被销毁时调用
   1. destory-method="方法名" ，通过xml 文件配置销毁对象
   2. 实现 DisposableBean 接口，重写destory（）

##### 默认的初始化和销毁方法

如果你有太多具有相同名称的初始化或者销毁方法的 Bean，那么你不需要在每一个 bean 上声明初始化方法和销毁方法。框架使用 元素中的 **default-init-method** 和 **default-destroy-method** 属性提供了灵活地配置这种情况，

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd" 
    default-init-method="init" 
    default-destroy-method="destroy">
```

### 5：Bean 后置处理器 (BeanPostProcessor)

实现 **BeanPostProcessor**  接口 重写 **postProcessBeforeInitialization 和 postProcessAfterInitialization** 可以对 bean实例之前或者之后进行一些逻辑处理

ApplicationContext 会自动检测由 BeanPostProcessor 接口的实现类定义的 bean，注册这些 bean 为后置处理器，然后通过在容器中创建 bean，在适当的时候调用它

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

### 6：Bean 定义继承 

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



### 3：依赖注入（DI）

#### 1：构造方法注入

###### 1：对象型属性从容器中获取

```
<bean id="foo" class="x.y.Foo">
            <constructor-arg ref="bar"/>
            <constructor-arg ref="baz"/>
</bean>

<bean id="bar" class="x.y.Bar"/>
<bean id="baz" class="x.y.Baz"/>
```

###### 2：属性可以通过type,index,name,来赋值

```
    <bean id="exampleBean" class="examples.ExampleBean">
        <constructor-arg type="int" value="7500000"/>
		<constructor-arg type="java.lang.String" value="42"/>
    </bean>
或
     <bean id="exampleBean" class="examples.ExampleBean">
		<constructor-arg index="0" value="7500000"/>
		<constructor-arg index="1" value="42"/>
     </bean>
或
    <bean id="exampleBean" class="examples.ExampleBean">
		<constructor-arg name="years" value="7500000"/>
		<constructor-arg name="ultimate" value="42"/>
    </bean>
```

#### 2：Setter 方法注入

```
<bean id="exampleBean" class="examples.ExampleBean">
	<property name="beanOne" ref="anotherExampleBean"/>
	<property name="beanTwo" ref="yetAnotherBean"/>
	<property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>	
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>

<bean id="mappings" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="properties">
        <value>
            jdbc.driver.className=com.mysql.jdbc.Driver
            jdbc.url=jdbc:mysql://localhost:3306/xupt
        </value>
    </property>
</bean>
```

#### 3：IDREF 配置

```
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
    <property name="targetName">
        <idref bean="theTargetBean" />
    </property>
</bean>
```

区别：

###### Ref：不带有验证功能,注入的是 bean 的实例

###### Idref：带有验证功能,注入的是string

#### 5：Inner beans

```
<bean id="outer" class="...">
	<property name="target">
		<bean class="com.example.Person">    <!-- this is the inner bean -->
			<property name="name" value="Fiona Apple"/>
			<property name="age" value="25"/>
		</bean>
	</property>
</bean>
```

#### 6：集合类型的装配

```
	<bean id="order" class="com.service.OrderServiceBean">
		<property name="lists">
			<list>
				<value>lihuoming</value>
			</list>
		</property>
		<property name="sets">
			<set>
				<value>set</value>
			</set>
		</property>
		<property name="maps">
			<map>
				<entry key="lihuoming" value="28"/>
			</map>
		</property>
		<property name="properties">
			<props>
				<prop key="12">sss</prop>
			</props>
		</property>
	</bean>
```

#### 7：空配置

```
<bean class="ExampleBean">
	<property name="email" value=""/>
</bean>

<bean class="ExampleBean">
	<property name="email">
		<null/>
	</property>
</bean>
```

### 3：延迟初始化

```
<bean id="lazy" class="com.foo.ExpensiveToCreateBean" lazy-init="true"/>
```

?

### 6：Bean的线程安全

虽然 Spring 的 Bean 是单例的但是，它内部采用ThreadLocal 这种方式，每一个线程对应一个变量副本，所以是线程安全的