### Dubbo

------

##### 01：Dubbo 的服务架构

![Dubbo](/Users/likang/Code/Git/Middleware/Dubbo/photos/Dubbo.png)

###### 节点角色

| 节点      | 角色说明                               |
| --------- | -------------------------------------- |
| Provider  | 暴露服务的服务提供方                   |
| Consumer  | 调用远程服务的服务消费方               |
| Registry  | 服务注册与发现的注册中心               |
| Monitor   | 统计服务的调用次数和调用时间的监控中心 |
| Container | 服务运行容器                           |

##### 02：运行过程

1. 启动容器，加载，**运行服务提供者**。
2. 服务提供者在启动时，在注册中心**发布注册**自己提供的**服务**。
3. 服务消费者在启动时，在注册中心**订阅**自己所需的**服务**。
4. 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
5. 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
6. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

##### 03：示例

###### 服务端

服务端的接口写好，因为其实 dubbo 的作用简单来说就是给消费端提供接口。

```java
/**
 * xml方式服务提供者接口
 */
public interface ProviderService {

    String SayHello(String word);
}

/**
 * xml方式服务提供者实现类
 */
public class ProviderServiceImpl implements ProviderService{

    public String SayHello(String word) {
        return word;
    }
}
```

###### 暴露接口（xml配置方法）

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
   
       <!--dubbo这个服务所要暴露的服务地址所对应的注册中心-->
       <!--<dubbo:registry address="N/A"/>-->
       <dubbo:registry address="N/A" />
   
       <!--当前服务发布所依赖的协议；webserovice、Thrift、Hessain、http-->
       <dubbo:protocol name="dubbo" port="20880"/>
   
       <!--服务发布的配置，需要暴露的服务接口-->
       <dubbo:service
                      interface="com.sihai.dubbo.provider.service.ProviderService"
                      ref="providerService"/>
   
       <!--Bean bean定义-->
       <bean id="providerService" 
             class="com.sihai.dubbo.provider.service.ProviderServiceImpl"/>
   ```

2. 上面的文件其实就是类似 spring 的配置文件，而且，dubbo 底层就是 spring。

   - **节点：dubbo:application**
     就是整个项目在分布式架构中的唯一名称，可以在 `name` 属性中配置，另外还可以配置 `owner` 字段，表示属于谁。
   - **节点：dubbo:monitor**
     - 监控中心配置， 用于配置连接监控中心相关信息，可以不配置，不是必须的参数
   - **节点：dubbo:registry**
     - 配置注册中心的信息，比如，这里我们可以配置 zookeeper 作为我们的注册中心。`address` 是注册中心的地址，这里我们配置的是 `N/A` 表示由 dubbo 自动分配地址。或者说是一种直连的方式，不通过注册中心。
   - **节点：dubbo:protocol**
     - 服务发布的时候 dubbo 依赖什么协议，可以配置 dubbo、webserovice、Thrift、Hessain、http等协议。
   - **节点：dubbo:service**
     - 重点接口，当我们服务发布的时候，我们就是通过这个配置将我们的服务发布出去的。`interface` 是接口的包路径，`ref` 配置的接口的 bean。

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
   dubbo://192.168.234.1:20880/com.sihai.dubbo.provider.service.ProviderService?anyhost=true&application=provider&bean.name=com.sihai.dubbo.provider.service.ProviderService&bind.ip=192.168.234.1&bind.port=20880&dubbo=2.0.2&generic=false&interface=com.sihai.dubbo.provider.service.ProviderService&methods=SayHello&owner=sihai&pid=8412&qos.accept.foreign.ip=false&qos.enable=true&qos.port=55555&side=provider&timestamp=1562077289380
   ```

   - dubbo://192.168.234.1:20880/com.sihai.dubbo.provider.service.ProviderService
   - ? 之前的链接，构成：**协议://ip:端口/接口**，类似于HTTP请求。

###### 消费端

- 在消费端的 resource 下建立配置文件consumer.xml，因为没有使用注册中心，所以配置有些不一样；

- 

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
  		dubbo.crawler.zookeeper=zk.beta.corp.xupt.com:2181
  		dubbo.crawler.group=/content/feed-crawler/xupt/32238
  		-->
      <dubbo:registry id="crawlerRegistry" protocol="zookeeper"
                      address="@dubbo.crawler.zookeeper@"
                      group="@dubbo.content.group@" />
    	<dubbo:reference id="contentSearchService"
                       registry="crawlerRegistry"
                       interface="com.xupt.travel.service.CollectionService"
                       timeout="3000" version="1.0.0" check="false"/>
  
      <!--生成一个远程服务的调用代理-->
      <!--点对点方式-->
      <dubbo:reference id="providerService"
                       interface="com.sihai.dubbo.provider.service.ProviderService"
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
          String str = providerService.SayHello("hello");
          System.out.println(str);
          System.in.read();
      }
  }
  ```

  