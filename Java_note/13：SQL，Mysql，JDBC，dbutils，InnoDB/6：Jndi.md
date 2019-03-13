##### JNDI(Java Naming and Directory Interface)：Java命名和目录接口

一个应用程序设计的API，为开发人员提供了查找和访问各种命名和目录服务的通用、统一的接口,所有的J2EE容器都必须
提供一个JNDI的服务

###### DataSource被绑定在了JNDI树上（为每一个DataSource提供一个名字）客户端通过名称找到在JNDI树上绑定的DataSource，

再由DataSource找到一个连接

JNDI配置
	javax.naming.InitialContext

```java
//初始化名称查找上下文环境
InitialContext ic = new InitialContext(); 
//通过JNDI名称找到DataSource,在tomcat下必须加：java:comp/env对名称进行定位,后面跟的是DataSource名
DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/test");-------检索命名对象
```


Context.xml 必须配置在META-INFO下