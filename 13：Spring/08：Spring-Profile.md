### Spring Profile：上下文环境

------

##### 01：概述

- Spring 容器中所定义的 Bean 的一组逻辑名称，只有当这些Profile被激活的时候，才会将Profile中所对应的Bean注册到Spring容器中，开发时可以定义不同的配置文件；

###### 使用场景：

- 3个配置文件，一个用于开发、一个用户测试、一个用户生产，其分别对应于3个Profile。当在实际运行的时候，只需给定一个参数来激活对应的Profile即可，那么容器就会只加载激活后的配置文件；

##### ：Spring 的配置方式【SSM】

- **web.xml** 中定义加载 spring-config.xml 、spring-mvc.xml文件

```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring/spring-config.xml</param-value>
</context-param>

<servlet>
  <servlet-name>springmvc</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/spring-mvc.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
  <async-supported>true</async-supported>
</servlet>
```

##### 03：激活 Profile

- Spring 在确定哪个 profile 处理激活状态时，需要依赖AbstractEnvironment中两个独立的属性：
- spring.profiles.active
- spring.profiles.default
- 若设置spring.profiles.active，那么它的值优先起作用，在没有spring.profiles.active属性值时，会查找spring.profiles.default，如果都没有设置，就不会激活 profile；

###### 设置以上两个属性的方式：

- 在集成测试类上用@ActiveProfiles（"dev"）注解
-  作为 DispatcherServlet 的初始参数，在web.xml中的 < servlet > 标签中配置；

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

##### 04：SSM XML 配置

###### Spring-config.xml

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
<!-- 剔除controller注解，这个由mvc扫-->
<context:component-scan base-package="com.xupt.dzs.crm">
	<context:exclude-filter expression="org.springframework.stereotype.Controller" 
                            type="annotation"/>
</context:component-scan>
  
<!-- 用来加载所有的 *.properties 配置文件-->
  <context:property-placeholder location="classpath:**/*.properties" 
                                ignore-unresolvable="true" />
<!--导入其他xml配置文件-->
<import resource="spring-dao.xml"/>
  
</beans>
```

###### spring-dao.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

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
  
    <!--配置SQLSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置MyBaties全局配置文件:mybatis-config.xml -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!-- 扫描sql配置文件:mapper需要的xml文件 -->
        <property name="mapperLocations" value="classpath:mappers/**/*.xml"/>
    </bean>

    <!-- 给相应的sqlSessionFactory配置扫描对应的dao接口包，动态实现dao接口 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.xupt.dzs.crm"/>
        <property name="annotationClass" value="org.springframework.stereotype.Repository"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--factory 和 template 二选一都是用于匹配相应的dao接口的-->
        <property name="sqlSessionTemplateBeanName" value="mySqlSessionFactory"/>
    </bean>
  
    <!--配置事务管理-->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--配置jdbc模板-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
  
    <!--事务AOP-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <!-- the transactional semantics... -->
        <tx:attributes>
            <!-- all methods starting with 'get' are read-only -->
            <tx:method name="get*" read-only="true"/>
            <!-- other methods use the default transaction settings (see below) -->
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>
</beans>
```

###### spring-mvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/mvc
                           http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--开启注解扫描，只扫描Controller注解-->
    <context:component-scan base-package="com.xupt.dzs.crm" use-default-filters="false">
        <context:include-filter type="annotation" 
                                expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <!--启用MVC注解-->
    <mvc:annotation-driven/>
    <!--定义默认的视图解析器-->
    <bean id="jspViewResolver"
          class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/static/templates"/>
    </bean>
</beans>
```

