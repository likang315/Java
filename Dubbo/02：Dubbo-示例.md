### Dubbo 示例

------

[TOC]

##### 01：dubbo 示例下载

###### 安装：

```sh
git clone https://github.com/apache/dubbo.git
cd dubbo/dubbo-demo/dubbo-demo-xml
运行 dubbo-demo-xml-provider中的org.apache.dubbo.demo.provider.Application
如果使用Intellij Idea 请加上-Djava.net.preferIPv4Stack=true
```

###### 配置：

```sh
resources/spring/dubbo-provider.xml
修改其中的dubbo:registry，替换成真实的注册中心地址，推荐使用zookeeper，如：
<dubbo:registry address="zookeeper://127.0.0.1:2181"/>
```

##### 02：示例

###### 导入依赖

```xml
<!--  理论上只依赖JDK，可以不依赖任何第三方库-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.7.3</version>
</dependency>
```

###### 服务提供者

服务端的接口写好，因为其实 dubbo 的作用简单来说就是给消费端提供接口。

```java
// 定义服务接口
public interface ProviderService {
    String sayHello(String word);
}
// 在服务提供方实现接口
public class ProviderServiceImpl implements ProviderService {
    public String sayHello(String word) {
        return word;
    }
}
```

###### 用 Spring 配置声明暴露服务

1. 在项目的 resource 目录下**创建 META-INF.spring 包**，然后再创建 **provider.xml** 文件，名字可以任取；

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
          xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd        http://code.alibabatech.com/schema/dubbo        http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
   
       <!--当前项目在整个分布式架构里面的唯一名称，计算依赖关系的标签-->
       <dubbo:application name="provider" owner="xupt">
           <dubbo:parameter key="qos.enable" value="true"/>
           <dubbo:parameter key="qos.accept.foreign.ip" value="false"/>
           <dubbo:parameter key="qos.port" value="55555"/>
       </dubbo:application>
     
       <dubbo:monitor protocol="registry"/>
   
       <!--该服务所要暴露的服务地址所对应的注册中心-->
       <dubbo:registry address="N/A" />
   
       <!-- 用dubbo协议在20880端口暴露服务 -->
       <dubbo:protocol name="dubbo" port="20880"/>
   
       <!--服务发布的配置，需要暴露的服务接口-->
       <dubbo:service interface="com.dubbo.provider.service.ProviderService"
                      ref="providerService"/>
   
       <!--Bean bean定义-->
       <bean id="providerService" 
             class="com.dubbo.provider.service.ProviderServiceImpl"/>
   ```
   
2. 上面的文件其实就是类似 spring 的配置文件，而且，dubbo 底层就是 spring。

   - **节点：dubbo:application**
     就是整个项目在分布式架构中的唯一名称，可以在 `name` 属性中配置，另外还可以配置 `owner` 字段，表示属于谁。
   - **节点：dubbo:monitor**
     - 监控中心配置， 用于配置连接监控中心相关信息，可以不配置，不是必须的参数
   - **节点：dubbo:registry**
     - 配置注册中心的信息，比如，这里我们可以配置 zookeeper 作为我们的注册中心。`address` 是注册中心的地址，这里我们配置的是 **N/A 表示由 dubbo 自动分配地址**。或者说是一种直连的方式，不通过注册中心。
   - **节点：dubbo:protocol**
     - 服务发布的时候 dubbo 依赖什么协议，可以配置 dubbo、webserovice、Thrift、Hessain、http等协议。
   - **节点：dubbo:service**
     - 重点接口，当我们服务发布的时候，我们就是通过这个配置将我们的服务发布出去的，**interface 是接口的包路径，ref 配置的接口的 bean 的 引用。**

3. ###### 发布接口

   ```java
   public class App {
       public static void main( String[] args ) throws IOException {
           //加载xml配置文件启动
           ClassPathXmlApplicationContext context = 
             new ClassPathXmlApplicationContext("META-INF/spring/provider.xml");
           context.start();
         	// 按任意键退出
           System.in.read();
       }
   }
   ```

4. ###### dubbo暴露的URL

   ```java
   dubbo://192.168.234.1:20880/com.dubbo.provider.service.ProviderService?anyhost=true&application=provider&bean.name=com.dubbo.provider.service.ProviderService&bind.ip=192.168.234.1&bind.port=20880&dubbo=2.0.2&generic=false&interface=com.dubbo.provider.service.ProviderService&methods=SayHello&owner=xupt&pid=8412&qos.accept.foreign.ip=false&qos.enable=true&qos.port=55555&side=provider&timestamp=1562077289380
   ```

   - dubbo://192.168.234.1:20880/com.dubbo.provider.service.ProviderService
   - ? 之前的链接，构成：**协议://ip:端口/接口**，类似于HTTP请求。
     - IP：提供dubbo服务的机器地址；

###### 服务消费者

- 在消费端的 resource 下建立配置文件consumer.xml，因为没有使用注册中心，所以配置有些不一样；

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
         xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd        http://code.alibabatech.com/schema/dubbo        http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
  
      <!--当前项目在整个分布式架构里面的唯一名称，计算依赖关系的标签-->
      <dubbo:application name="consumer" owner="xupt"/>
  
      <!--dubbo这个服务所要暴露的服务地址所对应的注册中心-->
      <!--点对点的方式-->
      <dubbo:registry address="N/A" />
      
    	<!-- 使用zookeeper做为注册中心
  		dubbo.crawler.zookeeper=zk.beta.xupt.com:2181
  		dubbo.crawler.group=/content/feed-crawler/
  		-->
      <dubbo:registry id="crawlerRegistry"
                      protocol="zookeeper"
                      address="@dubbo.crawler.zookeeper@"
                      group="@dubbo.content.group@" />
    	<dubbo:reference id="providerService"
                       interface="com.xupt.dubbo.provider.service.ProviderService" />
  
      <!--生成一个远程服务的调用代理-->
      <!--点对点方式-->
      <dubbo:reference id="providerService"
                       interface="com.dubbo.provider.service.ProviderService"
                       url="dubbo://192.168.234.1:20880
                            /com.xupt.dubbo.provider.service.ProviderService"/>
  </beans>
  ```

- 调用接口

  ```java
  /**
   * xml的方式调用
   */
  public class App {
      public static void main( String[] args ) throws IOException {
          ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("consumer.xml");
          context.start();
          ProviderService providerService = 
            (ProviderService) context.getBean("providerService");
          String str = providerService.sayHello("hello");
          System.out.println(str);
          System.in.read();
      }
  }
  ```

  