### 动态 SQL 元素：

​	动态SQL是MyBatis 的强大特性之一，作用：根据条件包含 where 子句的一部分

##### 1：if

```xml
<select id="findAllStudents" resultType="com.xzy.pojo.Students"     parameterType="Students" 	useCache="true">
    select * from students where stud_id>#{stud_id} 
    <if test="email!=null"> 
      AND email=#{email}
    </if>
  	<if test="name != null and email != null">
   	  AND name like #{email}
  	</if>
</select>
```

##### 2：choose, when, otherwise

​	有时不想用到所有的条件语句，而只想从中择其一项，类似于 switch

```XML
<select id="findActiveBlogLike" resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```

##### 3：where

​	SQL 语句中限定条件语句 where 必须写成元素 <where>，否则会导致SQL预计错误

< where>  元素只会在至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句。而且，若语句的开头为“AND”或“OR”，*where* 元素也会将它们去除多余的AND或者OR

```xml
<select id="selectByParams" parameterType="map" resultType="user">
    select * from user 
    <where>
        <if test="id != null ">
          id=#{id}
        </if>
        <if test="name != null and name.length()>0" >
          and name=#{name}
        </if>
        <if test="gender != null and gender.length()>0">
          and gender = #{gender}
        </if>
    </where>
</select>
```

在上述SQL中加入ID的值为null的话，那么打印出来的SQL为：select * from user where name="xx" and gender="xx" ，解决了select * from user where 这种情况的问题

##### 4：trim

用于去除 SQL 语句中**多余的 AND 关键字，逗号，或者给 SQL 语句前拼接 “where“、“set“以及“values(“ 等前缀，或者添加“)“等后缀**

- **prefix**：给sql语句拼接的前前缀
- **suffix**：给sql语句拼接的后缀
- **prefixesToOverride**：去除SQL语句前面的关键字或者字符，该关键字由prefixesToOverride 属性指定
  - 假设该属性指定为”AND”，当sql语句的开头为”AND”，trim标签将会去除该”AND”
- **suffixesToOverride**：去除SQL语句后面的关键字或者字符，该关键字或者字符由suffixesToOverride属性指定

```xml
<!-- 和where 标签相同的功能 -->
<trim prefix="WHERE" prefixOverrides="AND">
  <if test="state != null">
    state = #{state}
  </if> 
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</trim>

<insert  id="insertBlog" parameterType="blog">
    INSERT INTO blog
    <trim prefix="("  suffix=")" suffixOverrides=",">\
      <if test="state != null">
        state
      </if> 
      <if test="title != null">
        title
      </if>
    </trim>
    <trim prefix="values("  suffix=")" suffixOverrides=",">
      <if test="state != null">
        #{state}
      </if> 
      <if test="title != null">
        #{title}
      </if>
    </trim>
</insert>
```

##### 5：Set（update  语句）

​	用于SQL动态更新语句的解决方案，**用于动态包含需要更新的列,set 元素会动态前置 SET关键字**，同时也会消除无关的逗号

```XML
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

##### 6：foreach：用于遍历集合，在 IN 条件语句中

允许你指定一个集合，声明可以用在元素体内的集合项（item）和索引变量（index），它也允许你指定开闭匹配的字符串以及在迭代中间放置分隔符。这个元素是很智能的，它不会偶然地附加多余的分隔符

- 迭代对象为数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素
- 迭代对象为map时，index 是键，item 是值

```XML
<select id="selectUser" resultType="user">
  SELECT * FROM User u WHERE id IN
  <foreach item="item" index="index" collection="list" 
           open="(" separator="," close=")">
    #{item}
  </foreach>
</select>
```

##### 7：bind

​	创建一个变量并将其绑定到上下文	

```xml
<!-- List<Employee> getEmpsTestInnerParameter(Employee employee); 定义的mapper接口 -->
<select id="getEmpsTestInnerParameter" resultType="com.hand.mybatis.bean.Employee">
  <!-- name -->
  <bind name="Name" value="'%'+name+'%'"/> 
  SELECT * FROM emp 
  <if test="_parameter!=null">
    where ename like #{Name}
  </if>
</select>
```


