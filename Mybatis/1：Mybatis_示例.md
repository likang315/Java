## Mybatis ：

通过抽像底层的JDBC代码，**自动化 SQL 结果集产生 Java 对象，Java 对象的数据持久化数据库中的过程**，使得对 SQL 的使用变的容易，也是一个半自动化的 ORM 框架，需要手工匹配提供 POJO、SQL 和映射关系

##### ORM 是通过使用描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到关系数据库中

##### 1：import jar包

mybatis-3.x.x.jar mysql-connector-java-5.1.22.jar

##### 2：New mybatis 配置文件：mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
		<environments default="development">
			<environment id="development">
				<transactionManager type="JDBC"/>
				<dataSource type="POOLED">
					<property name="driver" value="${driver}"/>
					<property name="url" value="${url}"/>
					<property name="username" value="${username}"/>
					<property name="password" value="${password}"/>
				</dataSource>
			</environment>
		</environments>
		<mappers>
			<mapper resource="com/zxy/mapper/StudentMapper.xml"/> 
		</mappers>
	</configuration>
```

##### 3：New 映射文件:Mapper.xml

首先定义Java文件(接口),定义操作数据库的方法，实现类Mybatis半自动化生成

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
 	PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<mapper namespace="com.zxy.mapper.StudentMapper">
 		<select id="findAllStudents" resultType="com.xzy.pojo.Students">
   			 select * from students
 		</select>
 	</mapper>
```

##### 4：加载mybatis的配置文件，得到SqlSessionFactory 和 sqlSession,Mapper对象

```java
	//加载mybatis的配置文件
	InputStream inputStream = Resources.getResourceAsStream("/mybatis-config.xml");
	//相当于根据mybatis-config.xml构建连接池
	SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	// SqlSession相当于我们的Connection
	SqlSession sqlSession = sqlSessionFactory.openSession();
	StudentMapper sm = sqlsession.getMapper(StudentMapper.class);
```