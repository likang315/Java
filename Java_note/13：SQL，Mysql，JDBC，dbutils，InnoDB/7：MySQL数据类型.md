### MySQL

### 1：数据类型（定义数据字段的类型对数据库的优化至关重要）

###### 大致可以分为三类：数值、日期/时间和字符串(字符)类型

注意：
	关键字INT是INTEGER的同义词，关键字DEC是DECIMAL的同义词

###### Java中的枚举类型可以转成int或者char存储，然后取出的时候在恢复成枚举类型





### 整数类型

1:TINYINT 	1 字节 	(-128，127) 			(0，255) 		小整数值
2:SMALLINT 	2 字节 	(-32 768，32 767) 		(0，65 535) 		大整数值
3:MEDIUMINT 	3 字节 	(-8 388 608，8 388 607) 	(0，16 777 215) 	大整数值
4:INT或INTEGER 	4 字节 	(-2 147 483 648，2 147 483 647) (0，4 294 967 295) 	大整数值
5:BIGINT 	8 字节  	极大整数值

6:FLOAT 	4 字节 	 				单精度,浮点数值
7:DOUBLE 	8 字节 	 				双精度,浮点数值
8:DECIMAL 	DECIMAL(M,D),如果M>D，为M+2否则为D+2 	小数值



### 日期和时间类型

?	每个时间类型有一个有效值范围和一个"零"值，当指定不合法的MySQL不能表示的值时使用"零"值

```sql
1:DATE 		3 字节 	1000-01-01/9999-12-31 			YYYY-MM-DD 		日期值
2:TIME 		3 字节	'-838:59:59'/'838:59:59' 		HH:MM:SS 		时间值或持续时间
3:YEAR 		1 字节	1901/2155 				YYYY 			年份值
4:DATETIME 	8 字节	1000-01-01 00:00:00/9999-12-31 23:59:59 YYYY-MM-DD HH:MM:SS 	混合日期和时间值
5:TIMESTAMP 	4 字节	1970-01-01 00:00:00/2038 		YYYYMMDD HHMMSS 	混合日期和时间值，时间戳 

(c_time：DATETIME,u_time：TIMESTAMP)
自动跟新特性：若定义一个字段为timestamp，这个字段里的时间数据会随其他字段修改的时候自动刷新
```

### 字符串类型

1:CHAR 		0-255 字节 		定长字符串
2:VARCHAR 	0-65535 字节 		变长字符串
3:TINYTEXT 	0-255字节 		短文本字符串
4:TEXT 		0-65 535字节 		长文本数据
5:MEDIUMTEXT 	0-16 777 215字节 	中等长度文本数据
6:LONGTEXT 	0-4 294 967 295字节 	极大文本数据

7:BLOB 	    0-65535字节 		二进制形式的长文本数据
8:TINYBLOB 	0-255字节 		不超过 255 个字符的二进制字符串
9:MEDIUMBLOB 	0-16 777 215字节 	二进制形式的中等长度文本数据
10:LONGBLOB 	0-4 294 967 295字节 	二进制形式的极大文本数据

### char 和 varchar 的区别：

1.char(n) 若存入字符数小于n，则以空格补于其后，查询之时再将空格去掉
2.char(n) 固定长度，varchar 是存入的实际字符数 +1 个字节（n<=255）或2个字节(n>255)
3.char 类型的字符串检索速度要比 varchar 类型的快

### TEXT 和 BLOB

1.BLOB和text存储方式不同，TEXT以文本方式存储，英文存储区分大小写，而Blob是以二进制方式存储，不分大小写
2.BLOB存储的数据只能整体读出
3.TEXT可以指定字符集，BLOB不用指定字符集\

### 2：MySQL对NULL的处理

IS NULL: 当列的值是 NULL,此运算符返回 true
IS NOT NULL: 当列的值不为 NULL, 运算符返回 true
<=>: 比较操作符（不同于=运算符），当比较的的两个值为 NULL 时返回 true

### 3：MySQL事务

InnoDB存储引擎提供事务**的隔离级别有**READ UNCOMMITTED、READ COMMITTED、REPEATABLE READ和SERIALIZABLE
  MySQL中只有使用了 Innodb 数据库引擎的数据库或表才支持事务，事务默认自动提交的

1：BEGIN或START TRANSACTION; 显式地开启一个事务
2：COMMIT;   提交事务，并使已对数据库进行的所有修改成为永久性的
3：ROLLBACK; 回滚会结束用户的事务，并撤销正在进行的所有未提交的修改
4：SET TRANSACTION；用来设置事务的隔离级别
5：SET AUTOCOMMIT=0 禁止自动提交
      SET AUTOCOMMIT=1 开启自动提交




