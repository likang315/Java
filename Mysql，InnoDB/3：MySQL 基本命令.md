### MySQL

------

##### 1：MySQL读取配置文件

​	当启动实例时，mysql数据库会去读取配置文件，若没有配置文件，它会按照编译时的默认参数设置启动实例

###### 读取顺序：

- /etc/my.cnf ----> /etc/mysql/my.cnf ----> /usr/local/mysql/etc/my.cnf ----> ~/my.cnf


##### 2：MySQL 服务的启动、关闭

- Linux命令行 
  - service mysqld stop
  - service mdsqld start

##### 3：MySQL登录、退出

- 登录： mysql -uroot -pmysql(密码) 
- 退出：\q 
- 查看状态： \s 
- 修改分隔符：delimiter 分隔符

##### 4：数据库管理

- show databases：查看当前服务器上管理的所有数据库 
- create database 数据库名; 
- 创建数据库 use 数据库名; 
- 修改编码格式：alter database 数据库名 character set gbk;  
- 删除数据库：drop database 数据库名; 
- 导入数据库：mysql -uroot -p 数据库名 < sql的URL 
- 导出数据库：mysqldump -uroot -p xupt > xupt.sql

##### 5：查看帮助

- ？...


##### 6：修改密码

- set password=password('mysql')；