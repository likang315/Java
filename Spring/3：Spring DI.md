### 1：依赖注入，控制反转（DI）

​	Spring框架的核心功能之一就是通过依赖注入的方式来管理Bean之间的依赖关系

##### 1：基于构造方法的注入

当容器调用带有一组参数的类构造方法时，基于构造方法的 DI 就完成了，其中每个参数代表一个对其他类的依赖

- 如果你想要向一个对象传递一个引用，你需要使用 标签的 **ref** 属性，如果你想要直接传递值，那么你应该使用如上所示的 **value** 属性

###### 对象型属性从容器中获取

若构造方法不止一个参数时，应按照顺序 ref 否则可能会存在歧义，如果你使用 type 属性显式的指定了构造函数参数的类型，容器也可以使用与简单类型匹配的类型

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

##### 2：基于 Setter 方法注入

当容器调用一个无参的构造函数或一个无参的静态 factory 方法来初始化你的 bean 后，容器会调用Setter 方法，因此基于Setter 方法的注入就完成了

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

##### 3：两者的区别

​	在基于构造方法注入中，我们使用的是〈bean〉标签中的〈constructor-arg〉元素，而在基于Setter 方法的注入中，我们使用的是〈bean〉标签中的〈property〉元素



### 2：Inner beans

**inner beans** 是在其他 bean 的范围内定义的 bean，被称为内部bean

使用内部 bean 为基于 setter 方法注入进行配置的配置文件 Beans.xml 

```
<bean id="textEditor" class="com.tutorialspoint.TextEditor">
	<property name="spellChecker">
		<bean id="spellChecker" class="com.tutorialspoint.SpellChecker"/>
	</property>
</bean>	
```



### 3：Bean 的属性为集合类型的配置

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
      <entry key ="3" value-ref="address2"/>
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



### 4：注入 null 和空字符串的值

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

### 5：Bean 的自动装配









