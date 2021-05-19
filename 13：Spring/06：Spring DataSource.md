### Spring  DataSource

------

Spring 中提供了 5 种不同的数据源

1. DriverManagerDataSource：spring 自带的数据源
2. druid  （使用druid 数据源）
3. JNDI数据
4. DBCP 数据源
5. C3P0 数据源

##### 1：DriverManagerDataSource

```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="mysql.jdbc.driver.MysqlDriver"/>
		<property name="url" value="jdbc:mysql://localhost:3306/xupt"/>
		<property name="username" value="root" />
		<property name="password" value="mysql" />
</bean>
```

##### 2：druid

druid 连接池及监控在 spring 配置如下：

```xml
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" 
      init-method="init" destroy-method="close">
	<!-- 基本属性 url、user、password -->
	<property name="url" value="${jdbc_url}" />
	<property name="username" value="${jdbc_user}" />
	<property name="password" value="${jdbc_password}" />

	<!-- 配置初始化大小、最小、最大 -->
	<property name="initialSize" value="1" />	
	<property name="minIdle" value="1" />
	<property name="maxActive" value="20" />
---------------------------------------------------------------------------------------
<!--以上配置足以-->
  <!-- 配置获取连接等待超时的时间 -->
  <property name="maxWait" value="60000" />
  <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒ms -->
  <property name="timeBetweenEvictionRunsMillis" value="60000" />
  <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
  <property name="minEvictableIdleTimeMillis" value="300000" />

<!-- 检测连接是否有效的sql,要求是一个查询语句,如果validationQuery 为null,testOnBorrow,testOnReturn,testWhileIdle都不起作用-->
		<property name="validationQuery" value="SELECT 'x'" />
		<!-- 申请连接时，执行validationQuery检测连接是否有效，此配置会降低性能 -->
		<property name="testOnBorrow" value="false" />
		<!-- 归还连接时执行，validationQuery检测连接是否有效，此配置会降低性能-->
		<property name="testOnReturn" value="false" />
		<!-- 申请连接时检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效-->
		<property name="testWhileIdle" value="true" />
	
  <!-- 启用 PSCache，并且指定每个连接上 PSCache 的大小 -->
  <property name="poolPreparedStatements" value="true" />
  <property name="maxPoolPreparedStatementPerConnectionSize" value="20" />

  <!-- 配置监控统计拦截的 filters，去掉后监控界面 sql 无法统计 -->
  <property name="filters" value="stat" />
</bean>
```

