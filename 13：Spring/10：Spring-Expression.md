### Spring EL（Spring Expression Language）

------

[TOC]

##### 01：概述

- SpEL 是类似于 OGNL 的表达式语言，能够在运行时构建复杂表达式，**存取对象属性、对象方法调用等**，所有的 SpEL 都支持XML 和 Annotation 两种方式，格式：#{SpEL expression}。

##### 02：导入Spring EL 的依赖包

- pom.xml 依赖中倒入依赖包


##### 03：XML配置

###### XML 配置

```xml
<bean id="itemBean" class="com.lei.demo.el.Item">
	<property name="name" value="itemA" />
	<property name="total" value="10" />
</bean> 
```

###### 注解配置

- 要在 Annotation 中使用 SpEL，必须要通过 annotation 注册组件；


```java
@Value("${kafka.topic.xupt}")
private String topic;
```

##### 04：SpEL 方法调用

- SpEL 允许开发者**用 El 运行方法函数，并且允许将方法返回值注入到属性中**；

```xml
@Value("#{'string'.toUpperCase()}")   	字符串直接调用函数
@Value("#{priceBean.getSpecialPrice()}") 	实例对象调用
 
<bean id="customerBean" class="com.leidemo.el.Customer">
    <property name="name" value="#{'lei'.toUpperCase()}" />
    <property name="amount" value="#{priceBean.getSpecialPrice()}" />
</bean>
```

##### 05：SpEL 操作符

- Spring EL 支持大多数的数学操作符、逻辑操作符、关系操作符；
- 关系操作符：等于 (==, eq)，不等于 (!=, ne)
  - 小于 (<, lt)：less than
  - 小于等于(<= ,le)：less than or equal
  - 大于(>, gt)：greater than
  - 大于等于 (>=, ge) ：greater than or equal
- 逻辑操作符 包括：and，or，and not(!) 
- 数学操作符 包括：加 (+)，减 (-)，乘 (*)，除 (/)，取模 (%)，幂指数 (^)

```xml
@Value("#{2 ^ 2}")
<property name="testNotEqual" value="#{1 != 1}" />
```

##### 06：Spring EL 三元表达式

- condition ? true : false
- @Value("#{itemBean.qtyOnHand < 100 ? true : false}") xml


##### 07：Spring EL 操作 List、Map

```java
// get map where key = 'MapA' 
@Value("#{testBean.map['MapA']}") 
private String mapAValue;

// get first value from list, list is 0-based
@Value("#{testBean.list[0]}") 
private String list;
```

