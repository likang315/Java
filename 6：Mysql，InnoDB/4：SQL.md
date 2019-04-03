###   1：SQL (Structured Query Language)  结构化查询语言：在关系数据库上执行数据操作，检索及维护所使用的标准语言

SQL分为：
	1：数据定义语言（DDL）：用于创建，修改，删除数据库的对象，数据库对象包括：表，视图，索引，序列
	2：数据操作语言（DML）：用于改变数据表的数据。		增，删，改
	3：事物控制语言（TCL）： 用于维护数据一致性的语句。	提交，回滚，保存点
	4：数据查询语言（DQL）：用于查询所需要的语言
	5：数据控制语言（DCL）：用于执行权限的授予和回收操作，维护数据库。	授予，回收，创建用户

注意：
       1:SQL语句本身不区分大小写，但是出于可读性的目的，我们通常会将SQL的关键字全部大写，非关键字全部小写
       1: ；是不必要的但是作为一次语句的结束，分隔符，delimiter
       % 	替代一个或多个字符
	? 	仅替代一个字符

##### DDL语句：用于创建，修改，删除数据库的对象，数据库对象包括：表，视图，索引，序列

查看有多少个数据库  ：SHOW DATABESES；
选择数据库  	    ：USE 数据库名；
查看数据库中有多少表：SHOW TABLES；
查看表创建的语法    ：SHOW CREATE TABLE 表名 \G；
查看表的结构	    ：DESC 表名；

#####   创建表（CREATE）  //通常记事本写好直接copy

```sql
CREATE TABLE 表名（
	id	  数据类型（）PRIMARY KEY not null AUTO_INCREMENT，
	列名	数据类型（）not null DEFAULT defalut_value，
	列名	数据类型（），
	Foreign key( 列名) references 目标表名（列名)											
）;
```

###### 外键约束： foreign key references 引用外键表(列名) 

2：  foreign key(列名) references 目标表名(目标列名)

创建相同的表：CREATE TABLE 表1 LIKE 表2；
从已有的表中创建新表： CREATE TABLE 新表1 SELECT 字段1，字段2...FROM 原表2；

###### 约束（5个）：既可以写在字段之后，也可在最后用括号用约束字段,PRIMARY KEY(id)

   	   1：NULL，NOT NULL - 指示某列存储可以为 NULL或者不能为NULL值
    	   2：PRIMARY KEY - 主键 确保某列（或两个列多个列的结合）有唯一标识，有助于更容易更快速地找到表中的一个特定的记录
   	   3：FOREIGN KEY - 外键 一个表中的 FOREIGN KEY 指向另一个表中的 PRIMARY KEY(唯一约束的键)
    	   4：DEFAULT - 规定没有给列赋值时的默认值
	   5:   CHECK -  约束用于限制列中的值的范围，CHECK （条件）

###   修改表（ALTER）： 

1：修改表名

###### 	 AITER TABLE 表名 RENAME 新表名；

2：修改表的结构

###### 	增加新的字段：ALTER TABLE 表名 ADD 字段名... after 字段名|first；

###### 	修改现有字段：ALTER TABLE 表名 MODIFY 修改的字段；

###### 	修改字段名：     ALTER TABLE 表名 RENAME column A to B;

###### 	删除现有字段：ALTER TABLE 表名 DROP 字段名；

```sql
alter table stu add (name varchar(30) default ‘无名氏’ not null);
alter table stu modify (name varchar(16) default ‘unknown’);
```

###   删除表（DROP）：

###### 	DROP TABLE 表名；        删除表

###### 	TRUNCATE TABLE 表名；    清空表中数据



### DML语句：对表中的数据进行操作

1：INSERT INTO 向表中插入数据

###### 	INSERT INTO 表名(id，name，birth) VALUES(1，'jack'，TO_DATE（'2009-06-05',‘YYYY-MM-DD’）)；

如果插入的时日期字段:TO_DATE（'2009-06-05',‘YYYY-MM-DD’）

2：DELETE 删除表中的数据，要加WHERE语句限定删除的记录，否则就是清空表操作

###### 	DELETE FROM 表名 WHERE id=i；

3：UPDATE：修改表中的数据

###### 	UPDATE 表名 SET 字段名=值，字段名=值 WHERE 限制记录的字段；

```sql
update stu t set t.NAME = 'mike', t.SEX = '1' where t.ID = '2';
```

4:SELECT：查询表的内容，* 代表所有字段

###### 	SELECT * FROM 表名；  查看表的所有内容

SELECT 子句：后面跟要查询的字段，也可以是表中的具体字段函数或者表达式,直接在其后面加别名 as 别名；
	  那么结果集会以这个别名作为字段的名字，可以使用 "" 给别名区分大小写和添加空格

######   DISTINCT：对指定字段重复的记录去重，但是用多字段去重时，是字段的组合没有重复值(不同的)

例：SELECT DISTINCT column_name as new_name,column_name FROM table_name;  去重

######   FROM子句：用来制定数据表的来源，其后可以加 ORDER BY 字句排序；	

######   WHERE子句：用来添加限制条件，可以有多个，只将满足条件的记录查询出来

?	     1：>,<,>=,<=,<>等价于！=，=		
?	     2：使用AND，OR，NOT，AND 优先级高于 OR，通过括号来提高

 	     3：IS NULL ,IS NOT NULL ：判断是不是为空

?	     4：IN，NOT IN ：判断在不在列表中[]，常用来判断子查询的结果
?		例：WHERE id IN （1，2）；
?	     5：BETWEEN values1 AND values2  ：判断值在values1和values2之间()

?	     6：LIKE（模糊查询）：只知道其中某个字符
?		两个通配符：
?			    %：任意个字符，_：一个字符
?		name LIKE %lisi ;
?	     7：ANY，ALL是配合>,>=,<,<=,一个列表使用的，常用于子查询
?		1：>ANY(list)：大于列表最小的
?		2：>ALL（list）：大与列表最大的
?		3：<ANY (list) ：小于列表最小的
?		4：<ALL(list)：小于列表最小的



### 2：QUERY（查找）

投影(projector)：部分列组成的新的集合 
SELECT name as emp_name,sex,age FROM emp; 
选择(selector)：部分行组成新的集合 ，分表存储
SELECT * FROM stu WHERE id<10;

### 3：GROUP BY（分组）

将结果集按照其后指定的字段值相同的记录看做一组，常和分组函数联用

聚合函数：对某些字段的值进行统计的
    1：MAX（），MIN（）：求指定字段的最大值和最小值
    2：AVG，SUM：求平均值和总和
    3：COUNT（）函数：对给定字段的记录进行统计
 	  COUNT（*）:统计这个表有多少记录，有时候返回long，有时候返回BigInteger
 	  NVL（），配合进行

 select price, count(*) AS 记录数 from A group by price;

### 4：排序

ORDER BY 子句：可以根据指定的字段对结果集按照该字段的值进行升序或者降序排序

 ASC：升序，默认的不用写（ascending order）
DESC ：降序（descending order）
按照多字段排序时，首先按照第一个字段排序，当第一个字段有重复值时才会按照第二个字段排序方式进行排序 以此类推，每个字段都可以单独的指定排序方式，在排序中NULL值被认为是最大值
select * FROM stu1 ORDER BY age DESC;

### 5：WHERE 限制条件

WHERE ：不能使用聚合函数作为过滤条件，原因是过滤时机不对

WHERE是在数据库检索表中数据时，对数据逐条过滤以决定是否查询该数据是否使用的，所以WHERE用来确定结果集的数据的

若要使用聚合函数的结果作为过滤条件时，那么一定是数据从表中查询完毕得到的结果集，并且分组完毕才进行聚合函数统计结果，得到后才可以对分组进行过滤，由此可见这个过滤时机是在WHERE之后进行的，所以聚合函数的过滤条件要在HAVING子句中使用，
HAVING必须在GROUP BY之后

##### SELECT dep_id,count(dep_id) FROM emp GROUP BY dep_id HAVING count(dep_id)>1；

统计部门的人数

### 7：分页查询 LIMIT 

SELECT * FROM emp LIMIT 3;//返回从第一条开始，查询三条， 实际是：0,3;
SELECT * FROM emp LIMIT 3,5;//从结果的第4条开始，查询5条

### 8：UNION 操作符用于合并两个或多个 SELECT 语句的结果集，UNION 命令只会选取不同的值

请注意，UNION 内部的 **SELECT 语句必须拥有相同数量的列。列也必须拥有相似的数据类型。同时，每条 SELECT 语句中的列的顺序必须相同**

```sql
SELECT column_name(s) FROM table_name1
UNION ALL
SELECT column_name(s) FROM table_name2
```

###  UNION ALL 命令会列出所有的值，不去重，尽量用，效率高



### 8：多表查询(关联查询)：

###  从多张表中查询对应记录的信息，关联查询的重点与这些表中的记录的对应关系，这个关系也称连接条件

N张表就有N-1个连接条件

注意：
1：当两张表有相同字段时，SELECT子句中必须**明确指定该字段来自那张表**
	   在关联查询中，表名也可以有别名，在表名其后直接写，这样可以简化语句的复杂度

2：关联查询要添加连接条件，否则会产生**笛卡尔积**
   它是一个无意义的结果集，它的记录数是与所有参与查询的表的记录数乘积的结果，当数据量大时，会出现内存溢出

3：连接条件，过滤条件全部写在WHERE中
   例：SELECT e.id,d.dname AS dep_name,e.name,e.sex,e.age FROM emp e,dep d WHERE e.dep_id=d.id;



### 9：连接查询(JOIN)：用来完成关联查询

![](G:\Java\Java_note\13：SQL，Mysql，JDBC，dbutils，InnoDB\连接查询.png)

##### 内连接：获取两个表中字段匹配关系的记录，可以省略 INNER 使用 JOIN，效果一样

会自动优化成小表驱动大表，大表加索引提高查找速度

###### 	FROM 表名1 表1对象 INNER JOIN 表2名 表2对象 ON 连接条件 WHERE 过滤条件

例： select e.id,d.dname as dep_name,e.name,e.sex,e.age FROM emp e **INNER JOIN** dep d **ON e.dep_id=d.id;** 



##### 外链接：所有数据都显示

外连接分为： 

######   左外连接：（LEFT JOIN）以JOIN左侧作为驱动表，获取左表所有记录，即使右表没有对应匹配的记录，用NULL 填充

  例：select e.id,d.dname as dep_name,e.name,e.sex,e.age from emp e LEFT JOIN dep d ON e.dep_id=d.id; 

######   外链接：（RIGHT JOIN）右外连：与 LEFT JOIN 相反，用于获取右表所有记录，即使左表没有对应匹配的记录，用NULL填充

  例：select e.id,d.dname as dep_name,e.name,e.sex,e.age from emp e RIGHT JOIN dep d ON e.dep_id=d.id;

### 10：EXPLAIN：SQL执行计划

###### 查看运行SQL语句时哪种策略预计会被优化器采用，查看有没有走索引

```sql
+----+-------------+-------+-------+---------------+------+---------+------+------+--------------------------+
| id | select_type | table | type  | possible_keys | key  | key_len | ref  | rows | Extra                    |
+----+-------------+-------+-------+---------------+------+---------+------+------+--------------------------+
|  1 | SIMPLE      | t1    | index | PRIMARY       | name | 63      | NULL |    4 | Using where; Using index |
+----+-------------+-------+-------+---------------+------+---------+------+------+--------------------------+
```

#### 1：id：包含一组数字，表示查询中执行select子句或操作表的顺序

1：id相同，执行顺序由上至下

2：如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行

3：id 如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行

#### 2：select_type  示查询中每个select子句的类型（简单OR复杂）

?	a. SIMPLE：查询中不包含子查询或者UNION
?	b. 查询中若包含任何复杂的子部分，最外层查询则被标记为：PRIMARY
?	c. 在SELECT或WHERE列表中包含了子查询，该子查询被标记为：SUBQUERY
?	d. 在FROM列表中包含的子查询被标记为：DERIVED（衍生）用来表示包含在from子句中的子查询的select，mysql会递归执行并将结果放到一个临时表中。服务器内部称为"派生表"，因为该临时表是从子查询中派生出来的
?	e. 若第二个SELECT出现在UNION之后，则被标记为UNION；若UNION包含在FROM子句的子查询中，外层SELECT将被标记为：DERIVED
?	f. 从UNION表获取结果的SELECT被标记为：UNION RESULT

#### 3：type ：表示MySQL在表中找到所需行的方式，又称“访问类型”，常见类型如下:

 **ALL, index,  range, ref, eq_ref, const, system, NULL**

**从左到右，性能从最差到最好**

ALL：全表扫描

index：只遍历索引树

range：索引范围扫描，对索引的扫描开始于某一点，返回匹配值域的行

ref：使用**非唯一索引扫描或者唯一索引的前缀扫描，返回匹配某个单独值的记录行**

 NULL：MySQL在优化过程中分解语句，执行时甚至不用访问表或索引（覆盖索引）

#### 4：possible_keys  ：指出MySQL能使用哪个索引在表中找到记录，查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询使用

#### 5：key：显示MySQL在查询中实际使用的索引，若没有使用索引，显示为NULL

#### 6：key_len：表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度（key_len显示的值为索引字段的最大可能长度，并非实际使用长度，即key_len是根据表定义计算而得，不是通过表内检索出的）

#### 7：ref：表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值

#### 8：rows：估算的找到所需的记录所需要读取的行数

#### 9： Extra：包含不适合在其他列中显示但十分重要的额外信息

Using index ：该值表示相应的select操作中使用了**覆盖索引**

Using where：表示 mysql 服务器将**在存储引擎检索行后再进行过滤**

Using temporary：表示 MySQL 需要使用临时表来存储结果集，常见于排序和分组查询

#### 10：Table ：执行SQL用到的表名



### 11：select 语句的执行过程（ from--where--group by--having--select--order by ）

　　1、from子句组装来自不同数据源的数据； 
　　2、where子句基于指定的条件对记录行进行筛选； 
　　3、group by子句将数据划分为多个分组； 
　　4、使用聚集函数进行计算； 
　　5、使用having子句筛选分组； 
　　6、计算所有的表达式；（select:查看结果集中的哪个列，或列的计算结果 ）
　　7、使用order by对结果集进行排序

### 12：sql  语句的优化

**where子句--执行顺序为自下而上、从右到左**

Where 条件之前, 可以过滤掉最大数量记录的条件必须写在Where 子句的末尾

**group by--字段加上索引，最左前缀匹配原则**

把group by 的字段加上索引，因为符合最左前缀匹配原则，块

**having 子句----很耗资源，尽量少用**

避免使用HAVING 子句, **HAVING 只会在检索出所有记录之后才对结果集进行过滤.** 这个处理需要排序,总计等操作

**select子句--少用*号，尽量取字段名称**

在解析的过程中, 会将依次转换成所有的列名, 这个工作是通过查询数据字典完成的, 使用列名意味着将减少消耗时间

**limit 1 只查询一条语句时**

避免全表扫描，找到即返回

**使用Explain 取查看执行计划**

查看有没有走索引

#### count（1）和 count（*），count（字段）

###### 统计有多少条的记录 

count（1）：查询遍历的第一个字段，包含null 的记录,除非是主键索引，否则没什么区别和count（*）

##### count（*）会自己优化指定到哪一个字段，包含值为 null 的记录

count（字段）：统计该字段在表中出现的次数，不统计null记录

