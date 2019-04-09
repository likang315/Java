### DataSource：Spring 中提供了 5 种不同的数据源

spring 自带的数据源 (DriverManagerDataSource)，druid，JNDI数据，DBCP 数据源，C3P0 数据源

##### 1：DriverManagerDataSource

```xml
<bean id="dataSource"  class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="mysql.jdbc.driver.MysqlDriver"/>
		<property name="url" value=""jdbc:mysql://localhost:3306/xupt" />
		<property name="username" value="root" />
		<property name="password " value="mysql" />
</bean>
```

##### 2：druid 

Druid 连接池及监控在 spring 配置如下：

```xml
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
	<!-- 基本属性 url、user、password -->
	<property name="url" value="${jdbc_url}" />
	<property name="username" value="${jdbc_user}" />
	<property name="password" value="${jdbc_password}" />

	<!-- 配置初始化大小、最小、最大 -->
	<property name="initialSize" value="1" />	
	<property name="minIdle" value="1" />
	<property name="maxActive" value="20" />

---------------------------------------------------------------------------------------------
    <以上配置足以>
        
<!-- 配置获取连接等待超时的时间 -->
<property name="maxWait" value="60000" />

<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
<property name="timeBetweenEvictionRunsMillis" value="60000" />

<!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
<property name="minEvictableIdleTimeMillis" value="300000" />

<!-检测连接是否有效的sql,要求是一个查询语句,如果validationQuery为null,testOnBorrow,testOnReturn,testWhileIdle都不起作用-->
		<property name="validationQuery" value="SELECT 'x'" />
		<!-- 申请连接时，执行validationQuery检测连接是否有效，此配置会降低性能 -->
		<property name="testOnBorrow" value="false" />
		<!-- 归还连接时执行，validationQuery检测连接是否有效，此配置会降低性能-->
		<property name="testOnReturn" value="false" />
		<!-- 申请连接时检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效-->
		<property name="testWhileIdle" value="true" />
	

<!-- 打开 PSCache，并且指定每个连接上 PSCache 的大小 -->
<property name="poolPreparedStatements" value="true" />
<property name="maxPoolPreparedStatementPerConnectionSize" value="20" />

<!-- 配置监控统计拦截的 filters，去掉后监控界面 sql 无法统计 -->
<property name="filters" value="stat" />
        
</bean>
```




```xml
监控的配置：web.xml
<span style="white-space:pre"> </span>
<filter>
	<filter-name>DruidWebStatFilter</filter-name>
	<filter-class>com.alibaba.druid.support.http.WebStatFilter</filter-class>
	<init-param>
		<param-name>exclusions</param-name>
		<param-value>*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>DruidWebStatFilter</filter-name>
	<url-pattern>/</url-pattern>
</filter-mapping>
```

filter 可以监控 webURl 访问

```xml
<span style="white-space:pre"> </span>
<servlet>
	<servlet-name>DruidStatView</servlet-name>
	<servlet-class>com.alibaba.druid.support.http.StatViewServlet</servlet-class> 
</servlet>
<servlet-mapping>
	<servlet-name>DruidStatView</servlet-name>
	<url-pattern>/druid/*</url-pattern>
</servlet-mapping>
```

该配置可以访问监控界面，配置好后，访问 http://ip:端口号/项目名/druid/index.html 即可监控数据库访问性能


