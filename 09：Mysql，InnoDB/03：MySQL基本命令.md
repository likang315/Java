### MySQL

------

##### 1：MySQL读取配置文件

​	当启动实例时，mysql数据库会去读取配置文件，若没有配置文件，它会按照编译时的默认参数设置启动实例

###### 读取顺序：

- /etc/my.cnf ----> /etc/mysql/my.cnf ----> /usr/local/mysql/etc/my.cnf ----> ~/my.cnf


##### 2：MySQL 服务的启动、关闭

- Linux命令行 
  - service mysqld stop
  - service mysqld start

##### 3：Mac 配置数据库

###### 环境变量

- etc/profile、/etc/paths：是系统级别的，系统启动就会加载
- ～/.bash_profile：用户的环境变量，该文件包含专用于你的账户bash，shell的bash信息,当登录时以及每次打开新的shell时，该文件被读取

###### 查看PATH

-  echo  $PATH：查看环境变量
- /usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/likang/Code/Java/Maven/apache-maven-3.6.1/bin
  - ：为path分隔符

###### 修改PATH 

1. sudo vi ～/.bash_profile :使用vi打开.bash_profile ,必须使用sudo ，否则没有权限保存
2. export Maven_HOME=/Users/likang/Code/Java/Maven/apache-maven-3.6.1/bin
3. 

1. ```shell
   export PATH=$PATH:$Maven_HOME:$MySQL_HOME
   # 配置多个时用：分割
   # 在PATH变量后面加多一个目录Maven_HOME
   ```

4：source ~/.bash_profile ：立即生效

##### 4：修改密码

1. 关闭mysql服务
2. cd /usr/local/mysql/bin/
   - sudo su
   - ./mysqld_safe --skip-grant-tables & ：关闭mysql验证功能
3. ./mysql
4. FLUSH PRIVILEGES;  
5. SET PASSWORD FOR 'root'@'localhost' = PASSWORD('你的新密码');

##### 5：MySQL登录、退出

- 登录： mysql -uroot -pmysql(密码) 
- 退出：\q 
- 查看状态： \s 
- 修改分隔符：delimiter 分隔符

##### 4：数据库管理

- show databases：查看当前服务器上管理的所有数据库 
- create database 数据库名; 
- use 数据库名; 
- 修改编码格式：alter database 数据库名 character set gbk; 
- 删除数据库：drop database 数据库名; 
- 导入数据库：mysql -uroot -p 数据库名 < sql的URL 
- 导出数据库：mysqldump -uroot -p xupt > xupt.sql

##### 5：查看帮助

- ？...


