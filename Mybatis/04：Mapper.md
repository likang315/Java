### Mapper 文件

------

##### 1：Mapper 映射文件全局标签

- cache：给定命名空间的缓存配置
- cache-ref：其他命名空间缓存配置的引用
- sql：复用SQL语句
- resultMap：用于定义实例对象和数据库表的字段的关系
- select： 映射查询语句
- insert：  映射插入语句
- update：映射更新语句
- delete： 映射删除语句

##### 2：select

- ###### 简易数据类型

  - parameterType：定义参数类型，值为这条语句的参数类的完全限定名或别名
  
  - resultType（resultMap）：定义返回值类型，不可同时使用，实际上内部是创建一个ResultMap 的
  
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
  
       1. map 传递多个参数时，parameterType="java.util.Map"，
         
          - key为参数，value是参数值，通过**#{变量.key}**获取值
          
       2. map 作为返回值时
          - 返回**一个对象**时，根据数据库的字段和值生成map
            - resultType = "java.util.Map"
          - 返回**多个对象**时，有两种情况
            - 通过**@MapKey("fieldName")**  指定pojo对象中一个属性名作为key
              - value：是个Map<字段名，字段值>
              - 用在mapper 接口定义的方法上；
            - 返回结果用List<Map<String, String>> 接收，一个Bean为：字段名-字段值；
          
          ```java
          @MapKey("id")
          List<Map<String, JavaBean>> listUserMap();
          ```
    
  - resultMap ：用于把复杂的pojo进行结果映射，一对多、一对一时；

```xml
<!--类类型的参数对象，id、username 和 password 属性将会被查找，将它们的值传入预处理语句的参数中-->
<insert id="insertUser" parameterType="user">
  insert into users (id, username, password) values (#{id}, #{username}, #{password})
</insert>
<!--自动把列名作为key，value作为值存储-->
<select id="selectPerson" parameterType="int" resultType="map">
  SELECT * FROM PERSON WHERE ID = #{id}
</select>
<!--数据库列名和类属性不一致-->
<select id="selectUsers" parameterType="int" resultType="person">
  select user_id as "id",user_name as "userName", hashed_password as "hashedPassword"
  from some_table where id = #{id}
</select>
```

- ###### 自动映射功能（两种方式）

  - 只要返回 **数据库列名和 JavaBean 的属性一致**，MyBatis 就会自动回填这些字段而无需任何配置
  - 如果你的数据库是规范命名的，即数据库每一个单词都用下划线分隔，POJO 采用驼峰式命名方法，可以**设置 mapUnderscoreToCamelCase 为 true，或者也可以实现从数据库到 POJO 的自动映射**
  - 自动映射可以在config.xml 中 settings 元素中配置 autoMappingBehavior 属性值来设置其策略
    - NONE：取消自动映射
    - **PARTIAL：部分的，只会自动映射没有定义嵌套结果集映射的结果集**
    - FULL：会自动映射任意复杂的结果集（无论是否嵌套）

```xml
<settings>
  <!--设置自动映射-->
	<setting name="autoMappingBehavior" value="PARTIAL"/>	
  <!--驼峰命名自动映射-->
  <setting name="mapUnderscoreToCamelCase" value="true" />
</settings>
```

- ###### useCache="ture" 只是针对此select语句的使用二级缓存

##### 3：insert、update、delete 的属性

- id：命名空间中的唯一标识符，用于确定使用接口的哪个方法
- **useGeneratedKeys：主键回填，插入数据成功后将会返回数据库自动生成的主键ID**
  - 默认值：false，仅对 insert 和 update 有用
- parameterType：传入语句的参数的完全限定类名或别名
- **keyProperty：标记哪个属性是主键，默认：未设置（unset）**
- **flushCache：任何时候只要该语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true**
- timeout：抛出异常之前，驱动程序等待数据库返回请求结果的秒数，默认值： 未设置（unset）
- statementType：发送sql语句的方式，STATEMENT，PREPARED 或 CALLABLE，默认值：PREPARED	
- keyColumn：通过生成的键值设置表中的列名，这个设置在某些数据库是必须的，当主键列不是表中的第一列的时候需要设置

```xml
<insert id="insertAuthor" useGeneratedKeys="true" keyProperty="id">
  insert into User (username,password,email) values (#{username},#{password},
  #{email})f
</insert>
```

###### 主键回填


​	 MySQL 中主键自增字段，在插入后我们入往往需要获得这个主键，把获得的主键值给 JavaBean 的 ID

```xml
<!--需要设置主键回填，和指定接收主键的属性，返回生成主键-->
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

###### 字符串替换：参数占位

- 使用 ${} 会自动加上 ' ' ，用于字符串，会出现Sql注入问题
- 使用 #{} 不会，替换值

###### 复用SQL：可以通过 property 替换值

```xml
<!--复用的SQL语句-->
<sql id="role_columns">
	id,name,note
</sql>

<select>
  select <include refid="role_columns"> 
						<property name="note" value="status"/>
 				 </include>
  from t_role where id=#{id}
</select>
```

##### 4：结果映射（ResultMap）

- 定义一个复杂结果集的映射关系，包含子对象的
- type：表示返回结果的类型（pojo类）

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

##### 5：一对一映射(one to one)

​	一个学生对应一个成绩

###### 1：内嵌对象

​	stu 中有一个属性为Score的子对象，使用**圆点记法**为内嵌的对象的属性赋值

```xml
<result property="score.math" column="math"/>
```

###### 2：ResultMap 继承 resultMap

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

###### 3：< association> 引入

​	被用来导入“有一个”(has-one)类型的关联，子对象

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
  <association property="score" resultMap="scoreResultMap"
               select="findScorebyid"/>
</resultMap>

```

###### 4：< association> 内联

```xml
<resultMap type="stu" id="stusMap">
     <id property="id" column="stu_id"/>
     <result property="clazzId" column="clazz_id"/>
     <result property="name" column="name"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     <!--property="Type值的属性"，column="表中的列"，对应stu的score-->
     <association property="score" javaType="Score" column="stu_id"
                  select="findScorebyid">
	      <id property="id" column="id"/>
	      <result property="math" column="math"/>
	      <result property="english" column="english"/>
     </association>
</resultMap>
```

###### 5：resultMap 嵌套 select查询

​	select 属性值设置为 <select> 语句属性ID值，column 属性值作为参数传递给 select 标签执行查询，返回结果

- column="表中的列作为参数"

```xml
<association property="score" select="findScorebyid" column="id" /> 
```

#####    6：一对多映射(one to many)	

​	一个班级对应多个学生

###### 嵌套 select 标签

​     < collection> 或 < association> 元素将一对多,类型的结果映射到 一个对象集合上

```xml
<!-- 一个班级对象多个学生 -->
<association property="stus" javaType="ArrayList" select="findStuByClassId" column="id" 	fetchType="lazy">
</association>  

<collection property="stus" javaType="ArrayList" select="findStuByClassId" column="id" 		fetchType="lazy">
</collection>
```

- fetchType.LAZY ：懒加载，加载一个实体时，定义懒加载的属性不会马上从数据库中加载
- fetchType.EAGER：饿加载，加载一个实体时，定义饿加载的属性会立即从数据库中加载

#####   7：多对多映射(many to many)

​	一个老师对应多个学生，一个学生对应多个老师，三个表之间维护

###### 嵌套 select 标签，一对多主要是三个表之间的关系

```xml
<select id="findStuByTeacherId" resultMap="stusMap">
  select s，st from stu_tea st,stu s where st.tea_id=#{id} and st.stu_id=s.id
</select>
```

```xml
<resultMap type="Teacher" id="teachMap">
   	 <id property="id" column="id"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     <result property="course" column="course"/>
     <collection property="stus" fetchType="lazy" select="findStuByTeacherId" 													 column="id">
  	 </collection>
</resultMap>
```

##### 8：缓存：

- 一级缓存：默认情况下，开启一级缓存，一级缓存只是相对于同一个 SqlSession
- 二级缓存：默认情况下，不开启二级缓存，二级缓存是 SqlSessionFactory 层面上的缓存，关闭会话连接仍然缓存值
  - MyBatis要求返回的**POJO必须是可序列化的**，也就是要求实现Serializable接口
  - 开启配置：在select语句中就可以开启二级缓存，useCache="true"；
  - mapper文件中 < cache/>  全局配置开启，很多设置是默认的，三种方式开启
  - < cache eviction="LRU" flushInterval="100000" size="1024" readOnly="true"/>
    - eviction：代表是缓存置换算法，默认：LRU(Least Recently Used)
      - LRU：最近最少使用，移除最长时间不用的对像
      - FIFO：先进先出，按对像进入缓存的顺序来移除它们
    - flushInterval：缓存刷新间隔时间，单位为毫秒，如果不配置，那么当 SQL 被执行时，自动刷新缓存
    - size：缓存数量，代表缓存最多可以存储多个对象，不宜设置过大，设置过大会导致内存溢出
    - readOnly：只读，意味着缓存数据只能读取而不能修改，它的默认值为 false

##### 9：@Param mybatis 的注解

- 用于参数命名，参数命名后就能根据名字得到参数值，正确的将参数传入sql语句中;
- 使用@Param后，使用**#{},**${} 都可以，否则只能使用#{} ;
- 若不使用@Param时，参数只能有一个，并且为JavaBean；
- 修改参数名，匹配SQL字段

```xml
Student getStudent(@Param("name") String Name)

<select id="getStudent" resultType="com.xuot.student">
     SELECT * FROM student
    where name = #{name}
    LIMIT 1
</select>
```

##### 10：<= , >=

- 在 XML 元素中，"<" 和 "&" 是非法的，所以需要使用特殊的字符转义；

  - "<" 会产生错误，因为解析器会把该字符解释为新元素的开始。
  - "&" 也会产生错误，因为解析器会把该字符解释为字符实体的开始。

- ```
  &gt;  >  大于号[greater than]
  &lt;  <  小于号[less than]
  ```

- <![CDATA[   ]]> 

  - xml  的语法，在CDATA内部的所有内容都会被解析器忽略，不会别转义；

  - ```xml
    <delete id="batchDelete" parameterType="java.lang.String" flushCache="true">
      DELETE FROM lowest_price_compare
      <where>
        <if test="date != null and date != '' ">
          date <![CDATA[ <= ]]> #{item}>
        </if>
      </where>
    </delete>
    ```

    

