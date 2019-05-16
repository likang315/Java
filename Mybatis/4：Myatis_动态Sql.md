## 动态SQL：

   元素和使用 JSTL 或其他类似基于 XML 的文本处理器相似，MyBatis 采用功能强大的基于 OGNL 的表达式来消除其他元素，**通常要做的是有条件地包含 where 子句的一部分**

##### 1：if元素

```xml
<select id="findAllStudents" resultType="com.xzy.pojo.Students" parameterType="Students" 		useCache="true">
    select * from students where stud_id>#{stud_id} 
    <if test="email!=null"> 
      and email=#{email}
    </if>
 </select>
```

##### 2：choose, when, otherwise,有时不想用到所有的条件语句，而只想从中择其一二,选择判断

```XML
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
```

### 3：where

​	sql 语句中限定条件语句 where 必须写成元素 <where>，否则会导致SQL预计错误

where 元素知道只有在一个以上的 if 条件有值的情况下才去插入“WHERE”子句,而且若最后的内容是“AND”或“OR”开头的， where 元素也知道如何去除他们

```xml
<select id="selectByParams" parameterType="map" resultType="user">
    select * from user
    <where>
        <if test="id != null ">id=#{id}</if>
        <if test="name != null and name.length()>0" >and name=#{name}</if>
        <if test="gender != null and gender.length()>0">and gender = #{gender}</if>
    </where>
</select>　　　　　
```

在上述SQL中加入ID的值为null的话，那么打印出来的SQL为：select * from user where name="xx" and gender="xx"

###### where 标记会自动将其后第一个条件的 and 或者是 or 给忽略掉



### 4：Set

set 元素用于动态更新语句的解决方案,**被用于动态包含需要更新的列,set 元素会动态前置 SET关键字**，同时也会消除无关的逗号

```XML
<set>
		<if test="username != null">username=#{username},</if>	
</set>
```



### 5：foreach：对一个集合进行遍历，通常是在构建 IN 条件语句的时候

   foreach 元素的功能是非常强大的，它允许你指定一个集合，声明可以用在元素体内的集合项和索引变量,它也**允许你指定开闭匹配的字符串以及在迭代中间放置分隔符**。这个元素是很智能的，因此它不会偶然地附加多余的分隔符

   注意:你可以将任何可迭代对象（如列表、集合等）和任何的字典或者数组对象传递给 foreach作为集合参数。
	当使用可迭代对象或者数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素
	当使用字典（或者 Map.Entry 对象的集合）时，index 是键，item 是值

```XML
<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
	#{item}
</foreach>
```



#### 使用注解配置 SQL 映射器

​	MyBatis 也支持使用注解来配置映射语句,当我们使用基于注解的映射器接口时，我们不再需要在 XML 配置文件中配置

  @Insert,@Update,@Delete,@Select 

 









