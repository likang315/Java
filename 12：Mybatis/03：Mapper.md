### Mapper 

------

[TOC]

##### 01：mybatis-config.xml

- 用于MyBatis 全局配置的

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "WEB-INF/config/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!--下划线自动转换为驼峰-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <!--配置别名-->
    <typeAliases>
        <package name="com.xupt.pojo"/>
    </typeAliases>
    <!--类型处理器-->
    <typeHandlers>
        <package name="com.qunar.hotel.datacube.wrapper.dao.typehandler" />
    </typeHandlers>
</configuration>
```

##### 02：Settings

- 一般设置**全局配置**参数，比如开启二级缓存，开启延迟加载等；


```xml
<settings>
  	<!--开启二级缓存-->
	<setting name="cacheEnabled" value="true">
  	<!-- 在进行数据库字段和类属性名映射时，下划线自动转换为驼峰-->
	<setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

##### 03：TypeAliases

- 别名不区分大小写，用来**减少类完全限定名的冗余**；
- 用于让 mapper.xml 中的参数找到对应类，如：parameterType="test"；

1. < typeAlias alias="Author" type="com.xzy.main.Author"/>
2. < package name="com.xupt.entity">
   - 指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean，每一个在包中的Java Bean，在没有注解的情况下会使用 Bean 的**首字母小写的非限定类名**来作为它的别名，若类上有注解，则别名为其注解值
   - @Alias("author")

```xml
<!--类型命名-->
<typeAliases>
	<typeAlias alias="Tutor" type="com.xupt.pojo.Tutor"/>
	<package name="com.xupt.pojo"/>
</typeAliases>
```

##### 04：TypeHandler

- 类型处理器，将参数从 javaType 转化为 jdbcType 或者从数据库取出结果时把 jdbcType 转化为 javaType；
- 例：对象转换为Json；

###### 自定义 TypeHandler

- 自定义类型处理器**必须实现 TypeHandler  或者继承BaseTypeHandler类，并且重写四个方法**；

###### 引入方式【两种】

- 使用 mybatis-config.xml 配置

  ```xml
  <!--加载类型处理器-->
  <typeHandlers>
    <package name="com.xupt.dao.typehandler" />
  </typeHandlers>
  ```

- 使用 SpringBoot 配置

  - ```properties
    mybatis:
      type-handlers-package: com.xupt.mybatis.typehandler
      mapperLocations: classpath:mybatis/mapper/**/*.xml
    ```

###### 使用方式

- 使用 TypeHandler

  - 查询，更新，使用注解的方式时，insert语句里不用配置javaType，jdbcType;

  ```xml
  <resultMap type="student" id="studentMap">
    	<result property="stu" column="stu_json"  javaType="Stu" jdbcType="text" />
  </resultMap>
  ```

###### 示例

```java
/**
 * Json转换TypeHandler
 * 使用注解的方式配置JdbcType，JavaType
 * @author kangkang.li@qunar.com
 * @date 2021-03-12 10:42
 */
@MappedJdbcTypes(value={JdbcType.LONGVARCHAR})
@MappedTypes(value = {HotelInfo.class})
public class JsonTypeHandler extends BaseTypeHandler<HotelInfo> {

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i,
                                  HotelInfo hotelInfo, JdbcType jdbcType)
    throws SQLException {
    preparedStatement.setString(i, JsonUtils.writeValueAsString(hotelInfo));
  }

  @Override
  public HotelInfo getNullableResult(ResultSet resultSet, String s) throws SQLException {
    return JsonUtils.readValue(resultSet.getString(s), HotelInfo.class)
      .orElse(new HotelInfo());
  }

  @Override
  public HotelInfo getNullableResult(ResultSet resultSet, int i) throws SQLException {
    return JsonUtils.readValue(resultSet.getString(i), QMHotelInfo.class)
      .orElse(new HotelInfo());
  }

  @Override
  public HotelInfo getNullableResult(CallableStatement callableStatement, int i)
    throws SQLException {
    return JsonUtils.readValue(callableStatement.getString(i), HotelInfo.class)
      .orElse(new HotelInfo());
  }
}
```

###### 枚举类型 TypeHandler

- MyBatis 内部提供了两个转化枚举类型的 typeHandler；
- org.apache.ibatis.type.**EnumTypeHandler**
  - 使用枚举字符串名称作为参数传递的
- org.apache.ibatis.type.**EnumOrdinalTypeHandler**
  - 使用整数下标作为参数传递的

##### 05：对象工厂(ObjectFactory)

- 当 MyBatis 构建一个结果返回时，都会**使用（ObjectFactory）去构建 POJO**，需要做的仅仅是实例化目标类，要么通过默认**无参的构造方法**，要么在参数映射存在的时候通过**有参的构造方法**来实例化；
- 默认的：org.apache.ibatis.reflection.factory.DefaultObjectFactory；

##### 06：配置多环境

- environments 可以注册多个数据源（DataSource），尽管可以**配置多个环境（数据源）**，每个 SqlSessionFactory 实例只能选择其一，选择环境：default="id"
- 可以不再Mybatis中配置，在和Spring 整合时，可以在Spring容器中配置；

###### dataSource

- 配置数据源连接信息，type 属性(数据源类型)是提供我们对数据库连接方式的配置，包括账号、密码等信息；
- 数据源的配置；
- 数据库事务(transactionManager)的配置；

###### Type 属性（三种数据源类型）

- JNDI：JNDI 数据源，org.apache.ibatis.datasource.jndiDataSourceFactory 来获取数据源；
- **POOLED：** 数据库连接池，org.apache.ibatis.datasource.pooled.PooldDataSource 实现；
- **UNPOOLED：**非连接池数据库，每次连接数据库的时候都会创建一个新的连接，使用MyBatis提供的org.apache.ibatis.datasource.unpooled.UnpooledDataSource；

###### TransactionManager

- JDBC：采用 **JDBC 方式管理事务**，使用 JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务；
- MANAGED：采用**容器方式管理事务**， 默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将 closeConnection 属性设置为 false 来阻止它默认的关闭行为；

```xml
<transactionManager type="MANAGED">
    <property name="closeConnection" value="false"/>
</transactionManager>

<!-- 分模块部署时，dao层需要数据源-->
<environments default="dev">
    <environment id="dev">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/mybatis4?useSSL=true"/>
            <property name="username" value="root"/>
            <property name="password" value="mysql"/>
        </dataSource>
    </environment>
</environments>
```

##### 07：注解配置Mapper接口

- MyBatis 支持**使用注解来配置映射语句**，当我们使用基于注解的映射器接口时，则不再需要mappe文件；
- @Insert
- @Update
- @Delete
- @Select 

```java
@Insert({"INSERT INTO user (id, username, passwd) VALUES (#{id}, #{username},#{passwd})"})int insertUser(User user);
```

##### 08：全局标签

- **cache**
  - 用于声明这个 namespace 使用二级缓存，并且可以自定义配置；
- **cache-ref**
  - 引用别的命名空间的Cache配置，两个命名空间的操作使用的是同一个Cache；
- sql
  - 复用SQL语句
- resultMap
  - 用于定义实例对象和数据库表的字段的关系；
- select
  -  映射查询语句
- insert
  - 映射插入语句
- update
  - 映射更新语句
- delete
  - 映射删除语句

##### 09：select

###### 配置映射

- parameterType：定义参数类型，值为这条语句的参数类的完全限定名或别名；

- resultMap（resultType）：定义返回值类型，不可同时使用，实际上内部是创建一个ResultMap 的；

  1. resultType = 基本数据类型

  2. resultType = List | Set 时

     ```java
     List<Hotel> getHotel(Integer i);
     // 只需定义集合元素的类型即可
     <select id = "getHotel" resultType = "com.xupt.Hotel">
       SELECT * FROM WHERE id > #{i}
     </select>
     ```

  3. ###### 用 Map 传递多个参数或返回Map

     1. map 传递多个参数时，parameterType="java.util.Map"
       
        - key为参数，value是参数值，通过**#{变量.key}**获取值
        
     2. map 作为返回值时
        - 返回**一个对象**时，根据数据库的字段和值生成map
          - resultType = "java.util.Map"
        - 返回**多个对象时，有两种情况**
          - 通过**@MapKey("fieldName")**  指定pojo对象中一个属性名作为key；
            - value：是个Map<字段名，字段值>
            - 用在 mapper 接口定义的方法上；
          - 返回结果用List<Map<String, String>> 接收，一个Bean为：字段名-字段值；
        
        ```java
        @MapKey("id")
        List<Map<String, Map<String, String>>> queryUserInfo();
        ```
  
- resultMap ：用于把复杂的pojo进行结果映射，一对多、一对一时；

- ```xml
  <!--类类型的参数对象，id、username 和 password 属性将会被查找，将它们的值传入预处理语句的参数中-->
  <insert id="insertUser" parameterType="user">
    insert into user (id, username, password) values (#{id}, #{username}, #{password})
  </insert>
  <!--自动把列名作为key，value作为值返回-->
  <select id="selectUser" parameterType="int" resultType="map">
    SELECT * FROM user WHERE ID = #{id}
  </select>
  <!--数据库列名和类属性不一致-->
  <select id="selectUsers" parameterType="int" resultType="person">
    select user_id as "id",user_name as "userName", hashed_password as "hashedPassword"
    from user where id = #{id}
  </select>
  ```

###### 自动映射

- 如果你的数据库是规范命名的，即数据库每一个单词都用下划线分隔，POJO 采用驼峰式命名方法，可以**设置 mapUnderscoreToCamelCase 为 true，MyBatis 就会自动回填这些字段而无需任何配置，或者也可以实现从数据库到 POJO 的自动映射**。

- 自动映射可以在config.xml 中 settings 元素中配置 autoMappingBehavior 属性值来设置其策略
  - NONE：取消自动映射；
  - **PARTIAL：部分的，只会自动映射没有定义嵌套结果集映射的结果集**；
  - FULL：会自动映射任意复杂的结果集（无论是否嵌套）；
  
- ```xml
  <settings>
    <!--设置自动映射-->
  	<setting name="autoMappingBehavior" value="PARTIAL"/>	
    <!--驼峰命名自动映射-->
    <setting name="mapUnderscoreToCamelCase" value="true" />
  </settings>
  ```

##### 10：更新操作

- id：命名空间中的唯一标识符，用于确定使用接口的哪个方法；
- **useGeneratedKeys：主键回填**，插入数据成功后将会返回数据库自动生成的主键ID；
  - 默认值：false，仅对 insert 和 update 有用；
- parameterType：传入语句的参数的完全限定类名或别名
- **keyProperty**：标记哪个属性是主键，默认：未设置（unset）；
- **flushCache：缓存清空**，任何时候只要该语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true
- timeout：抛出异常之前，驱动程序等待数据库返回请求结果的秒数，默认值： 未设置（unset）
- **statementType**：发送sql语句的方式，STATEMENT，PREPARED 或 CALLABLE，**默认值：PREPARED**；
- keyColumn：通过生成的键值设置表中的列名，这个设置在某些数据库是必须的，当主键列不是表中的第一列的时候需要设置；

###### 主键回填

- 
  MySQL 中主键自增字段，在插入后我们入往往需要获得这个主键，把获得的主键值给 JavaBean 的 ID；


```xml
<!--需要设置主键回填，指定接收主键的属性，返回生成主键-->
<insert id="insertUser" parameterType="user" useGeneratedKeys="true" keyProperty="id">
	insert into user(username,note) values(#{username},#{note})
</insert>

<!-- 使用mysql 自带的函数查询-->
<insert id="insertBook">
    <selectKey keyProperty="id" resultType="java.lang.Integer">
        SELECT LAST_INSERT_ID()
    </selectKey>
    insert into t_book (b_name,author) values (#{name},#{author});
</insert>
```

###### 字符串替换【参数占位】

- 使用 ${} 会自动加上 ' ' ，用于字符串，会出现Sql注入问题；
- 使用 #{} 不会，替换值；

###### SQL 复用

```xml
<!--复用的SQL语句-->
<sql id="userColumns"> ${alias}.id,${alias}.username </sql>

<select id="selectUsers" resultType="map">
  SELECT
    <include refid="userColumns"><property name="alias" value="t1"/></include>,
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  FROM some_table t1
    INNER JOIN some_table t2
</select>
```

##### 11：@Param 注解

- 用于**参数命名**，参数命名后就能根据名字得到参数值，正确的将参数传入sql语句中；
- 使用@Param后，使用**#{}**，${} 都可以，否则只能使用#{} ;
- 若不使用@Param时，参数只能有一个，并且为JavaBean；
- 修改参数名，匹配SQL字段；

```xml
Student queryStudentByName(@Param("name") String name)

<select id="getStudent" resultType="com.xupt.student">
    SELECT * FROM student
    WHERE name = #{name}
    LIMIT 1
</select>
```

##### 12：ResultMap【结果映射】

- 定义一个复杂结果集的映射关系，包含子对象，也可以用于驼峰式和下划线字段互转；
  - type：表示返回结果的类型（pojo类）；
  - javaType：用于说明Java类型，jdbcType：用于说明数据库字段类型；

```xml
<resultMap type="stu" id="stuscore">
    <id property="id" column="u_id"/>
    <result property="clazzId" column="clazz_id"/>
    <result property="name" column="name"/>
    <result property="sex" column="sex"/>
    <result property="age" column="age"/>
	<!--子对象，score可以存储学生id分离彻底，不用强耦合-->
    <result property="score.math" column="math"/>
    <result property="score.english" column="english"/>
</resultMap>
```

##### 13：一对一映射(one to one)

- 一个学生对应一个成绩


###### 内嵌对象

- stu 中有一个属性为Score的子对象，使用**圆点记法**为内嵌的对象的属性赋值；


```xml
<result property="score.math" column="math"/>
```

###### 继承嵌套

```xml
<!-- 继承嵌套 -->
<resultMap type="stu" id="stus">
  <id property="id" column="id"/>
  <result property="clazzId" column="clazz_id"/>
  <result property="name" column="name"/>
  <result property="sex" column="sex"/>
  <result property="age" column="age"/>
</resultMap>

<resultMap type="stu" id="scoreResultMap" extends="stus">
  <result property="score.math" column="math"/>
  <result property="score.english" column="english"/>
</resultMap>
```

###### association 引入

- 被用来导入“有一个”(has-a)类型的关联，子对象；
- 处理两张表之间的关系，关联查询【禁用】；

```xml
<resultMap type="stu" id="scoreResultMap">
  <result property="score.math" column="math"/>
  <result property="score.english" column="english"/>
</resultMap>

<resultMap type="stu" id="stus">
  <id property="id" column="id"/>
  <result property="clazzId" column="clazz_id"/>
  <result property="name" column="name"/>
  <result property="sex" column="sex"/>
  <result property="age" column="age"/>
  <association property="score" resultMap="scoreResultMap"/>
</resultMap>
```

###### association 内联

```xml
<resultMap type="stu" id="stusMap">
     <id property="id" column="stu_id"/>
     <result property="clazzId" column="clazz_id"/>
     <result property="name" column="name"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     <!--property="Type值的属性"，column="表中的列"，对应stu的score-->
     <association property="score" javaType="Score" column="stu_id">
	      <id property="id" column="score_stu_id"/>
	      <result property="math" column="math"/>
	      <result property="english" column="english"/>
     </association>
</resultMap>
```

#####    14：一对多映射(one to many)	

- 一个班级对应多个学生；
- 嵌套 select
  -  < collection> 或 < association> 标签将一对多，类型的结果映射到一个对象集合上；

```xml
<!-- 一个班级对象多个学生 -->
<association property="stus" javaType="ArrayList" select="findStuByClassId" column="id" 	
             fetchType="lazy">
</association>

<collection property="stus" javaType="ArrayList" select="findStuByClassId" column="id" 		
            fetchType="lazy">
</collection>
```

- fetchType.LAZY ：懒加载，加载一个实体时，定义懒加载的属性不会马上从数据库中加载；
- fetchType.EAGER：饿加载，加载一个实体时，定义饿加载的属性会立即从数据库中加载

#####   15：多对多映射(many to many)

- 一个老师对应多个学生，一个学生对应多个老师，三个表之间维护；
- 打平产出所有学生，然后系统做Map；

##### 16：缓存【禁用】

![](/Users/likang/Code/Git/Middleware/12：Mybatis/photos/cache.png)

###### 一级缓存

- 场景：有可能在一次数据库会话中，执行多次查询条件完全相同的SQL；

- **Executor**： `SqlSession`向用户提供操作数据库的方法，但和**数据库操作有关的职责**都会委托给Executor。

- **默认情况下，开启一级缓存**，它仅仅对**同一个会话中的数据进行缓存**，作用域是SqlSession级别的；

- LocalCache 类，MyBatis提供了默认下**基于HashMap**的缓存实现；

- 有多个SqlSession或者分布式的环境下，数据库写操作会引起脏数据，建议设定缓存级别为**Statement**；

- **一级缓存配置：**

  ```xml
  <setting name="localCacheScope" value="SESSION"/>
  ```

###### 二级缓存

- 开启二级缓存后，会使用**CachingExecutor装饰Executor**，进入一级缓存的查询流程前，先在CachingExecutor进行二级缓存的查询；

- **默认情况下，不开启二级缓存**，二级缓存开启后，**同一个namespace下的所有操作语句**，都影响着同一个Cache，即二级缓存被多个SqlSession共享，是一个全局的变量；

- **二级缓存配置**

  1. 在MyBatis的配置文件中开启二级缓存；

     - ```xml
       <setting name="cacheEnabled" value="true"/>
       ```

  2. 在MyBatis的映射XML中配置cache或者 cache-ref，用于声明这个namespace使用二级缓存；

     - ```xml
       <cache eviction="LRU" flushInterval="100000" size="1024" readOnly="true"/>
       ```

       - eviction：代表是缓存置换算法，默认：LRU(Least Recently Used)
         - LRU：最近最少使用，移除最长时间不用的对像
         - FIFO：先进先出，按对像进入缓存的顺序来移除它们

       - flushInterval：缓存刷新间隔时间，单位为毫秒，如果不配置，那么当 SQL 被执行时，自动刷新缓存
       - size：缓存数量，代表缓存最多可以存储多个对象，不宜设置过大，设置过大会导致内存溢出；
       - readOnly：只读，意味着缓存数据只能读取而不能修改，它的默认值为 false；

     - 只针对此 select 语句的使用二级缓存，useCache="true"；

- **缺陷**

  - 通常我们会为每个单表创建单独的映射文件，由于MyBatis的二级缓存是基于`namespace`的，多表查询语句所在的`namspace`无法感应到其他`namespace`中的语句对**多表查询中涉及的表进行的修改，引发脏数据**问题。
  - < cache-ref />  : 共享一个nameSpace；

###### 注意

- 在分布式环境下，由于默认的**MyBatis Cache实现都是基于本地的**，分布式环境下必然会出现读取到脏数据，需要使用**集中式缓存将MyBatis的Cache接口实现**；
- 专注于做一个ORM框架，缓存使用Redis实现；

##### 17：CDATA

- 在 XML 元素中，"<" 和 "&" 是非法的，所以需要使用**特殊的字符转义**；

  - "<" 会产生错误，因为解析器会把该字符解释为新元素的开始。
  - "&" 也会产生错误，因为解析器会把该字符解释为字符实体的开始。

- ```
  &gt;  >  大于号[greater than]
  &lt;  <  小于号[less than]
  ```

- **CDATA**

  - xml  的语法，在CDATA内部的所有内容都会被解析器忽略，不会别转义；

  - ```xml
    <delete id="batchDelete" parameterType="java.lang.String" flushCache="true">
        DELETE FROM local_student
        <where>
            <if test="date != null and date.length() > 0">
                date <![CDATA[ <= ]]> #{date}>
            </if>
        </where>
    </delete>
    ```

    

