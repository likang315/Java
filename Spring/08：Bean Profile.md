### Spring profile：上下文环境

------

​	Spring 容器中所定义的Bean的一组逻辑名称，只有当这些Profile被激活的时候，才会将Profile中所对应的Bean注册到Spring容器中，开发时可以定义不同的配置文件

###### 使用场景：

​	3个配置文件，一个用于开发、一个用户测试、一个用户生产，其分别对应于3个Profile。当在实际运行的时候，只需给定一个参数来激活对应的Profile即可，那么容器就会只加载激活后的配置文件

##### 1：配置方式

- @Profile("数据库名")
  - 在Java config 类上配置
- 基于XML文件的配置，配置不同的配置文件

```xml
<!-- 定义开发的profile，主要是配置数据库的一些配置-->
<beans profile="dev">
  <!-- 只扫描开发环境下使用的类 -->
  <context:component-scan base-package="com.spring.profile.service.dev" />
  <!-- 加载开发使用的配置文件 -->
  <util:properties id="config" location="classpath:dev/config.properties"/>
</beans>

<!-- 定义生产使用的profile -->
<beans profile="prod">
  <!-- 只扫描生产环境下使用的类 -->
  <context:component-scan base-package="com.spring.profile.service.produce" />
  <!-- 加载生产使用的配置文件-->
  <util:properties id="config" location="classpath:produce/config.properties"/>
</beans>
```

##### 2：激活 Profile

Spring 在确定哪个 profile 处理激活状态时，需要依赖AbstractEnvironment中两个独立的属性：

- spring.profiles.active
- spring.profiles.default
- 若设置spring.profiles.active,那么它的值优先起作用，在没有spring.profiles.active属性值时,会查找spring.profiles.default，如果都没有设置，就不会激活 profile

###### 设置以上两个属性的方式：

- 在集成测试类上用@ActiveProfiles（"dev"）注解
-  作为 DispatcherServlet 的初始参数，在web.xml中的 <servlet>标签中配置

```xml
<init-param>
  <param-name>spring.profiles.default</param-name>
  <param-value>dev</param-value>
</init-param>
```

- 作为 web 应用上下文的参数

```xml
<context-param>
		<param-name>spring.profiles.default</param-name>
		<param-value>dev</param-value>
</context-param>
```

##### 3：Spring-config-xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   mlns:context="http://www.springframework.org/schema/context"
	   xmlns:aop="http://www.springframework.org/schema/aop"
	   xmlns:tx="http://www.springframework.org/schema/tx"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
			http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
			http://www.springframework.org/schema/context
			http://www.springframework.org/schema/context/spring-context-3.0.xsd
			http://www.springframework.org/schema/aop
			http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
			http://www.springframework.org/schema/tx
			http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

<context:annotation-config />
<context:component-scan base-package="com.xupt" />
<aop:aspectj-autoproxy />

<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init"
      destroy-method="close">
	<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
	<property name="url" value="jdbc:mysql://localhost:3306/xupt"></property>
	<property name="username" value="root"></property>
	<property name="password" value="mysql"></property>
  <!--初始化连接数-->
	<property name="initialSize" value="1"/>
	<!-- 连接池的最大值 -->
	<property name="maxActive" value="500"/>
	<!-- 最大空闲值.当经过一个高峰时间后，连接池可以慢慢将已经用不到的连接慢慢释放一部分，一
	直减少到 maxIdle 为止 -->
	<property name="maxIdle" value="2"/>
	<!-- 最小空闲值.当空闲的连接数少于阀值时，连接池就会预申请去一些连接，以免洪峰来时来不及申请 -->
	<property name="minIdle" value="1"/>
</bean>
 
  <!-- 用来加载 jdbc.properties 配置文件-->
  <context:property-placeholder location="classpath:jdbc.properties"/>

  <bean id="TransactionManager"
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
  </bean>

	<!--事务AOP-->
	<tx:advice id="txAdvice" transaction-manager="TransactionManager">
		<!-- the transactional semantics... -->
		<tx:attributes>
		<!-- all methods starting with 'get' are read-only -->
		<tx:method name="get*" read-only="true"/>
		<!-- other methods use the default transaction settings (see below) -->
		<tx:method name="*"/>
		</tx:attributes>
	</tx:advice>
	
  <!--使用强大的切点表达式语言轻松定义目标方法-->
  <aop:config>
    <!--通过 aop 定义事务增强切面-->
    <aop:pointcut id="serviceMethod" expression="execution(com.xupt.service.*Forum.*(..))"/>
    <!--引用事务增强-->
    <aop:advisor pointcut-ref="serviceMethod" advice-ref="txAdvice"/>
  </aop:config>

  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"></property>
  </bean>

  <bean id="daoBase" abstract="true">
    <property name="jt" ref="jdbcTemplate"></property>
  </bean>

  <bean id="empDao" class="com.iss.dao.imp.EmpDaoImp" parent="daoBase"></bean>   
</beans>
```

