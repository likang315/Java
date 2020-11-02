### SQL（Structured Query Language ）

​	结构化查询语言：在关系数据库上执行数据操作，检索及维护所使用的标准语言

------

##### 1：SQL 分类：

1. 数据定义语言（DDL）：
   - 创建，修改，删除数据库的对象，数据库对象包括：表，视图，索引，序列 
2. 数据操作语言（DML）：
   - 修改数据表的数据，增，删，改
3. 事物控制语言（TCL）：
   -  维护数据一致性的语句 提交，回滚，保存点
4. 数据查询语言（DQL）：
   - 查询所需要的语言
5. 数据控制语言（DCL）：
   - 执行权限的授予和回收操作，维护数据库，授予，回收，创建用户

######  注意：

- SQL语句本身不区分大小写，但是出于可读性的目的，我们通常会将SQL的关键字全部大写，非关键字全部小写
- ；：分隔符，是不必要的，但是作为一次语句的结束
- %：替代一个或多个字符
- ?：仅替代一个字符

##### 2：DDL：create、drop、alter、show

- SHOW databases；查看有多少个数据库
- USE 数据库名；选择数据库
- SHOW TABLES；查看数据库中有多少表
- SHOW CREATE TABLE 表名； 查看表创建的语法
-  DESC 表名；查看表的结构

###### 创建表（CREATE）

```SQL
CREATE TABLE 表名（
	id	 数据类型（）PRIMARY KEY NOT NULL AUTO_INCREMENT，
	列名	数据类型（）not null DEFAULT defalut_value，
	列名	数据类型（），
	Foreign key(列名) references 目标表名（列名)
）;
# 通常记事本写好直接 copy
```

- 创建相同的表：CREATE TABLE 表1 LIKE 表2;
- 从已有的表中创建新表： CREATE TABLE 新表1 SELECT 字段1，字段2...FROM 原表2；

###### 约束条件：

​	既可以写在字段之后，也可在最后用括号用约束字段，例：PRIMARY KEY(id)

1. NULL，NOT NULL ：指示某列存储可以为 NULL或者不能为NULL值
2. PRIMARY KEY ：主键，确保某列（或两个列多个列的结合）有唯一标识，有助于更容易更快速地找到表中的一个特定的记录
3. FOREIGN KEY：外键，一个表中的 FOREIGN KEY 指向另一个表中的 PRIMARY KEY(唯一约束的键)
4. DEFAULT：规定没有给列赋值时的默认值
5. CHECK：约束用于限制列中的值的范围，CHECK （条件）
6. UNSIGNED：定义数值类型是一定要说明是否有无符号；

###### 删除表（DROP）：

- DROP TABLE 表名； 删除表

###### 修改表（ALTER）：

1. 修改表名
   - AITER TABLE 表名 RENAME 新表名；
2. 修改表的结构
   - 增加新的字段：ALTER TABLE 表名 **ADD** 字段名... after 字段名|first；
   - 修改现有字段：ALTER TABLE 表名 **MODIFY** 修改的字段；
   - 修改字段名：   ALTER TABLE 表名 **CHANGE**  旧字段名 新字段名 数据类型 ... ;
   - 删除现有字段：ALTER TABLE 表名 **DROP** 字段名；

- alter table stu add name varchar(30) default 'xxx' not null after id;
- alter table stu modify name varchar(16) default 'unknown';

##### 3：DML：insert、delete、update、select

1. INSERT INTO 向表中插入数据
  
   - INSERT INTO tableName(id，name，birth) VALUES(1，'jack','199-22-45'）)；
   
     ```sql
     # 批量插入 
     INSERT INTO table_name (id, name,sex,address)
     VALUES
     (?,?,?,?),(?,?,?,?),(?,?,?,?),(?,?,?,?)
     ```
2. DELETE 删除表中的数据，要加WHERE语句限定删除的记录，否则就是清空表操作
  
   - DELETE FROM 表名 WHERE id=i;
3. UPDATE：修改表中的数据
   - UPDATE 表名 SET 字段名=值，字段名=值 WHERE 限制记录的字段；
   - UPDATE stu t set t.name = 'mike', t.id = '1' where t.ID = '2';
4. SELECT：查询表的内容，* 代表所有字段
   - SELECT * FROM 表名； 查看表的所有内容（禁用）
   - SELECT：具体的字段，也可以是表中的具体字段函数或者表达式，直接在其后面加别名
     -  as 别名：那么结果集会以这个别名作为字段的名字
     - 可以使用 "" 给别名区分大小写和添加空格
       - select name as "Name" from student;
   - FROM：用来限制数据表的来源
   - WHERE子句：添加限制条件，只会将满足条件的记录查询出来
     1. <,>=,<=,<>等价于!=
     2. AND，OR，NOT，AND 优先级高于 OR，可以通过括号来提高优先级
     3. IS NULL ,IS NOT NULL ：判断是不是为空
     4. IN，NOT IN ：判断在不在列表中[]，常用来判断子查询的结果
        - WHERE id IN (1,25)；
        - IN 后不能接子查询，并且常量数尽量不超过200个；
     5. BETWEEN values1 AND values2 ：判断值在values1和values2之间
     6. LIKE（模糊查询）：只知道其中某个字符
        1. %：任意个字符
        2. _ ：一个字符 
        3. name LIKE %lisi
     7. ANY，ALL：常用于子查询
        - ANY：任意一个
        - ALL：所有的
        - select * from stu where class='1' and age > any (select age from stu where class='2')；
        - 查询出01班中，年龄大于 02班任意一个的同学

##### 4：DISTINCT：

- 对指定字段去重，用多字段去重时，是字段的拼接没有重复值
- SELECT DISTINCT column_name as new_name,column_name FROM table_name; (组合去重)

##### 5：QUERY：查找

- 投影(projector)：部分列组成的新的集合 
  - SELECT name as Name,sex,age FROM emp; 
- 选择(selector)：部分行组成新的集合
  - SELECT * FROM stu WHERE id<10;

##### 6：GROUP BY：分组

- 将结果集按照指定的**字段值相同的记录分为一组**，常和分组函数联用；
- 如果在以某字段为根据进行了分组之后，select 那个字段得到的不是这个字段的信息，而是这个字段的分组信息，如果不与聚合函数联合使用，只显示该组第一条记录；
- 使用Select查询的列，在使用分组的语句中**要不包含在group by 字段中，要不包含在聚合函数中**。不然查询会报错。
- select stu.name, **avg(math) as math, avg(chinese) as chinese** from score s INNER JOIN student stu ON stu.id = s.student_id **group by stu.name**;
  - score 表中可能会有多个一个学生的成绩，求平均
    - 先找到联表中符合限定条件的信息；
    - 以group by 后的字段作为分组key；
    - 进行聚合计算，最终显示也是key，字段名；
- **多列分组**：按照多列（字段A+字段B）合并后的值进行分组；
- **HAVING 条件表达式**：用来限制分组后的显示，符合条件表达式的结果将被显示；
  - HAVING   COUNT(sex) >= 1;
- **WITH ROLLUP**：将会在所有记录的最后加上一条记录，这一条记录是上面所有记录的总和。

###### 聚合函数：对某些字段的值进行统计的

- MAX（），MIN（）：求指定字段的最大值和最小值
- AVG，SUM：求平均值和总和
- COUNT( )：对给定字段的记录进行统计 
  - COUNT（*）：统计这个表有多少记录，有时候返回long，有时候返回 BigInteger，配合进行
  - SELECT price, **count(*) AS number** FROM tablename GROUP BY price；

##### 7：ORDER BY ：排序

​	将结果集按照指定的字段，对该字段的值进行升序或者降序排序

- ASC：升序，默认的不用写（ascending order） 
- DESC ：降序（descending order）
  - 按照多字段排序时，首先按照第一个字段排序，当第一个字段有重复值时才会按照第二个字段排序方式进行排序
  - 多字段排序时，每个字段都可以单独的指定排序方式
    -  SELECT * FROM stu1 ORDER BY age DESC，id ASC;
  - 在排序中NULL值被认为是最大值

##### 8：WHERE：限制条件

- WHERE ：不能使用聚合函数作为过滤条件，原因是过滤时机不对
- WHERE：是在数据库检索表中数据时，对数据逐条过滤以决定是否查询该数据是否使用的，所以WHERE用来**确定结果集**的数据的
- 聚合函数时**从结果集中**，并且分组完毕才进行过滤的，由此可见这个过滤时机是在WHERE之后进行的，所以聚合函数的过滤条件要在HAVING子句中使用， HAVING必须在GROUP BY之后
- SELECT dep_id,count(dep_id) AS count FROM emp GROUP BY dep_id **HAVING count(dep_id)>1**；

##### 9：LIMIT：分页查询

- SELECT * FROM emp LIMIT 3;      从第一条开始，查询三条，实际是：0,3
- SELECT * FROM emp LIMIT 3,5;   从结果的第3条开始，查询5条
- SELECT * FROM emp WHERE id > 3 LIMIT 5; 
  - 当第一个值比较大时，尽量使用id 的方式高效分页，否则可能会逐行扫描到指定数值后，再进行分页，效率较慢。

##### 10：UNION、UNION ALL

- 用于合并两个或多个 SELECT 语句的结果集，UNION 命令只会选取不同的值
- UNION 内部的 SELECT 语句必须拥有相似的列对应
- SELECT column_name(s) FROM table_name1  UNION ALL  SELECT column_name(s) FROM table_name2；
- UNION ALL：命令会列出所有的值，不去重，效率高

##### 11：SELECT 语句的执行过程（ from--where--group by--having--select--order by ）

1. FROM 子句：组装来自不同数据源的数据
2. WHERE 子句：基于指定的条件对记录行进过滤
3. GROUP BY子句：将数据划分为多个分组，使用聚集函数进行计算
4. HAVING 子句：根据过滤条件，对分组进行过滤
5. 计算所有的表达式，例：select:查看结果集中的哪个列，或列的计算结果 
6. OERDER BY ：对结果集进行排序

##### 12：多表查询（关联查询）：

​	从多张表中查询信息，关联查询的重点与这些表中的记录的对应关系，这个关系也称连接条件，N张表就有N-1个连接条件

- 当两张表有相同字段时，SELECT子句中必须明确指定该字段来自那张表，在关联查询中，表名也可以有别名，在表名其后直接写，可以简化语句的复杂度
- 关联查询要添加连接条件，否则会产生**笛卡尔积它是一个无意义的结果集**，它的记录数是与所有参与查询的表的记录数乘积的结果，可能会出现内存溢出
- 连接条件全部写在WHERE中
- SELECT e.id,e.name FROM emp e INNER JOIN dep d ON e.dep_id=d.dep_id;   

##### 13：连接查询(JOIN)：用来完成关联查询

![](https://github.com/likang315/Middleware/blob/master/Mysql%EF%BC%8CInnoDB/InnoDB/%E8%BF%9E%E6%8E%A5%E6%9F%A5%E8%AF%A2.png?raw=true)

###### 内连接：获取两个表中字段匹配关系的记录，可以省略 INNER 使用 JOIN，效果一样

- FROM 表名1 表1对象 INNER JOIN 表2名 表2对象 ON 连接条件 WHERE 过滤条件
- select e.id,d.dname as dep_name,e.name,e.sex,e.age FROM emp e **INNER JOIN** dep d **ON e.dep_id=d.id;**
- 会自动优化成小表驱动大表，大表加索引提高查找速度

###### 外链接：所有数据都显示

- 左外连（LEFT JOIN）：以JOIN左侧作为驱动表，获取左表所有记录，即使右表没有对应匹配的记录的字段值，用NULL 填充
- select e.id,d.dname as dep_name,e.name,e.sex,e.age from emp e **LEFT JOIN** dep d **ON** e.dep_id=d.id;
- 右外连（RIGHT JOIN）：与 LEFT JOIN 相反，用于获取右表所有记录，即使左表没有对应匹配的记录的字段值，用NULL填充
- select e.id,d.dname as dep_name,e.name,e.sex,e.age from emp e **RIGHT JOIN** dep d **ON** e.dep_id=d.id;

##### 14：EXPLAIN：SQL执行计划

查看运行SQL语句时哪种策略预计会被优化器采用，查看有没有走索引

```shell
# EXPLAIN select * from student;
| id | select_type | table | type  | possible_keys | key  | key_len | ref  | rows | Extra										 |
|  1 | SIMPLE      | t1    | index | PRIMARY       | name | 63      | NULL |    4 | Using where; Using index |
```

1. ###### id：包含一组数字，表示查询中执行 select 子句 或 操作表的顺序	

   - id相同，执行顺序由上至下
   - id 如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，先执行

2. ###### select_type 示查询中每个select子句的类型（简单OR复杂）

   - SIMPLE：查询中不包含子查询或者UNION
   - 查询中若包含任何复杂的子部分，最外层查询则被标记为：PRIMARY 
   - 在SELECT或WHERE列表中包含了子查询，该子查询被标记为：SUBQUERY
   - 在FROM列表中包含的子查询被标记为：DERIVED（派生）用来表示包含在from子句中的子查询语句
   - 若第二个SELECT出现在UNION之后，则被标记为UNION；若UNION包含在FROM子句的子查询中，外层SELECT将被标记为：DERIVED 
   - 从UNION表获取结果的SELECT被标记为：UNION RESULT

3. ###### table ：执行SQL用到的表名

4. ###### type ：表示MySQL在表中找到所需行使用的方式，又称“访问类型”

   - ALL, index, range, ref, eq_ref, const, system, NULL，从左到右，性能从最差到最好
     - ALL：全表扫描
     - index：只遍历索引树
     - range：索引范围扫描，对索引的扫描开始于某一点，返回匹配值域的
     - ref：使用非唯一索引扫描或者唯一索引的前缀扫描，返回匹配某个单独值的记录行
     - NULL：MySQL在优化过程中分解语句，执行时甚至不用访问表或索引（覆盖索引）

5. ###### possible_keys ：

   - 指出MySQL可能使用哪个索引在表中找到记录的，查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询使用

6. ###### key：

   - 显示MySQL在查询中实际使用的索引，若没有使用索引，显示为NULL

7. ###### key_len：

   - 表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度（key_len显示的值为索引字段的最大可能长度，并非实际使用长度，即key_len是根据表定义计算而得，不是通过表内检索出的）

8. ###### ref：

   - 表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值

9. ###### rows：

   - 估算的找到所需的记录所需要读取的行数

10. ###### Extra：

    - 包含不适合在其他列中显示但十分重要的额外信息
    - Using index ：该值表示相应的select操作中使用了覆盖索引
    - Using where：表示 mysql 服务器将在存储引擎检索行后再进行过滤
    - Using temporary：表示 MySQL 需要使用临时表来存储结果集，常见于排序和分组查询

##### 15：SQL 语句的优化

1. WHERE子句：执行顺序为从右到左
   - WHERE 条件之前, 可以过滤掉最大数量记录的条件，必须写在WHERE 子句的末尾
2. ORDER BY：字段加上索引，最左前缀匹配原则
   - 把ORDER BY 的字段加上索引，因为符合最左前缀匹配原则
3. HAVING子句：很耗资源，尽量少用
   - 所有记录之后才对结果集进行过滤，非常消耗资源
4. SELECT子句：少用\*号，尽量取字段名称
   - 在解析的过程中, 会将依次转换成所有的列名
5. LIMIT 1：查询一条符合时终止
   - 避免全表扫描，找到即返回
6. 使用EXPLAIN 查看执行计划
   - 查看有没有走索引

##### 16：count（1）和 count（*），count（字段） 的区别

​	统计有多少条的记录，效率上

- count（1）：查询遍历的第一个字段，包含null 的记录，除非是主键索引，否则和count（*）没有区别
- count（*）：会自己优化指定到使用索引的字段，包含值为 null 的记录
- count（字段名）：统计该字段在表中出现的次数，不统计null记录

##### 17：SQL 中单引号和双引号的区别

- 标准 SQL 中，字符串使用的是单引号，但是mysql做了兼容双引号也可以；
- 若字符串中标也有单引号，则使用两个单引号，转义的；