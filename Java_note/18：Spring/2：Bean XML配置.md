### 1：Bean  标签的定义

##### 	Property:

1：id="对象名"
2：class="实例化类，放入Spring容器中"
**3：scope= "作用域"**
		singleton：单例模式,Spring IOC容器中只会存在一个共享的bean实例，并且所有对bean的请求，只要id与该bean定义相匹配，则只会返回bean的同一实例
		prototype：原型模式,每一次请求（将其注入到另一个bean中，或者以程序的方式调用容器的getBean()方法）都会产生一个新的bean实例，相当与一个new的操作，克隆操作

4：lazy-init="true" 懒加载，初始化容器的时候不会立刻加载，用时在加载
5：init-method="方法名"     初始化对象之前调用此方法
6：destory-method="方法名"  销毁对象之前调用此方法
7：abstract="true" 抽象的被用来继承
8：parent="id值"  继承bean，与abstract联用
9：primary="true"  首选的，有两个相同对象时，优先选择	
10：factory-bean="实例化工厂对象名"  factory-method="实例化对象的方法名"

```xml
<bean id="aa" class="com.xzy.pojo.Tear" lazy-init="true" scope="prototype" init-method="init">
   
</bean>
```

##### 2：实例化Bean

###### 	1：使用类构造器实例化，默认使用不带参数的构造方法实例化 Bean 对像

<bean id="exampleBean" class="com.xupt.ExampleBean"/>

###### 	2：使用静态工厂方法实例化，该方法必须返回一个

```XML
<bean id="对象名"class="com.xupt.ExampleBean" factory-method="静态方法名"/>
	public class ClientService {
		private static ClientService clientService = new ClientService();
		private ClientService() {}
		public static ClientService createInstance() {
			return clientService;
		}
	}
```

###### 3：.使用实例工厂方法实例化

```xml
<bean id="serviceLocator" class="com.xupt.DefaultServiceLocator">
</bean>
<bean id="clientService" factory-bean="serviceLocator" factory-method="实例方法名"/>
</bean>
```

##### 3：依赖注入（DI）

  1：构造方法注入

###### 	  1：对象型属性从容器中获取

```xml
<bean id="foo" class="x.y.Foo">
            <constructor-arg ref="bar"/>
            <constructor-arg ref="baz"/>
</bean>

<bean id="bar" class="x.y.Bar"/>
<bean id="baz" class="x.y.Baz"/>
```

######    2：属性可以通过type,index,name,来赋值

```XML
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
	<constructor-arg name="ultimateAnswer" value="42"/>
    </bean>
```



#####  2：Setter 方法注入

```xml
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



##### 3：IDREF 配置

```xml
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
	<property name="targetName">
		<idref bean="theTargetBean" />
	</property>
</bean>
```



###### 4：Ref:不带有验证功能,注入的是bean的实例 

###### 	Idref:带有验证功能,注入的是string



##### 5：Inner beans

```xml
<bean id="outer" class="...">
	<property name="target">
		<bean class="com.example.Person">    <!-- this is the inner bean -->
			<property name="name" value="Fiona Apple"/>
			<property name="age" value="25"/>
		</bean>
	</property>
</bean>
```



##### 6：集合类型的装配

```xml
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

##### 7：空配置

```xml
<bean class="ExampleBean">
	<property name="email" value=""/>
</bean>

<bean class="ExampleBean">
	<property name="email">
		<null/>
	</property>
</bean>
```



##### 8：延迟初始化

```xml
<bean id="lazy" class="com.foo.ExpensiveToCreateBean" lazy-init="true"/>
```

##### 9：自动装配（Autowiring modes）

<beans><beans />中配置自动装配类型
	

##### 10：Bean作用域（scope）的三种方式：

```java
	@Bean
	@Scope(ConfigurableBeanFactory.Scope_PROTOTYPE) 
	Public Notepad notepad()
	{
		return new Notepad();
	}
```

```java
	@Bean
 	@Scope(“prototype”)
	Public Notepad notepad()
	{
		return new Notepad();
	}
```

```xml
<bean id="" class="" scope="prototype">
```

1：singleton ：单例模式
2：prototype ：原型模式
3：request
4：session ：购物车时应用	
5：globalSession	
6：application ：上下文环境
7：websocket 

## 11：Bean 的生命周期

1：Spring 对 **bean 进行实例化**
2：**init-method="方法名"**     初始化对象之前调用此方法
3：Spring 将**值和 bean的引用注入到 bean 对应的属性**中
4：bean 已经准备就绪， 可以被应用程序使用了， 它们**将一直驻留在应用上下文中， 直到该应用上下文被销毁**
5：**destory-method="方法名"**  销毁对象之前调用此方法
	


