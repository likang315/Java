### 动态 SQL：

------

[TOC]

##### 01：概述

- 动态SQL是 MyBatis 的强大特性之一，可以根据条件包含 where 子句的一部分。

##### 02：if

```xml
<select id="findAllStudents" parameterType="Students" resultType="com.xzy.pojo.Students">
    select * from students where stud_id>#{stud_id} 
    <if test="email != null and emial != ''"> 
      AND email=#{email}
    </if>
</select>
```

##### 03：choose、when、otherwise

- 有时不想用到所有的条件语句，而只想从中择其一项，类似于 switch


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

##### 04：where

- SQL 语句中限定条件语句 where 必须写成元素 < where>，否则会导致SQL预处理错误;
- < where>  元素只会在**至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句**，而且若语句的开头为“AND”或“OR”，*where* 元素也会将它们**去除多余的AND或者OR**；

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

##### 05：trim（insert）

- 用于去除 SQL 语句中**多余的 AND 关键字，逗号，或者给 SQL 语句前拼接 “where“、“set“以及“values(“ 等前缀，或者添加“)“等后缀**
  - **prefix**：给sql语句拼接的前缀
  - **suffix**：给sql语句拼接的后缀
  - **prefixesToOverride**：去除SQL语句前面的关键字或者字符，该关键字由prefixesToOverride 属性指定；
    - 假设该属性指定为”AND”，当sql语句的开头为”AND”，trim标签将会去除该”AND”
  - **suffixesToOverride**：去除SQL语句后面的关键字或者字符，该关键字或者字符由suffixesToOverride属性指定

```xml
<!--插入-->
<insert  id="insertBlog" parameterType="blog">
    INSERT INTO blog
    <trim prefix="("  suffix=")" suffixOverrides=",">
      <if test="state != null and state != ''">
        state,
      </if> 
      <if test="title != null and title != ''">
        title,
      </if>
    </trim>
    <trim prefix="values("  suffix=")" suffixOverrides=",">
      <if test="state != null and state != ''">
        #{state},
      </if> 
      <if test="title != null and title != ''">
        #{title}
      </if>
    </trim>
</insert>

<!--where标签相同的功能 -->
<trim prefix="WHERE" prefixOverrides="AND">
    <if test="state != null and state != ''">
      	state = #{state}
    </if>
    <if test="title != null and title != ''">
      	AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
      	AND author_name like #{author.name}
    </if>
</trim>
```

##### 06：set（update）

- 用于SQL动态更新语句的解决方案，**用于动态确定需要更新的列，set 元素会动态前置 SET关键字**，同时也会消除无关的逗号；


```XML
<update id="updateAuthorBatch">
  <foreach collection="idList" item="term" index="index" separator=";">
    UPDATE student
      <set>
        <if test="username != null">username=#{username},</if>
        <if test="email != null">email=#{email},</if>
      </set>
    WHERE id=#{item}
  </foreach>
</update>
```

##### 07：foreach(批量)

- 用于遍历集合，在批量查询、更新、插入中使用。指定一个集合，声明可以用在元素体内的**集合项（item）**和**索引变量（index）**，它也允许指定**开闭匹配的字符串**以及在**迭代中间分隔符，**它不会偶然地附加多余的分隔符。


- **迭代对象为数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素**
- **迭代对象为map时，index 是键，item 是值**

```XML
<select id="selectUser" resultType="user">
  SELECT *
  FROM User
  WHERE id IN
  <foreach index="index" item="item" collection="list" open="(" separator="," close=")">
    	#{item}
  </foreach>
</select>
<insert id="batchInsert" parameterType="java.util.Map">
    INSERT INTO tableName(a, b)
    VALUES
    <foreach collection="param.entrySet()" separator="," index="key" item="val">
        (key}, #{val})
    </foreach>
<select/>
```

##### 08：bind

- 从 OGNL 表达式中创建一个变量并将其绑定到上下文中，主要用于**模糊查询LIKE拼接**；


```xml
<select id = "getEmpsTestInnerParameter"
        parameterType = "com.xupt.User" resultType = "com.xupt.User">
  SELECT *
  FROM user 
  <!--name 为 User的一个属性-->
  <!-- LIKE CONCAT('%', #{name}, '%') -->
  <if test="name != null and name != ''">
    <bind name="pattern" value="'%' + name + '%'"/> 
    WHERE name LIKE #{pattern}
  </if>
</select>
```

