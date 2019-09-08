### Mybatis：

------

​	通过抽像底层的JDBC代码，自动化 SQL 结果集产生 Java 对象，Java 对象的数据持久化数据库中的过程，使得对 SQL 的使用变的容易，是一个半自动化的 ORM （对象关系映射）框架，需要手工匹配提供 POJO、SQL 和映射关系

- ORM：是通过使用描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到数据库中

##### Mybatis 示例

###### 1：import jar 包

- mybatis-3.x.x.jar
- mysql-connector-java-5.1.22.jar

###### 2：mybatis 配置文件：mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
 PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<typeAliases>
	  <package name="com.xzy.pojo"/>
	</typeAliases>
	
 <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis4?useSSL=true"/>
        <property name="username" value="root"/>
        <property name="password" value="mysql"/>
      </dataSource>
    </environment>

 </environments>
	 
 <mappers>
    <!-- 对像和数据表的关系 -->
    <mapper resource = "mapper/StudentMapper.xml"/>
 </mappers>
	 
</configuration>
```

###### 3：编写映射接口,其实现类有 mybatis 自动实现 

```java
public interface StudentMapper {
  // 根据ID查询
  public List<Students> findStudentsById(int id);
  // 返回Map，一个对象
  public Map<String,Object> findById(int id);
}
```

###### 4：编写Mapper ，定义操作数据库的方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 映射此 mapper 对应的接口 -->
<mapper namespace="com.zxy.mapper.StudentMapper">
 
 <resultMap type="students" id="stumap">
      <id property="studId" column="stud_id"/>
      <result property="name" column="name"/>
      <result property="dob" column="dob"/>
      <result property="email" column="email"/>
 </resultMap>
 
	<!-- 简单数据类型 -->
	<select id = "findStudentsById" parameterType = "int" resultType = "students">
    	select * from students where stud_id = #{id}
	</select>

	<!-- 返回 Map 是一个数据时，根据数据库的字段和值生成map-->
  <select id="findById" parameterType="int" resultType="map">
    select * from students where stud_id = #{id}
  </select>
 
   <!-- 返回 map 对个对象，map 的key对应字段对象名，map的value对应封装的bean-->
   <select id="findById2" parameterType="int" resultType="map">
      select * from students where stud_id > #{id}
   </select>
 
   <!-- Map用于传多个参数，map对象封装了多个key -->
   <select id="findBy03" parameterType="map" resultType="students">
      select * from students where stud_id > #{id} and name like #{name};
   </select>
</mapper>
```

###### 4：加载 mybatis 的配置文件，得到 SqlSessionFactory 和 sqlSession,Mapper对象，操作数据库

```java
public void testfindStudentsById()
  // 加载 mybatis 的配置文件
  InputStream inputStream = Resources.getResourceAsStream("/mybatis-config.xml");
	// 相当于根据 mybatis-config.xml 构建连接池
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	// SqlSession 相当于我们的 Connection
	SqlSession sqlSession = sqlSessionFactory.openSession();
  try {
    // 根据指定的 MapperInterface 返回对应的 Mapper，并且实现其接口
    StudentMapper sm = sqlsession.getMapper(StudentMapper.class);
    List<Students> stus = sm.findStudentsById(1);
    for(Students s:stus) {
     		System.out.println(s.getStud_id() +
                           "\t" + s.getName() + "\t" + s.getEmail() + "\t");
    }
    // 断言测试，两个参数一致继续执行，反之抛出异常
    assertEquals(1, stus.size());
  } finally {
    sqlsession.close();
  }
}
```