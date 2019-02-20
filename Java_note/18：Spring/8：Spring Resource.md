### Spring Resource(访问资源,org.springframework.core.io)

  Spring 为资源访问提供了一个 Resource 接口，该接口提拱了更强的资源访问能力，Spring框架本身大量使用Resource来访问底层资源

##### 1：Resource ：一个接口，所有资源访问类必须实现的接口

方法:
	getInputStream();定位并打开资源，返回资源对应的输入流，每次调用都返回新的输入流，调用者必须关闭输入流
	exists()返回 Resource 所指向的资源是否存在
	getFile：返回资源对应的 File 对象
	getURL: 返回资源对应的 URL 对象

​	isOpen():返回资源文件是否打开，如果资源文件不能多次读取，每次读取结束时应该显式关闭，防止资源泄漏
​	getDescription()返回资源的描述信息，通常是全限定文件名或实际 URL
​	getFile：返回资源对应的 File 对象
​	getURL: 返回资源对应的 URL 对象

​     Resource 接口本身没有提供访问底层资源的实现，针对不同的资源，Spring 提供了不同的实现类

##### 2：Resource 实现类

ClassPathResource：访问类加载路径里资源的实现类
FileSystemResource:访问文件系纺里资源的实现类
ServletContextResource：访问相对于 ServletContext 路径里的资源的实现类
InputStreamResource：访问输入流资源的实现类

UrlResource：访问网络资源的实现类
ByteArrayResource:访问字节数组资源的实现类

```java
例：FileSystemResource res=new FileSystemResource("D:/cxyapi/show.txt");
    Resource res=new ClassPathResource("aop.xml");
    Resource res = new ServletContextResource(application,"/configTest/cxyapi.txt");
```

##### 3：资源地址表达式

​	1：classpath：从类路径中加载资源
​		classpath:com/baobaotao/beanfactory/bean.xml
​	2：file：使用 URLResource 从文件系统目录中装载资源，可采用绝对或相对路径
​		file:/conf/com/baobaotao/beanfactory/bean.xml
​	3：http：使用 UrlResource 从 web 服务器中装载资源
​		http://www.baobaotao/resource/bean.xml
​	4：没有前缀：根据 ApplicationContext 具体实现类采用对应的类型的Resource	
​		com/baobaotao/beanfatory/beans.xml

Ant 风格资源地址支持 3 种匹配符：
？：匹配文件名中的一个字符

*：  匹配文件名中任意一个字符
** ：匹配此路径以及子路径下
classpath:com/**/test.xml : 匹配 com 路径下（当前目录及其子目录）的test.xml 文件

##### 4：ResourceLoader 接口访问(org.springframework.core.io)

###### ResourceLoader （interface）只有一个方法

​	Resource getResource(String location)：根据一个资源地址加载文件资源，资源地址只支持带资源类型前缀的表达式			

###### ResourcePatternResolver implements ResourceLoader

​	Resource[] getResources（String locationPattern）：支持带资源类型前缀及 Ant 风格的资源路径的表达式

###### PathMatchingResourceResolver implements ResourcePatternResolver 

   例：
	ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	Resource[] resources = resolver.getResources("file:C:/Documents and Settings/Administrator/桌面/*.rar");
	









