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

<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
	<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
	<property name="url" value="jdbc:mysql://localhost:3306/xupt"></property>
	<property name="username" value="root"></property>
	<property name="password" value="mysql"></property>
	<property name="initialSize" value="1"/>
	<!-- 连接池的最大值 -->
	<property name="maxActive" value="500"/>
	<!-- 最大空闲值.当经过一个高峰时间后，连接池可以慢慢将已经用不到的连接慢慢释放一部分，一
	直减少到 maxIdle 为止 -->
	<property name="maxIdle" value="2"/>
	<!-- 最小空闲值.当空闲的连接数少于阀值时，连接池就会预申请去一些连接，以免洪峰来时来不及
	申请 -->
	<property name="minIdle" value="1"/>
</bean>
    
<bean id="TransactionManager"
	class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"/>
</bean>

<!--事务AOP-->
	<!-- the transactional advice (what 'happens'; see the <aop:advisor/> bean below) -->
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

