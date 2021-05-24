### Spring Resource

------

[TOC]

##### 01：概述

- 获取外部资源，处理URL资源、File资源资源、ClassPath相关资源、服务器相关资源；
- Spring 为资源访问提供了一个 **Resource 接口**，该接口提拱了更强的资源访问能力，Spring框架本身大量使用Resource来访问底层资源；

##### 02：API

###### 实现类

- ClassPathResource：通过类路径获取资源文件
- FileSystemResource：通过文件系统获取资源
- UrlResource：通过URL地址获取资源
- ByteArrayResource：获取字节数组封装的资源
- ServletContextResource：获取ServletContext环境下的资源
- InputStreamResource：获取输入流封装的资源

###### 方法：

- exists()：返回 Resource 所指向的资源是否存在
- createRelative() ：根据资源的相对路径创建新资源
- getFilename() ：获取资源的文件名
- getInputStream()：获取当前资源的输入流，每次调用都返回新的输入流，调用者必须关闭输入流
- getFile：返回资源对应的 File 对象
- getURL：返回资源对应的 URL 对象
- isOpen()：返回资源文件是否打开
- getDescription()：返回资源的描述信息，通常是全限定文件名或实际 URL

```java
Resource fileResource = new FileSystemResource("D:/cxyapi/show.txt");
Resource fileResource = new ClassPathResource("aop.xml");
Resource fileResource = new ServletContextResource(application,"/configTest/cxyapi.txt");
```

###### ResourceLoader Interface

- 弱化对各个Resource接口的实现类的感知，不去本身去实现Resource，通过ResourceLoader接口，该接口的getResource(String location)方法可以用来获取资源。它的DefaultResourceLoader实现类可以适用于所有的环境；
- Resource getResource(String location)：根据资源地址加载文件资源，需要指定前缀
  - 在进行加载资源时需要使用前缀来指定加载：
  - "classpath:path"：表示返回ClasspathResource
  - "http://path"，"file:path"：表示返回UrlResource资源
  - 如果不加前缀则需要根据当前上下文来决定，DefaultResourceLoader默认实现是加载classpath资源；

###### 使用方式

- 所有ApplicationContext实现类，都实现了ResourceLoader接口


```java
// 获取的是 classPath 类型的Resource实现类
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-resource.xml");  
```

##### 03：资源地址表达式

1. classpath：从类路径中加载资源（Resource）
   - classpath:/beanfactory/**/bean.xml
2. file：使用 URLResource 从文件系统目录中装载资，相对，绝对路径都可以
   - file:/conf/com/baobaotao/beanfactory/bean.xml
3. http：使用 UrlResource 从 web 服务器中装载资源
   - http://www.baobaotao/resource/bean.xml
4. 没有前缀：根据 ApplicationContext 具体实现类采用对应的类型的Resource	

###### Ant 风格资源地址支持 3 种匹配符：

- ？：匹配文件名中的一个字符
- *：  匹配文件名中任意一个字符
- ** ：匹配此路径以及子路径下
