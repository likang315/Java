### 1：核心API

##### 1>:SqlSessionFactoryBuilder.build(InputStream in)------------有重载的

​		根据xml配置信息或代码构建 SqlSessionFactory

##### 	2>:SqlSessionFactory(会话工厂)

​	 生成 SqlSession(会话)工厂,两个实现类 DefaultSqlSessionFactory 和SqlSessionManager（目前没有使用），
​	 默认使用 DefaultSqlSessionFactory，可以使用 XML和代码构建
   SqlSession openSession()------有重载的
​		1:会开启一个事务(也就是不自动提交)

​		2:连接对象会从由活动环境配置的数据源实例中得到
​    		3:事务隔离级别将会使用驱动或数据源的默认设置
​    		4:预处理语句不会被复用,也不会批量处理更新

#####  	3>:SqlSession (相当于Connection)

 		包含所有执行语句的方法并返回结果,提交或回滚事务,还有获取映射器实例
		通用的方式来执行映射语句是使用映射器类,映射器类就是简单的接口,其中的方法定义匹配于 SqlSession 方法

​	<T> T getMapper(Class<T> type)   ---------使用映射器
​	void commit()
​	void rollback()	
​	void clearCache()---------清除缓存
​	void close()   -----------关闭缓存

​	<T> T selectOne(String statemen,Object parameter)
​	<E> List<E> selectList(String statement, Object parameter)
​	int insert(String statement, Object parameter)
​	int update(String statement, Object parameter)
​	int delete(String statement, Object parameter)

##### 4>:SQL Mapper是MyBatis新设计的组件，它是由一个 java 接口和XML文件(或注解)构成的，需要给出对应的SQL和映射规则，			它负责发送 SQL,并返回结果

##### 5>:.Configuration ：将 xml 解析到 Configuration 对像中，以便复用

### 2：映射器（Mapper）

​      由java接口(定以操作数据库的方法)和Mapper.xml文件共同组成,其实就是通过 XML 和反射来动态实现的java接口的实现类
​	作用：定义参数类型
​		  描述缓存
​		  描述 SQL 语句
 		  定义查询结果和 POJO 的映射关系

```xml
<mapper namespace="com.xzy.mapper.StudentMapper">
```



###### 通知Mybatis在哪里找映射文件(Mappers)

```xml
<mappers><!--映射器-->
	<mapper resource="com/mybatis3/mappers/StudentMapper.xml"/>
	<mapper url="file:///var/mappers/StudentMapper.xml"/>
	<mapper class="com.mybatis3.mappers.TutorMapper"/>
</mappers>
```

### 3：mybatis-config.xml 配置文件

###### 1:资源(properties) 

​	<properties resource="资源文件路径">
​		<property name="name" value="${name}"/>
​	</properties>

  数据源从config.propertise 加载
	<dataSource type="POOLED">
		<property name="url" value="${jdbc.url}"/>
	</dataSource>

属性进行了多次配置时：方法参数传递的属性具有最高优先级，resource/url 属性中指定的配置文件次之,最低优先级的是
		      properties 属性中指定的属性

###### 2:设置(Setting)

​	<settings>
​		
​	</settings>

###### 3:别名(typeAliases)

​	别名不区分大小写，用来减少类完全限定名的冗余
​	1： <typeAlias alias="Author" type="com.xzy.main.Author"/>
​	2： <package name="domain.blog"/>
​		指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean,每一个在包中的Java Bean,在没有注解的情况下
​		会使用 Bean 的首字母小写的非限定类名来作为它的别名,若有注解，则别名为其注解值 @Alias("author")

```xml
<typeAliases><!--类型命名-->
	<typeAlias alias="Tutor" type="com.mybatis3.domain.Tutor"/>
	<package name="com.mybatis3.domain"/>
</typeAliases>
```

###### 4:类型处理器(typeHandler) 

​	将参数从 javaType 转化为 jdbcType 或者从数据库取出结果时把jdbcType 转化为 javaType

​      系统定义的TypeHandler 
​	类型处理器		javaType		jdbcType
​	StringTypeHandler	java.lang.String 	CHAR, VARCHAR

######   自定义TypeHandler

​	1：自定义类型处理器必须实现TypeHandler或继承BaseTypeHandler类，并且重写四个方法
​	2：配置mybatis-config.xml

```xml
<typeHandlers><!--类型处理器-->
	<typeHandler handler="com.mybatis3.typehandlers.PhoneTypeHandler"/>
	<package name="com.mybatis3.typehandlers"/>
</typeHandlers>
```

##### 5:枚举类型 TypeHandler

​	MyBatis 内部提供了两个转化枚举类型的 typeHandler

​		org.apache.ibatis.type.EnumTypeHandler
​				使用枚举字符串名称作为参数传递的
​         	org.apache.ibatis.type.EnumOrdinalTypeHandler
​				使用整数下标作为参数传递的

##### 6:对象工厂(ObjectFactory)

​	当 MyBatis 构建一个结果返回时，都会使用（ObjectFactory）去构建 POJO,需要做的仅仅是实例化目标类，要么通过默认构造方法，要么在参数映射存在的时候通过参数构造方法来实例化
默认的：org.apache.ibatis.reflection.factory.DefaultObjectFactory

##### 7:配置环境（environments）

​	Environments 可以注册多个数据源（DataSource）
​	每个数据源分为两大部分：一个是数据源的配置，另外一个数据库事务(transactionManager)的配置 
​	尽管可以配置多个环境，每个 SqlSessionFactory 实例只能选择其一,选择环境：default="id"
​	
​	transactionManager 配置数据库事务

​	JDBC，采用 JDBC 方式管理事务
​	MANAGED，采用容器方式管理事务，在 JNDI 数据源中常用

​	Property 配置数据源各属性，如 autoCommit=false

​	dataSource 标签配置数据源连接信息，type 属性是提供我们对数据库连接方式的配置，
​	UNPOOLED:非连接池数据库,使用MyBatis提供的org.apache.ibatis.datasource.unpooled.UnpooledDataSource
​	POOLED： 连接池数据库，org.apache.ibatis.datasource.pooled.PooldDataSource 实现
​	JNDI：JNDI 数据源，org.apache.ibatis.datasource.jndiDataSourceFactory 来获取数据源










