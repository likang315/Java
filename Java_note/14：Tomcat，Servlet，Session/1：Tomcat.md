### Servlet 的运行环境叫做 Servlet容器

### Tomcat：是一个Servlet和JSP容器，独立的 WEB 服务器软件

##### 目录结构

   Bin     存放启动和停止 tomcat 服务的脚本
   Conf    放了相关的配置文件
   Lib     放了tomcat 用到的 java 库 *.jar
   Logs    放所有日志
   Temp    临时目录
   Webapps 布署 web 应用（网站）的目录
   Work    工作目录，jsp 将来生成 java 文件都放在此目录

##### 配置 tomcat

要求：
  已安装 jdk
  配置 path 和 classpath

   Step1:配置 java_home 配到 jdk 的安装目录就可以
   Step2:%tomcat_home%/lib/servlet-api.jar 加入 classpath
   Step3:运行%tomcat_home%/bin/startup.bat
   Step4:打开浏览器 http://localhost:8080(Tomcat的默认端口)

 把tomcat 监听端口更改为 http 默认端口号 80
	打开%tomcat_home%/conf/server.xml

###### 项目中：WebContent：是Root目录，此目录下的WEB-INF 是安全目录，不能直接访问

配置的URL，与已经有的路径都是站点的资源


















