### Tomcat：

------

​	既是一个 Servlet 容器，也是一个独立的 WEB 应用服务器，Servlet 的运行环境叫做 Servlet 容器

##### 01：目录结构

- Bin ：存放启动和停止 tomcat 服务的脚本
- Conf：存放了相关的配置文件 
- Lib：存放了tomcat 用到的 java 库 *.jar 
- Logs：存放所有日志
- Temp ：临时目录
- Webapps ：布署 web 应用（网站）的目录 ，web应用放置到此目录下浏览器可以直接访问
- Work ：工作目录，jsp 将来生成 java 文件都放在此目录

##### 02：配置 tomcat

要求： 已安装 jdk 配置 path 和 classpath

- Step1：配置 JAVA_HOME 配到 jdk 的安装目录就可以 
- Step2：%tomcat_home%/lib/servlet-api.jar 加入 classpath 
- Step3：运行%tomcat_home%/bin/startup.bat
- Step4：打开浏览器  http://localhost:8080  (Tomcat的默认端口) ，可以修改

##### 03：配置server.xml

```xml
<Server>
    <Service>
    		<Connector URIEncoding="UTF-8" />
        <Engine>
            <Host>
            		<Context></Context>
            </Host>
        </Engine>
    </Service>
</Server>
```

1. < Server> : 代表一个servlet组件，它是tomcat的顶层元素，可以包含多个< Service> 元素；
2. < **Connector**>：代表与客户程序实际交互的组件，用来接收请求，并将客户的结果返回；
   - 相当于一个接待员，分发请求的；
3. < Engine>：处理services内的所有请求的，在services元素中只有一个；
4. < Host>：定义每个虚拟主机，在engine 中可以有多个；
5. < Context>：可以有多个context，代表的servletContext；

###### 修改tomcat 的端口号

- 把 tomcat 监听端口更改为 http 默认端口号 80 打开 %tomcat_home%/conf/server.xml

##### 04：Tomcat容器层器关系

![tomcat容器层次](/Users/likang/Code/Git/Middleware/Servlet，Session/Servlet/tomcat容器层次.jpg)

##### 05：Connector 处理一次请求

![Connector](/Users/likang/Code/Git/Middleware/Servlet，Session/Servlet/Connector.png)

##### 04：项目结构

- **webapp**

  - WEB资源Root目录，此目录下的WEB-INF 是安全目录，不能直接访问，必须跳转配置的URL，与已经有的路径都是站点的资源

- ##### WEB目录

  - ![WEB目录结构](/Users/likang/Code/Git/Middleware/Servlet，Session/Servlet/WEB目录结构.jpg)