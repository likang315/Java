### Spring DI：依赖注入

------

​	Spring框架的核心功能之一就是通过依赖注入的方式来**管理Bean之间的依赖关系**

##### 1：基于有参构造方法的注入【重要】

当容器调用带有一组参数的类构造方法时，基于构造方法的 DI 就完成了，其中每个参数代表一个对其他类的依赖

- 如果你想要向一个对象传递一个引用，你需要使用 标签的 **ref** 属性，如果你想要直接传递值，那么你应该使用如上所示的 **value** 属性

###### 对象型属性从容器中获取

​	若构造方法不止一个参数时，应按照顺序 ref 否则可能会存在歧义，如果你使用 type 属性显式的指定了构造函数参数的类型，容器也可以使用与简单类型匹配的类型

```xml
<bean id="foo" class="x.y.Foo">
  <constructor-arg ref="bar"/>
  <constructor-arg ref="baz"/>
</bean>

<bean id="bar" class="x.y.Bar"/>
<bean id="baz" class="x.y.Baz"/>

<bean id="exampleBean" class="examples.ExampleBean">
  <constructor-arg type="int" value="7500000"/>
  <constructor-arg type="java.lang.String" value="42"/>
</bean>

<!-- 最方便的使用方式 -->
<bean id="exampleBean" class="examples.ExampleBean">
  <constructor-arg index="0" value="7500000"/>
  <constructor-arg index="1" value="42"/>
</bean>
```

##### 2：基于 Setter 方法注入【重要】

​	当容器调用一个**无参的构造函数**或一个**无参的静态 factory 方法**来初始化你的 bean 后，容器会调用Setter 方法，因此基于Setter 方法的注入就完成了

```xml
<bean id="exampleBean" class="examples.ExampleBean">
  <property name="beanOne" ref="anotherExampleBean"/>
  <property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>	

<bean id="mappings" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
  <property name="properties">
    <value>
      jdbc.driver.className=com.mysql.jdbc.Driver
      jdbc.url=jdbc:mysql://localhost:3306/xupt
    </value>
  </property>
</bean>
```

##### 3：两种注入方式的区别

​	在基于构造方法注入中，我们使用的是〈bean〉标签中的〈constructor-arg〉元素，而在基于Setter 方法的注入中，我们使用的是〈bean〉标签中的〈property〉元素

##### 2：Inner beans

**inner beans** 是在其他 bean 的范围内定义的 bean，被称为内部bean。

使用内部 bean 为基于 setter 方法注入进行配置的配置文件 Beans.xml

```xml
<bean id="textEditor" class="com.tutorialspoint.TextEditor">
	<property name="spellChecker">
		<bean id="spellChecker" class="com.tutorialspoint.SpellChecker"/>
	</property>
</bean>
```

##### 3：Bean 的属性为集合类型的配置

Spring 提供了四种类型的集合的配置元素，如下所示：

| 元素    | 描述                                                        |
| ------- | ----------------------------------------------------------- |
| <list>  | 它有助于连线，如注入一列值，允许重复。                      |
| <set>   | 它有助于连线一组值，但不能重复。                            |
| <map>   | 它可以用来注入名称-值对的集合，其中名称和值可以是任何类型。 |
| <props> | 它可以用来注入名称-值对的集合，其中名称和值都是字符串类型。 |

```xml
<bean id="javaCollection" class="com.tutorialspoint.JavaCollection">
  <!-- results in a setAddressList(java.util.List) call -->
  <property name="addressList">
    <list>
      <value>INDIA</value>
      <!-- Bean 引用的注入-->
      <ref bean="address2"/>
    </list>
  </property>

  <!-- results in a setAddressSet(java.util.Set) call -->
  <property name="addressSet">
    <set>
      <value>INDIA</value>
      <ref bean="address2"/>
    </set>
  </property>

  <!-- results in a setAddressMap(java.util.Map) call -->
  <property name="addressMap">
    <map>
      <entry key="1" value="INDIA"/>
      <entry key="2" value="Pakistan"/>
      <entry key="3" value-ref="address2"/>
    </map>
  </property>

  <!-- results in a setAddressProp(java.util.Properties) call -->
  <property name="addressProp">
    <props>
      <prop key="one">INDIA</prop>
      <prop key="two">Pakistan</prop>
      <prop key="three">USA</prop>
    </props>
  </property>

</bean>
```

##### 4：注入 null 和空字符串的值

```xml
<bean id="ID" class="ExampleBean">
	<property name="email" value=""/>
</bean>

<bean id="ID" class="ExampleBean">
	<property name="email">
		<null/>
	</property>
</bean>
```

##### 5：Bean 的自动装配【重要】

​	Spring 容器可以在不使用`<constructor-arg>`和`<property>` 元素的情况下**自动装配**相互协作的 bean 之间的关系，这有助于减少编写一个大的基于 Spring 的应用程序的 XML 配置的数量

| 模式            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| **byName**      | 由属性名自动装配。Spring 容器看到在 XML 配置文件中 bean 的自动装配的属性设置为 byName时，然后尝试匹配，并且将它的属性与在配置文件中被定义为相同名称的 beans 的属性进行连接。 |
| **byType**      | 由属性数据类型自动装配。Spring 容器看到在 XML 配置文件中 bean 的自动装配的属性设置为 byType时，然后查看如果它的**类型**匹配配置文件中的一个确切的 bean 名称，它将尝试匹配和连接属性的类型。**如果存在不止一个这样的 bean，则一个致命的异常将会被抛出** |
| **constructor** | 类似于 byType，但该类型适用于构造函数参数类型。如果在容器中没有一个构造函数参数类型的 bean，则一个致命错误将会发生。 |

```xml
<!--手动装配--->
<bean id="ac_100" class="twm.demo.Account"/>
<bean id="user" class="twm.demo.User">
  <property name="username" value="Yanglan"/>
  <property name="account" ref="ac_100"/>
</bean>
<!-- 自动装配 -->
<bean id="account" class="twm.demo.Account"/>
<bean id="user" class="twm.demo.User" autowire="byName">
  <property name="username" value="Yanglan"/>
</bean>
<!--注入account属性时，并不关心bean id，而是查找容器中是否有类型为twm.demo.Account的bean-->
<bean id="ac_type" class="twm.demo.Account"/>
<bean id="user" class="twm.demo.User" autowire="byType">
  <property name="username" value="Yanglan"/>
</bean>

<bean id="yanglan" class="twm.demo.User" autowire="constructor">
</bean>
```



