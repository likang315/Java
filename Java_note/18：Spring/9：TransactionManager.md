TransactionManager：Spring 提供了灵活方便的事务管理功能，基于底层数据库本身的事务处理机制工作

### Spring 的 事务管理机制实现的原理：

通过 **动态代理** 对所有需要事务管理的 Bean 进行加载，并根据配置在 invoke 方法中对当前调用的方法名进行判定，并在method.invoke方法前后为其加上合适的事务管理代码

### 1：JDBC 对事务的支持

```java
connection.getMetaData() ：获取 DataBaseMetaData 对象，并通过该对象的
supportsTransactions()、supportsTransactionIsolationLevel(int level) 方法查看底层数据库的事务支持情况
	Connection ：默认是自动提交事务
	Con=xxx.getConnection();
	con.setAutoCommit(false);
	con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//设置事务隔离级别，最高
```

### 2：Spring 对事物的支持

###### Spring 为事务管理提供了一致的编程模板，在高层次建立了统一的事务抽象不管选择 Spring JDBC、Hibernate 、JPA 还是 iBatis，druid，Spring 都让我们可以用统一的编程模型进行事务管理



### Spring 事务管理 SPI（Service Provider Interface）的抽象层主要包括 3 个接口，

**PlatformTransactionManager：**事务管理器,**事务的提交,回滚等操作全部交给它来实现**
	
**TransactionDefinition：**用于**事物的属性配置,描述事务的隔离级别、超时时间、是否为只读事务和事务传播规则**等控制事务具体行为的事务属性，这些事务属性可以通过 XML 配置或注解描述提供

Spring 在 TransactionDefinition接口中规定了7种类型的事务传播行为，它们规定了**事务方法**和**事务方法发生嵌套调用时**事务如何进行传播：

### 事务传播（propagation）行为类型 (7种)

**1：propagation_required  ：**判断当前上下文（方法）有没有事务，如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中，**默认的传播行为**

**2：propagation_required_new ：**新建事务，如果**当前存在事务，把当前事务挂起**

**3：** propagation_supports ：支持当前事务**，如果当前没有事务，就以非事务方式执行  

**4：propagation_not_supported**  ：以**非事务方式执行操作**，如果当前存在事务，就把当前事务挂起

**5：propagation_never：** 以非事务方式执行，如果当前存在事务，则抛出异常

**6：propagation_nested**  ：如果当前存在事务，则在嵌套事务内执行，如果当前没有事务，则新建一个事务

**7：propagation_mandatory：** 使用当前的事务，如果**当前没有事务，就抛出异常** 



**TransactionStatus：**描述**激活事务的状态**



### 3：Spring JDBC：基于数据源的管理器

```xml
<context:property-placeholder location="classpath:jdbc.properties"/>  用来加载jdbc.properties 配置文件

<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"destroy-method="close"
	p:driverClassName="${jdbc.driverClassName}"
	p:url="${jdbc.url}"
	p:username="${jdbc.username}"
	p:password="${jdbc.password}"/>

<bean id="transactionManager"class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
p:dataSource-ref="dataSource"/>
```

### 4：Hibernate3.0 ：事务管理器配置

```xml
<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean"
	p:dataSource-ref="dataSource"
	p:mappingResources="classpath:bbtForum.hbm.xml">	
    <property name="hibernateProperties">
        <props>
            <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
            <prop key="hibernate.show_sql">true</prop>
            <prop key="hibernate.generate_statistics">true</prop>
        </props>	
    </property>
</bean>
```

### 5：编程式事务管理

​	Spring 事务管理提供了模板类(org.springframework.transaction.support.TransactionTemplate),封装了对数据库的操作

#####        一般不用，用druid声明式事务

### 6：XML 配置声明式事务：Spring的事务是基于AOP实现的，因此必须先配AOP

​	通过事务的声明性信息，**Spring负责将事务管理增强逻辑，动态织入到业务方法相应连接点中,这些逻辑包括获取线程绑定资源,开始事务、提交/回滚事务、进行异常转换和处理等工作**

1:使用原始的 TransactionProxyFactoryBean 进行声明式事务配置,参考TransactionProxyFactoryBean.xm文件
2:基于 tx/aop 命名空间的配置(使用)

### 7：配置声明式事务

声明式事务实现方式主要有2种，

###### 1：通过使用Spring的< tx:advice >定义事务通知与AOP相关配置实现

###### 2：通过@Transactional实现事务管理实现



**1：使用@Transactional 注解声明事务**，可以应用于接口定义和接口方法、类定义和类的public 方法上

属性：
​	① 传播行为
​	② 隔离级别
​	③ 回滚策略
​	④ 超时时间
​	⑤ 是否只读

###### propagation：事务的传播行为

###### 只读事务：

readOnly = true ：用于客户代码只读但不修改数据的情形，只读事务
@Transactional(readOnly = true)

rollbackFor：对于**增删改查时的回滚**,默认情况下 checked exceptions 不进行回滚，仅uncheckedexceptions(RuntimeException,的子类) 才进行事务回滚,需直接抛出RuntimeException及其子类
	@Transactional(rollbackFor = { RuntimeException.class })

###### 隔离级别

isolation：可选的隔离性级别（默认值：ISOLATION_DEFAULT)

###### 事务超时

timeout： 以秒为单位，一个事务所**允许执行的最长时间**，如果超过该时间限制但事务还没有完成，则自动回滚事务

```xml
<!-- 是事务注解生效-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
   		<property name="dataSource" ref="dataSource"/>  
</bean>  
<!--对标注@Transaction 注解的 Bean 进行加工处理，以织入事务管理切面-->
<tx:annotation-driven transaction-manager="txManager" proxy-target-class="true" />
```



###### 2：使用Spring的 < tx:advice > 定义事务通知与AOP相关配置实现

< tx:advice >定义事务通知，用于指定事务属性，其中“transaction-manager”属性指定事务管理器，并通过				< tx:attributes >指定具体需要拦截的方法

```xml
<!--事务增强-->
<tx:advice id="txAdvice" transaction-manager="txManager">
	<tx:attributes>
		<!--事务属性定义-->
		<tx:method name="get*" read-only="false"/>
		<tx:method name="add*" rollback-for="PessimisticLockingFailureException"/>
		<tx:method name="update*"/>
	</tx:attributes>
</tx:advice>

<!-- 使用强大的切点表达式语言轻松定义目标方法 -->
<aop:config>
	<!--通过 aop 定义事务增强切面-->
	<aop:pointcut id="serviceMethod" expression="execution(*com.yyq.service.*Forum.*(..))"/>
	<!--引用事务增强-->
	<aop:advisor pointcut-ref="serviceMethod" advice-ref="txAdvice"/>
</aop:config>
```



### Spring提供的@Transaction注解事务管理，内部同样是利用环绕通知 TransactionInterceptor 实现事务的开启及关闭

使用 @Transactional 注意点：

1:如果在接口、实现类或方法上都指定了@Transactional 注解，则优先级顺序为**方法>实现类>接口**

2：建议**只在实现类或实现类的方法上使用@Transactional**，而不要在接口上使用，这是因为如果使用JDK代理机制（基于接口的代理）是没问题；而使用使**用CGLIB代理（继承）机制时就会遇到问题，因为其使用基于类的代理而不是接口**，这是因为接口上的@Transactional注解是“不能继承的”





