### Tomcat-9

------

##### 01：Mac 配置 tomcat

1.  下载Tomcat
   1. 首先到官网下载Tomcat：https://tomcat.apache.org/download-90.cgi，Core-tar.gz
   2. 解压Tomcat压缩包，然后放到/Library 路径下；
2. 配置Tomcat
   1. 进入可执行文件；	
      1. cd /Library/Tomcat/bin 
   2. 修改文件权限：
      1. chmod 755 *.sh
   3. 启动tomcat
      1. ./startip.sh
      2. ./shutdown.sh
3. 测试是否启动
   1. localhost:8080/

##### 02：Tomcat 目录结构

- /bin：存放windows或Linux平台上启动和关闭Tomcat的脚本文件
- /conf：存放Tomcat服务器的各种全局配置文件，其中最重要的是**server.xml**和web.xml
- /lib：存放Tomcat服务器所需的各种Jar包
- /temp：临时文件
- /logs：存放Tomcat执行时的日志文件
- /webapps：Tomcat的主要Web发布目录，默认情况下把Web应用文件放于此目录
- /work：存放JSP编译后产生的class文件

##### 03：支持的新特性

1. 新增HTTP/2支持和TLS虚拟主机；
2. 实现当前Servlet4.0规范草案；
3. BIO connectors 不再支持Windows Itanium 和 Comet；
4. Tomcat 9.0设计用于运行在Java se 8及以后的版本；