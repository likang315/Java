## JDBC ( java database connectivty )： Java 连接数据库的一种技术

JDBC API：sun 公司定义的一套接口（ java.sql.* ），用这些接口来操作数据库

###### JDBC驱动程序：由驱动类和数据库访问类组成,由数据库厂商提供，可以到Mysql网站下载（mysql-connector-j.jar）

配置JBDC驱动程序： D:\mysql-connector-java-5.1.20-bin.jar
IDEA配置：jar包导入

驱动类：JDBC 实现了 Java.SQL.Drive接口
数据库访问类分别实现了JDBC规范中,定义的数据库访问接口，主要包含有Connection(连接接口)，Statement(语句接口）和ResultSet(结果集接口)三种接口

##### Java.SQL.Drive接口：每个驱动程序类必须实现的接口 

##### JDBC 驱动程序工作过程可分为加载阶段、建立连接阶段和数据访问阶段

建立连接阶段将驱动类实例注册到DriverManager(驱动程序管理器)中，建立数据库连接

###### DriverManager:是一个JDBC驱动程序管理器，所有的驱动程序使用前都必须在其上登记注册，它提供对所有已注册驱动程序的管理	

###### 1：加载驱动：    得到class对象

Class.forName("com.mysql.jdbc.Driver");

###### 2：连接数据库（三种重载）：

```java
1：Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt?user=root&password=mysql");
2：Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt","root","mysql");
3：	    Properties p=new Properties();  //Map<String,String>
		p.setProperty("user","root");
		p.setProperty("password","mysql");
   Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/xupt",p);
```

###### 3：数据访问

  1：建立一个语句对象，是用于发送sql语句给数据库的
	Statement stat=con.createStatement();
  2：发送 sql语句给数据库





### DriverManager 类：驱动管理器类，用于管理驱动，也用于得到数据库的链接对象 Connection

static Connection getConnection(String url) 
      	试图建立到给定数据库 URL 的连接
static Connection getConnection(String url, Properties info) 
     	试图建立到给定数据库 URL 的连接 
static Connection getConnection(String url, String user, String password) 
      	试图建立到给定数据库 URL 的连接

注意：jdbc连接不同的数据库，方法全部一样，只是两个地方不一样，驱动类和url不一样

常用的数据库对应的JDBC驱动
	Oracle:oracle.jdbc.driver.OracleDriver 
	MySQL:com.mysql.jdbc.Driver

常见数据库的URL
	Oracle：jdbc:oracle:thin:@computerName或IP地址:端口:数据库名称
	MySQL： jdbc:mysql://computerName或IP地址:端口/数据库名称



### Connection 接口：得到实现此接口的实例，表示已和数据库建立链接

  方法：
	Statement createStatement() 
             创建一个 Statement 对象来将 SQL 语句发送到数据库
	PreparedStatement prepareStatement(String sql) 
             创建一个 PreparedStatement 对象来将参数化的 SQL 语句发送到数据库
	CallableStatement prepareCall(String sql) 
             创建一个 CallableStatement 对象来调用数据库存储过程  
	void close() 
             立即释放此 Connection 对象的数据库和 JDBC 资源，而不是等待它们被自动释放 
	DatabaseMetaData getMetaData() 
             获取一个 DatabaseMetaData 对象，该对象包含关于此 Connection 对象所连接的数据库的原数据 

特别：
	Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) 
          创建一个可以修改，保存ResultSet 对象 的Statement 对象，参数为ResultSet的常量
	



##### Statement 接口：用于执行 静态 SQL 语句 并返回它所生成结果的对象

 ResultSet executeQuery(String sql) 
         	SELECT语句
 int executeUpdate(String sql) 
     	INSERT、UPDATE 或 DELETE 语句 

 void addBatch(String sql) 
      	将给定的 SQL 命令添加到此 Statement 对象的当前命令列表中，调用executeBatch()可以批量执行此列表中的命令
 int[] executeBatch() 
      	将一批命令提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组

##### PreparedStatement 接口:Statement 的 子接口，表示预编译的 SQL 语句的对象，不会破坏Sql语句结构

ResultSet executeQuery() 
		在此 PreparedStatement 对象中执行 SQL 查询，并返回该查询生成的 ResultSet 对象 

int executeUpdate() 
    		在此 PreparedStatement 对象中执行 SQL 语句，INSERT、UPDATE 或 DELETE 语句

void setInt(int parameterIndex, int x) 
             将指定参数设置为给定int 值
void setString(int parameterIndex, String x) 
void setTime(int parameterIndex, Time x) 
void setDate(int parameterIndex, Date x) 

```java
String sql="insert into person(id,name,sex,age) values(?,?,?,?)";
PreparedStatement ps=con.prepareStatement(sql);
		
		for(int i=1;i<10;i++)
		{
			ps.setInt(1, i);
			ps.setString(2, "name"+i);
			ps.setString(3, i%2==0?"M":"F");
			ps.setInt(4, 20+i);
			ps.addBatch();  //添加到命令执行列表
		}
		int[] re=ps.executeBatch();
```



##### ResultSet 接口：表示数据库结果集的数据表，由执行查询数据库的语句生成，ResultSet 中有一个记录指针

###### 默认情况下，记录指针被置于第一行之前

 	    boolean next() 
          	将光标从当前位置向前移一行。 
	    boolean absolute(int row) 
		把记录指针移到指定行号
	    boolean previous() 
          	将光标移动到此 ResultSet 对象的上一行
	    boolean first() 
          	将光标移动到此 ResultSet 对象的第一行
	    boolean last() 
          	将光标移动到此 ResultSet 对象的最后一行 
	    void deleteRow() 
                从此 ResultSet 对象和底层数据库中删除当前行，得到的对象要给对Statement 参数给对

####     ResultSetMetaData getMetaData() 

获取此 ResultSet 对象的结果集数据表元数据，列号、类型和属性 

getXXX方法。。。。
setXXX方法。。。。
例：
int getInt(int columnIndex) 
	 以 Java 编程语言中 int 的形式获取此 ResultSet 对象的当前行中指定列序号的值
int getInt(String columnLabel) 
	 以 Java 编程语言中 int 的形式获取此 ResultSet 对象的当前行中指定列名的值

注意：
	1：ResultSet和表没有关系，只和select语句有关系
	2：SQL语句中拼接："+ +"
	3：带有 ' 符号的字符串可能会改变SQL语句的结构，所以使用PreparedStatement接口，先发送预编译的格式，再发参数，安全



##### ResultSetMetaData接口：封装了ResultSet结果表结构的原数据信息

getColunmCount()：取结果集总共有几列
getColumnName(i)：取指定列的名字称
getColumnType(i)：取指定列的类型

##### DatabaseMetaData接口：封装了数据库的原数据 

ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) 
获取可在给定类别中使用的表的描述，其他参数直接传null










