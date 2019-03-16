### TransactionManager：Spring 提供了灵活方便的事务管理功能，基于底层数据库本身的事务处理机制工作

### Spring的事务管理机制实现的原理:

通过**动态代理**对所有需要事务管理的Bean进行加载，并根据配置在invoke方法中对当前调用的方法名进行判定，并在method.invoke方法前后为其加上合适的事务管理代码

##### 4：JDBC 对事务的支持

​	connection.getMetaData() ：获取 DataBaseMetaData 对象，并通过该对象的
​	supportsTransactions()、supportsTransactionIsolationLevel(int level) 方法查看底层数据库的事务支持情况
​		Connection ：默认是自动提交事务
​		Con=xxx.getConnection();
​		con.setAutoCommit(false);
​		con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//设置事务隔离级别，最高

### 5：ThreadLocal(java.lang.ThreadLocal<T>)

​	它不是一个线程，是存储线程本地对象的容器，当运行于多线程环境的某个对象并且用ThreadLocal 维护变量时，ThreadLocal为每个使用该变量的线程分配一个独立的变量副本，所以每个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本,从线程的角度看，这个变量就像线程专有的本地变量

ThreadLocal 类，只有 4 个方法

void   set(T value)
		设置当前线程的线程局部变量的值
public T get()
		返回当前线程所对应的线程局部变量
public void remove()
		将当前线程局部变量的值删除，目的是为了减少内存的占用,不过当线程结束后，对应该线程的局部变量将自动
		被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度
protected T initialValue()
		返回该线程局部变量的初始值，此方法为了让子类覆盖而设计的,这个方法是一个延迟调用方法，在线程第 1次
		调用 get()或 set(Object)时才执行，并且仅执行 1 次
	

ThreadLocal中的缺省实现直接返回一个 null,没有方法体

###### ThreadLocal 为每一个线程维护变量的副本,实现的思路很简单：

​	**在 ThreadLocal 类中有一个ThreadLocalMap，用于存储每一个线程的变量副本，Map 中元素的键为线程对象，而值对应线程的变量副本**

ThreadLocal 提供了线程安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进 ThreadLocal
private static ThreadLocal<Connection>  connThreadLocal = new ThreadLocal<Connection>();

### 6：ThreadLocal与 同步机制的比较

​	对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式，而 **ThreadLocal 采用了“以空间换时间”的方式**

​	前者仅提供一份变量，让不同的线程排队访问，而后者为每一个线程都提供了一份变量，因此可以同时访问而互不影响要做到同一事务多 DAO 共享同一 Connection，必须在一个共同的外部类使用 ThreadLocal 保存 Connection

### 7：Spring 对事物的支持

###### ​	Spring 为事务管理提供了一致的编程模板，在高层次建立了统一的事务抽象不管选择 Spring JDBC、Hibernate 、JPA 还是 iBatis，druid，Spring 都让我们可以用统一的编程模型进行事务管理

######    Spring 事务管理 SPI（Service Provider Interface）的抽象层主要包括 3 个接口，

   **PlatformTransactionManager：**事务管理器,**事务的提交,回滚等操作全部交给它来实现**
	
   **TransactionDefinition：**用于**事物的属性配置,描述事务的隔离级别、超时时间、是否为只读事务和事务传播规则**等控制事务具体行为的事务属性，这些事务属性可以通过 XML 配置或注解描述提供

   **TransactionStatus：**描述**激活事务的状态**

### 8：Spring JDBC：基于数据源的管理器

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

​	

##### 9：JPA ：事务管理器配置

```xml
<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"	
p:dataSource-ref="dataSource"/>
<bean id="transactionManger" class="org.springframework.orm.jpa.JpaTransactionManager"
	p:entityManagerFacotry-ref="entityManagerFactory"/>
```

##### 10：Hibernate3.0 ：事务管理器配置

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

##### 11：JTA：事务管理器

​	<jee:jndi-lookup id="accountDs" jndi-name="java:comp/env/jdbc/account"/>
​	<jee:jndi-lookup id="orderDs" jndi-name="java:comp/env/jdbc/account"/>

<bean id="transactionManager"
	class="org.springframework.transaction.jta.JtaTransactionManager"/>

##### 12：编程式事务管理

​	Spring 事务管理提供了模板类(org.springframework.transaction.support.TransactionTemplate),封装了对数据库的操作

#####        一般不用，用druid声明式事务

### 13：XML 配置声明式事务：Spring的事务是基于AOP实现的，因此必须先配AOP

​	通过事务的声明性信息，**Spring负责将事务管理增强逻辑，动态织入到业务方法相应连接点中,这些逻辑包括获取线程绑定资源,开始事务、提交/回滚事务、进行异常转换和处理等工作**

1:使用原始的 TransactionProxyFactoryBean 进行声明式事务配置,参考TransactionProxyFactoryBean.xm文件
2:基于 tx/aop 命名空间的配置(使用)

## 14：配置声明式事务

声明式事务实现方式主要有2种，

###### 1：通过使用Spring的< tx:advice >定义事务通知与AOP相关配置实现

###### 2：通过@Transactional实现事务管理实现

###   1：   使用@Transactional 注解声明事务，可以应用于接口定义和接口方法、类定义和类的public 方法上

​     属性：
​	① 传播行为
​	② 隔离级别
​	③ 回滚策略
​	④ 超时时间
​	⑤ 是否只读

propagation：事务的传播行为
	propagation=Propagation.NOT_SUPPORTED，容器不为这个方法开启事务
readOnly = true ：用于客户代码只读但不修改数据的情形，只读事务
	@Transactional(readOnly = true)
rollbackFor：对于增删改查时的回滚,默认情况下checked exceptions不进行回滚，仅unchecked exceptions(RuntimeException,的子类) 才进行事务回滚,需直接抛出RuntimeException及其子类
	@Transactional(rollbackFor = { RuntimeException.class })
isolation：可选的隔离性级别（默认值：ISOLATION_DEFAULT)
timeout:以秒为单位，一个事务所允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务

```xml
<!-- 是事务注解生效-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
   		<property name="dataSource" ref="dataSource"/>  
</bean>  
<!--对标注@Transaction 注解的 Bean 进行加工处理，以织入事务管理切面-->
<tx:annotation-driven transaction-manager="txManager" proxy-target-class="true" />
```

### 2：使用Spring的 < tx:advice > 定义事务通知与AOP相关配置实现

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



### Spring提供的@Transaction注解事务管理，内部同样是利用环绕通知TransactionInterceptor实现事务的开启及关闭

使用@Transactional注意点：

1:如果在接口、实现类或方法上都指定了@Transactional 注解，则优先级顺序为**方法>实现类>接口**

2：建议只在实现类或实现类的方法上使用@Transactional，而不要在接口上使用，这是因为如果使用JDK代理机制（基于接口的代理）是没问题；而使用使**用CGLIB代理（继承）机制时就会遇到问题，因为其使用基于类的代理而不是接口**，这是因为接口上的@Transactional注解是“不能继承的”





