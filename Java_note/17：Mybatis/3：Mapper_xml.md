## Mapper XML：映射文件

##### 1：XML 映射文件有很少的几个顶级元素

cache -------给定命名空间的缓存配置
cache-ref – 其他命名空间缓存配置的引用
sql -------  可被其他语句引用的可重用sql语句

```xml
<!-- sql元素包含 -->
<!--<sql id="role_columns">
	id,name,note
</sql>
<select id="getRole" parameterType="int" resultMap="map">
	select <include refid="role_columns"/> from t_role where id=#{id}
</select>
```

resultMap – 用来描述如何从数据库结果集中来加载对象
	select – 映射查询语句
	insert – 映射插入语句
	update – 映射更新语句
	delete – 映射删除语句	

### 2：select 映射 

###### 1>:简易数据类型

​		parameterType:定义参数类型	resultType:定义返回值类型

###### 2>:自动映射

​	自动映射功能，只要返回 SQL 列名和 JavaBean 的属性一致，MyBatis 就会帮助我们回填这些字段而无需任何配置

   自动映射可以在 settings 元素中配置 autoMappingBehavior 属性值来设置其策略，它包含 3 个值：

​		NONE，取消自动映射
​		PARTIAL，只会自动映射没有定义嵌套结果集映射的结果集(映射部分)
​		FULL,会自动映射任意复杂的结果集（无论是否嵌套）

```xml
<setting name="autoMappingBehavior" value="PARTIAL"/>
```

​																								如果你的数据库是规范命名的，即数据库每一个单词都用下划线分隔，POJO 采用驼峰式命名方法，那么你也可以		设置 mapUnderscoreToCamelCase 为 true，这样就可以实现从数据库到 POJO 的自动映射

###### 3>:使用Map 传递多个参数或返回Map

​	使用 MyBatis 提供的 Map 接口作为参数来实现它，parameterType="map"，实现时直接把map传过去
   	传参时，Map的key 时参数，value是参数值
​	map 作为返回值时，返回一个map或者返回map集合： resultMap="map"

​	返回Map 是一个数据时，根据数据库的字段和值生成map															

​	返回map 是个对象时，可以是属性，value是值

###### 4>:通过JavaBean 传递参数

​	MyBatis 允许JavaBean,通过简单的 setter 和 getter 方法设置参数，设置parameterType="com.xzy.pojo.Students"，传递参数时把对象属性赋好

###### 5>:useCache="ture" 使用二级缓存



### 3：insert,update,delete 的属性

###### id：命名空间中的唯一标识符，选择使用哪个方法

###### parameterType：传入语句的参数的完全限定类名或别名

###### useGeneratedKeys：MyBatis 使用 JDBC的 getGeneratedKeys 方法来取出由数据库内部生成的主键,默认值：false

​		     （仅对 insert 和 update 有用）

###### keyProperty：标记哪个属性是主键，MyBatis 会通过 getGeneratedKeys 的返回值或者通过insert 语句的selectKey子元素设置它的键值，默认：unset（仅对 insert 和 update 有用）

###### flushCache：将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true

timeout：抛出异常之前，驱动程序等待数据库返回请求结果的秒数,默认值为 unset（依赖驱动）

statementType :STATEMENT，PREPARED 或 CALLABLE,默认值：PREPARED							keyColumn：通过生成的键值设置表中的列名，这个设置在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中					的第一列的时候需要设置(仅对 insert 和 update 有用)
databaseId：如果配置了 databaseIdProvider，MyBatis 会加载所有的不带databaseId 或匹配当前 databaseId 的语句
	    		如果带或者不带的语句都有，则不带的会被忽略

##### 4：主键回填

​	 MySQL 中主键自增字段,在插入后我们入往往需要获得这个主键，把获得的主键值给JavaBean的id
​		useGeneratedKeys="true" keyProperty="JavaBean_id"

##### 5：参数配置

​	通过El表达式的功能设置：#{age,javaType=int,jdbcType=NUMERIC}
​	指定用哪个 typeHandler 去处理参数:	
​		#{age,javaType=int,jdbcType=NUMERIC,typeHandler=MyTypeHandler}

##### 6：特殊字符串替换和处理(#和$)：占位

​	使用${}会自动加上""，用于字符串
​	使用#{}不会

##### 7：支持存储过程

​	具体参考pdf

##### 8：sql元素：定义一串 SQL 语句的组成部分，其它语句可以通过包含来使用它,并且可以通过<Property>传参

```xml
	select <include refid="role_columns"> 
				<property name="name" value="lisi"/>
		   </include> from t_role where id=#{id}
```

##### 9：ResultMap 映射结果集

​	resultMap定义的主要是一个结果集的映射关系，MyBatis 支持 resultMap 查询	

```xml
 <resultMap type="stu" id="stuscore">
     <id property="id" column="id"/>
     <result property="clazzId" column="clazz_id"/>
     <result property="name" column="name"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     
     <result property="score.math" column="math"/>
     <result property="score.english" column="english"/>
     <result property="score.pe" column="pe"/>
  </resultMap> 
```


 若Pojo(property) 和数据库类型(column)不匹配，新定义的参数id引用：#{stuId}

1：用 Map 存储结果集：resultType="map"
2：使用POJO 存储结果集：<resultMap id="roleResultMap" type="com.pojo.Role">和<property>
3：继承 ResultMap ：<resultMap type="Students" id="StudentRes" extends="StudentResult">

​	

## ​10：resultMap :映射结果集的

##### 一对一映射(one to one)

###### 1>:内嵌对象

​	stu 中有一个属性为Score,使用圆点记法为内嵌的对象的属性赋值
   <result property="score.math" column="math"/>------score.math stu对象的属性的属性	

###### 2>:ResultMap 继承嵌套

​	<resultMap type="stu" id="stuscore" extends="stus">

###### 3>:<association> 引入ResultMap

​	被用来导入“有一个”(has-one)类型的关联
   		<association property="score" resultMap="ScoreResult" />

4:>:<association>内联的 resultMap

```xml
 <resultMap type="stu" id="stusMap">
     <id property="id" column="id"/>
     <result property="clazzId" column="clazz_id"/>
     <result property="name" column="name"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     
     <association property="score" javaType="Score" column="stu_id">
	      <id property="id" column="id"/>
	      <result property="math" column="math"/>
	      <result property="english" column="english"/>
	      <result property="pe" column="pe"/>
     </association>
</resultMap> 
```

​	property="Type值的属性"---column="表中的列",对应stu的score

###### 5>:resultMap嵌套select查询

​	select属性设置成了 id 为 <select>属性id,column的值将作为参数传递给select

```xml
<association property="score" select="findScorebyid" column="id"> </association>
```

​	column="表中的列作为参数"

####    一对多映射(one to many)

###### ​	1>:嵌套 select标签

​     		<collection>或<association>元素将一对多,类型的结果映射到 一个对象集合上

```xml
<association property="stus" javaType="ArrayList" select="findStuByClassId" column="id" fetchType="lazy"> </association> 
<collection property="stus" javaType="ArrayList" select="findStuByClassId" column="id" fetchType="lazy"></collection>
```

fetchType.LAZY ：懒加载，加载一个实体时，定义懒加载的属性不会马上从数据库中加载
fetchType.EAGER：急加载，加载一个实体时，定义（饿）急加载的属性会立即从数据库中加载

####   多对多映射(many to many)

​	1>:嵌套select 语句，主要是三个表之间的关联
​		<select id="findStuByTeacherId" resultMap="stusMap">
 			select s.*,st.* from stu_tea st,stu s where st.tea_id=#{id} and st.stu_id=s.id
 		</select>

```xml
<resultMap type="Teacher" id="teachMap">
   	<id property="id" column="id"/>
     <result property="sex" column="sex"/>
     <result property="age" column="age"/>
     <result property="course" column="course"/>
     
    <collection property="stus" fetchType="lazy" select="findStuByTeacherId" column="id">		  	  </collection>
</resultMap>
```



## 11：缓存(buffer):存储内容访问命中率

一级缓存：默认情况下，开启一级缓存，一级缓存只是相对于同一个 SqlSession 
二级缓存：默认情况下，不开启二级缓存，二级缓存是SqlSessionFactory 层面上的缓存,MyBatis要求返回的POJO必须是
	  可序列化的，也就是要求实现Serializable接口
开启配置方法：在映射 XML 文件配置<cache>就可以开启二级缓存,useCache="true"

   <cache />像这样配置，很多设置是默认的，如果我们只是这样配置，那么就意味着：
例：<cache eviction="LRU" flushInterval="100000" size="1024" readOnly="true"></cache>
		eviction：代表是缓存置换算法,默认：LRU(Least Recently Used)
		LRU,最近最少使用，移除最长时间不用的对像
		FIFO,先进先出，按对像进入缓存的顺序来移除它们
	flushInterval:刷新间隔时间，单位为毫秒，如果你不配置它，那么当 SQL 被执行的时，才会去刷新缓存
	size：引用数目，一个正整数，代表缓存最多可以存储多个对象，不宜设置过大，设置过大会导致内存溢出
	Readonly:只读，意味着缓存数据只能读取而不能修改，它的默认值为 false,不允许我们修改


