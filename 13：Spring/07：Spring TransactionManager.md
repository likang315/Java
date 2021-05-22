### Spring TransactionManager：

------

[TOC]

##### 01：概述

- 事务管理机制原理：基于底层数据库本身的事务处理机制工作；
- 通过**动态代理**对所有需要事务管理的 Bean 进行加载，并根据配置在 invoke 方法中对当前调用的方法名进行判定，并在调用 method.invoke ，**方法前后为其加上合适的事务管理代码**；

##### 02：局部事物和全局事物

- 局部事务：特定于一个单一的事务资源，如一个 JDBC 连接
- 全局事务：横跨多个事务资源事务，如在一个分布式系统中的事务

##### 03：Spring 事务的管理方式

- 编程式事务管理：在编程的帮助下有管理事务，具有极大的灵活性，但却很难维护；
- 声明式事务管理：从业务代码中分离出事务管理，只需要通过XML 配置来管理事务；

##### 04：Spring 对事物的支持

- Spring 为事务管理提供了**一套编程模板**，在高层次上建立了统一的事务抽象，不管选择 Spring JDBC、iBatis，druid，Spring都让我们可以用统一的编程模型进行事务管理；


##### 05：事务 API

- **PlatformTransactionManager：**事物管理器
  - TransactionStatus getTransaction(TransactionDefinition definition)：根据指定的传播行为，返回当前事物
  - void commit(TransactionStatus status) ：提交给定事务
  - void rollback(TransactionStatus status) ：执行给定事务的回滚
  
- **TransactionDefinition：**事物定义
  - int getPropagationBehavior() ：返回传播行为
  - int getIsolationLevel()：返回隔离级别
  - String getName()：返回该事务的名称
  - int getTimeout()：返回以秒为单位的超时时间时间间隔
  - boolean isReadOnly()：该事务是否是只读的
  
- **TransactionStatus：**事物状态
  - boolean hasSavepoint() ：该事务内部是否有一个保存点，就是说，基于一个保存点已经创建了**嵌套事务**
  - boolean isCompleted() ：该方法返回该事务是否完成 
  - boolean isNewTransaction()：当前事物是否是一个新的事物
  - boolean isRollbackOnly()：该方法返回该事务是否已标记为 rollback-only 
  - void setRollbackOnly() ：该方法设置该事务为 rollback-only 

##### 06：事务传播行为（propagation）【七种】

- **propagation_required：**
  - 判断当前上下文有没有事务，如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中，**默认的传播行为**；
  
- propagation_required_new：
  - 当前方法新建一个事务，如果外层方法存在事务，把外层事务挂起；
  
- propagation_nested：
  - 如果外层方法没有事物，则新建一个事物，若外层方法存在事务，则把当前方法的当成外层是事物的一部分执行；
  
- **propagation_supports：**
  - 支持当前事务，如果当前没有事务，就以非事务方式执行；
  
- **propagation_not_supported：**
  - 以**非事务方式执行**操作，如果当前存在事务，就把当前事务挂起；
  
- propagation_never：
  - 以非事务方式执行，如果当前存在事务，则抛出异常；
  
- propagation_mandatory：
- 如果外层方法没有事物，当前方法就会抛出异常，如果外层方法有事物，则使用外层方法的事务；

##### 07：基于数据源的事物管理器

```xml
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
			p:dataSource-ref="dataSource" />
```

##### 08：XML 配置事务的代理基类

- **Spring的事务是基于AOP实现的**，因此必须先配AOP；
- 使用 TransactionProxyFactoryBean 进行声明式事务配置，可以单独给某个特定的方法加单独的事务；

```xml
<!--事务管理的基类-->
<bean id="TxProxyBase" lazy-init="true"
    class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean"
    abstract="true">
  	<!-- 注入事物管理器-->
    <property name="transactionManager" ref="transactionManager" />
    <!--要在哪个Bean上面创建事务代理对象或者直接用Bean parent属性继承-->
	<property name="target" ref="newsDao" />
  
    <property name="transactionAttributes">
        <props>
            <prop key="*">PROPAGATION_REQUIRED</prop>
            <prop key="get*">PROPAGATION_REQUIRED</prop>
            <prop key="find*">PROPAGATION_REQUIRED</prop>
            <prop key="save*">PROPAGATION_REQUIRED</prop>
            <prop key="delete*">PROPAGATION_REQUIRED</prop>
        </props>
    </property>
</bean>
```

##### 09：声明式事务配置方法（两种）

1. 通过@Transactional 注解实现事务管理；
2. 通过 Spring的 < tx:advice > 定义事务通知与AOP相关配置实现；

###### 事物属性（五个）

1. propagation：传播行为
2. isolation：隔离级别（默认值：ISOLATION_DEFAULT)
3. rollbackFor：回滚策略
   - rollbackFor：默认情况下 checked exceptions 不进行回滚，unchecked exceptions 才进行事务回滚，需直接抛出RuntimeException及其子类；
   - @Transactional(rollbackFor = { RuntimeException.class })
4. timeout：事务超时
   -  以秒为单位，一个事务所允许执行的最长时间，如果超过该时间但事务还没有完成，则自动回滚；
5. readOnly：只读事务
   - @Transactional(readOnly = true)

###### 使用注解

- @Transactional 可以使用在类上，或者public 修饰的方法上
- @Transactional **只能对public 修饰的方法起事物作用**

```xml
<!-- 定义事务管理器 -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
</bean>
<!--启动事物注解 -->
<tx:annotation-driven  transaction-manager="transactionManager" />
	
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,timeout=1,
isolation=Isolation.DEFAULT)
public void insert() {}
```

###### 使用xml

- < tx:advice > 定义事务增强，用于指定事务属性，其中“transaction-manager”属性指定事务管理；
- < tx:attributes > 指定具体需要拦截的方法；

```xml
<!--定义事务增强-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <!--事务属性定义-->
        <tx:method name="get*" read-only="false"/>
        <tx:method name="add*" rollback-for="PessimisticLockingFailureException"/>
        <tx:method name="update*"/>
    </tx:attributes>
</tx:advice>

<!-- 使用强大的切点表达式语言轻松定义目标方法 -->
<aop:config>
    <!--通过aop定义事务增强切面-->
    <aop:pointcut id="serviceMethod" 
                  expression="execution(*com.yyq.service.*Forum.*(..))"/>
    <!--引用事务增强-->
    <aop:advisor pointcut-ref="serviceMethod" advice-ref="txAdvice"/>
</aop:config>
```

##### 10：@Transaction 原理

- 原理：利用AOP环绕通知 Transaction Interceptor 实现事务的开启及关闭；
- 注意：
  - 若在接口、实现类或方法上都指定了@Transactional 注解，则优先级顺序：**方法 > 实现类 > 接口**
  - 建议**只在实现类或实现类的方法上使用@Transactional**，而不要在接口上使用，因为如果使用JDK代理机制（基于接口的代理）是没问题；而使用CGLIB代理（继承）机制时就会遇到问题，因为其使用基于类的代理而不是接口，这是因为接口上的@Transactional注解是“不能继承的”。

