### 1：数据库的基础知识

数据库管理系统（DBMS）：管理数据库的软件

   DB2数据库：关系型数据库
   Microsoft SQL Server数据库 
   MySQL数据库：小型关系型数据库管理系统，开源的，Oracle 旗下，  c/c++编写的，64位系统支持最大的表文件为8TB
   Oracle数据库，学习的，商用收费，侵权

服务器---数据库（database）---数据表（TABLE）

数据库 DB（Database）：按照数据的结构来组织，存储和管理数据的仓库

数据表（Table）：是关系数据库的基本存储结构，二维的，由行列组成的一张表格，一个关系型数据库由多个数据表组成

关系数据库：描述两个元素之间的关联或对应关系，表与表之间的关系

记录（元组）：数据表中的一行
字段：数据表中的一列

表与表之间存在关联关系，一对一，一对多，多对多 三种关系

  主键（PRIMARY KEY）：其值能唯一地标识表中的每一行，作用是用于其他表的外键关联，以及本记录的修改与删除
  外键(FOREIGN KEY)  ：存放另一个表中主键的值的字段来表示关联，它的值必须是其他表中已存在的值
                       例：FOREIGN KEY (dep_id) REFERENCES dep(id) )

##### 2：MySQL服务 启动和关闭

   DOS命令行，root权限下
	net start mysql
	net stop mysql 
   Linux命令行
	service mysqld stop
	service mdsqld start

##### 3: mysql数据库 登录和退出 

?    登录      mysql -uroot -pmysql(密码) 
?    退出      \q
?    查看状态  \s
?    修改分隔符   delimiter 分隔符

##### 4: 数据库管理 

   show databases; 		查看当前服务器上管理的所有数据库 
   create database 数据库名;    创建数据库
   use 数据库名;                选择数据 
   alter database 数据库名 character set gbk;    修改编码格式
   drop database 数据库名;        删除数据库
   mysql -uroot -p 数据库名 < sql的URL    导入数据库 
   mysqldump -uroot -p xupt > xupt.sql

##### 5：查看帮助 

   ？...   

##### 6：修改密码

?	set password=password('mysql')；



