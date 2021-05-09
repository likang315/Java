### Dubbo 配置

------

[TOC]

##### 01：xml 配置

###### 配置标签

| 标签                   | 用途         | 解释                                                         |
| ---------------------- | ------------ | ------------------------------------------------------------ |
| `<dubbo:service/>`     | 服务配置     | 用于**暴露一个服务**，定义服务的元信息，一个服务可以用多个协议暴露，一个**服务也可以注册到多个注册中心**，是根据ByName暴露服务的 |
| `<dubbo:reference/>`   | 引用配置     | 用于创建一个**远程服务代理**，一个引用可以指向多个注册中心   |
| `<dubbo:protocol/>`    | 协议配置     | 用于**配置提供服务的协议信息**，协议由提供方指定，消费方被动接受 |
| `<dubbo:application/>` | 应用配置     | 用于**配置当前应用信息**，不管该应用是提供者还是消费者       |
| `<dubbo:module/>`      | 模块配置     | 用于配置当前模块信息，可选                                   |
| `<dubbo:registry/>`    | 注册中心配置 | 用于**配置连接注册中心**相关信息                             |
| `<dubbo:monitor/>`     | 监控中心配置 | 用于**配置连接监控中心**相关信息，可选                       |
| `<dubbo:provider/>`    | 提供方配置   | 当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选 |
| `<dubbo:consumer/>`    | 消费方配置   | 当 ReferenceConfig 某属性没有配置时，采用此缺省值，可选      |
| `<dubbo:method/>`      | 方法配置     | 用于 ServiceConfig 和 ReferenceConfig 指定方法级的配置信息   |
| `<dubbo:argument/>`    | 参数配置     | 用于指定方法参数配置                                         |

###### 不同粒度配置的覆盖关系

1. 方法级优先，接口级次之，全局配置再次之。
2. 如果级别一样，则消费方优先，提供方次之。

以 timeout 为例，下图显示了配置的查找顺序，建议由服务提供方设置超时，因为一个方法需要执行多长时间，服务提供方更清楚，如果一个消费方同时引用多个服务，就不需要关心每个服务的超时设置

![xml-配置优先级](/Users/likang/Code/Git/Middleware/Dubbo/photos/xml-配置优先级.png)

###### 延迟加载

- 引用缺省是**延迟初始化**的，只有引用被注入到其它 Bean，或被 `getBean()` 获取，才会初始化。如果需要饥饿加载，即没有人引用也立即生成动态代理，可以配置：**<dubbo:reference ... init="true" />**

##### 02：属性配置（dubbo.properties文件）

- 如果你的应用足够简单，例如，不需要多注册中心或多协议，并且需要在spring容器中共享配置，那么，我们可以直接使用 `dubbo.properties`作为默认配置。
- **Dubbo可以自动加载classpath根目录下的dubbo.properties**，也可以使用JVM参数来指定路径：
  - -Ddubbo.properties.file=xxx.properties

###### 映射规则

- 可以**将xml的tag名和属性名组合**起来，用‘.’分隔。每行一个属性。

  - `dubbo.application.name=foo` 相当于 `<dubbo:application name="foo" />`

- 如果在xml配置中有**超过一个的tag，那么你可以使用‘id’进行区分**。如果你不指定id，它将作用于所有tag。

  - `dubbo.protocol.rmi.port=1099` 相当于 `<dubbo:protocol id="rmi" name="rmi" port="1099" />`

- 示例

  ```sh
  dubbo.application.name=foo
  dubbo.application.owner=bar
  dubbo.registry.address=10.20.153.10:9090
  ```

###### 重写与优先级

- 优先级从高到低：
  - JVM -D参数，当你部署或者启动应用时，它可以轻易地重写配置，比如，改变dubbo协议端口；
  - XML, XML中的当前配置会重写dubbo.properties中的；
  - Properties，默认配置，仅仅作用于以上两者没有配置时。
- 如果在classpath下有超过一个dubbo.properties文件，比如，两个jar包都各自包含了dubbo.properties，dubbo将随机选择一个加载，并且打印错误日志。
- 如果 id 没有在 protocol 中配置，将使用`name`作为默认属性。

##### 03：API 配置【太重，不使用】

- 可用于测试dubbo服务；

- ```java
  // 服务提供者
  import org.apache.dubbo.rpc.config.ApplicationConfig;
  import org.apache.dubbo.rpc.config.RegistryConfig;
  import org.apache.dubbo.rpc.config.ProviderConfig;
  import org.apache.dubbo.rpc.config.ServiceConfig;
  import com.xxx.XxxService;
  import com.xxx.XxxServiceImpl;
   
  // 服务实现
  XxxService xxxService = new XxxServiceImpl();
   
  // 当前应用配置
  ApplicationConfig application = new ApplicationConfig();
  application.setName("xxx");
   
  // 连接注册中心配置
  RegistryConfig registry = new RegistryConfig();
  registry.setAddress("10.20.130.230:9090");
  registry.setUsername("aaa");
  registry.setPassword("bbb");
   
  // 服务提供者协议配置
  ProtocolConfig protocol = new ProtocolConfig();
  protocol.setName("dubbo");
  protocol.setPort(12345);
  protocol.setThreads(200);
   
  // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
   
  // 服务提供者暴露服务配置
  ServiceConfig<XxxService> service = new ServiceConfig<XxxService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
  service.setApplication(application);
  service.setRegistry(registry); // 多个注册中心可以用setRegistries()
  service.setProtocol(protocol); // 多个协议可以用setProtocols()
  service.setInterface(XxxService.class);
  service.setRef(xxxService);
  service.setVersion("1.0.0");
   
  // 暴露及注册服务
  service.export();
  
  
  // 服务消费者
  import org.apache.dubbo.rpc.config.ApplicationConfig;
  import org.apache.dubbo.rpc.config.RegistryConfig;
  import org.apache.dubbo.rpc.config.ConsumerConfig;
  import org.apache.dubbo.rpc.config.ReferenceConfig;
  import com.xxx.XxxService;
   
  // 当前应用配置
  ApplicationConfig application = new ApplicationConfig();
  application.setName("yyy");
   
  // 连接注册中心配置
  RegistryConfig registry = new RegistryConfig();
  registry.setAddress("10.20.130.230:9090");
  registry.setUsername("aaa");
  registry.setPassword("bbb");
   
  // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
   
  // 引用远程服务
  ReferenceConfig<XxxService> reference = new ReferenceConfig<XxxService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
  reference.setApplication(application);
  reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
  reference.setInterface(XxxService.class);
  reference.setVersion("1.0.0");
  
  // 如果点对点直连，可以用reference.setUrl()指定目标地址，设置url后将绕过注册中心，
  ReferenceConfig<XxxService> reference = new ReferenceConfig<XxxService>();
  // 其中，协议对应provider.setProtocol()的值，端口对应provider.setPort()的值，
  // 路径对应service.setPath()的值，如果未设置path，缺省path为接口名
  reference.setUrl("dubbo://10.20.130.230:20880/com.xxx.XxxService"); 
   
  // 和本地bean一样使用xxxService
  XxxService xxxService = reference.get();
  // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
  ```

##### 04：注解配置【使用方便】

###### 服务提供方

- Service注解暴露服务

```java
@Service
public class AnnotationServiceImpl implements AnnotationService {
    @Override
    public String sayHello(String name) {
        return "annotation: hello, " + name;
    }
}
```

- 增加应用共享配置

```properties
# dubbo-provider.properties
dubbo.application.name=annotation-provider
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
```

- 指定Spring扫描路径

```java
@Configuration
@EnableDubbo(scanBasePackages = "org.apache.dubbo.samples.simple.annotation.impl")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public static class ProviderConfiguration {
       
}
```

###### 服务消费方

- Reference 注解引用服务

```java
@Component("annotationAction")
public class AnnotationAction {
    @Reference
    private AnnotationService annotationService;
    
    public String doSayHello(String name) {
        return annotationService.sayHello(name);
    }
}
```

- 增加应用共享配置

```properties
# dubbo-consumer.properties
dubbo.application.name=annotation-consumer
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.consumer.timeout=3000
```

- 指定Spring扫描路径

```java
@Configuration
@EnableDubbo(scanBasePackages = "org.apache.dubbo.samples.simple.annotation.action")
@PropertySource("classpath:/spring/dubbo-consumer.properties")
@ComponentScan(value = {"org.apache.dubbo.samples.simple.annotation.action"})
public static class ConsumerConfiguration {

}
```

- 调用服务

```java
public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext context = 
      new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
    context.start();
    final AnnotationAction annotationAction =
      (AnnotationAction) context.getBean("annotationAction");
    String hello = annotationAction.doSayHello("world");
}
```

##### 05：SpringBoot 配置 【类似属性配置】

```properties
  ## application.properties
  # Spring boot application
  spring.application.name=dubbo-externalized-configuration-provider-sample
  # Base packages to scan Dubbo Component: @com.alibaba.dubbo.config.annotation.Service
  dubbo.scan.base-packages=com.alibaba.boot.dubbo.demo.provider.service
  # Dubbo Application
  ## The default value of dubbo.application.name is ${spring.application.name}
  ## dubbo.application.name=${spring.application.name}
  # Dubbo Protocol
  dubbo.protocol.name=dubbo
  dubbo.protocol.port=12345
  ## Dubbo Registry
  dubbo.registry.address=N/A
  ## DemoService version
  demo.service.version=1.0.0
```

##### 06：动态配置中心【重点】

配置中心（v2.7.0）在Dubbo中承担**两个职责**：

1. **外部化配置**。启动配置的集中式存储 （简单理解为dubbo.properties的外部化存储）。
2. **服务治理**。服务治理规则的存储与通知。
   - 为了兼容2.6.x版本配置，在使用Zookeeper作为注册中心，且没有显示配置配置中心的情况下，Dubbo框架会默认将此Zookeeper用作配置中心，但将只作服务治理用途。

###### 三种启用方式

```properties
<dubbo:config-center address="zookeeper://127.0.0.1:2181"/>

dubbo.config-center.address=zookeeper://127.0.0.1:2181

ConfigCenterConfig configCenter = new ConfigCenterConfig();
configCenter.setAddress("zookeeper://127.0.0.1:2181");
```

###### 外部化部署

- 外部化配置和其他本地配置在内容和格式上并无区别，可以简单理解为`dubbo.properties`的外部化存储，配置中心更适合将一些公共配置如注册中心、元数据中心配置等抽取以便做集中管理。

- ```properties
  # 将注册中心地址、元数据中心地址等配置集中管理，可以做到统一环境、减少开发侧感知。
  dubbo.registry.address=zookeeper://127.0.0.1:2181
  dubbo.registry.simplified=true
  
  dubbo.metadata-report.address=zookeeper://127.0.0.1:2181
  
  dubbo.protocol.name=dubbo
  dubbo.protocol.port=20880
  
  dubbo.application.qos.port=33333
  ```

- 优先级

  - 外部化配置默认较本地配置有更高的优先级，因此这里配置的内容会覆盖本地配置值，你也可通过以下**选项调整配置中心的优先级**：

```properties
-Ddubbo.config-center.highest-priority=false
```

- 作用域

  - 外部化配置有**全局和应用**两个级别，全局配置是所有应用共享的，应用级配置是由每个应用自己维护且只对自身可见的。
  - 当前已支持的扩展实现有Zookeeper、Apollo。

- **Zookeeper【示例】**

  - ```xml
    <dubbo:config-center address="zookeeper://127.0.0.1:2181"/>
    ```

  - 默认所有的配置都存储在`/dubbo/config`节点，具体节点结构图依次降级：

    1. namespace：用于不同配置的环境隔离。
    2. config：Dubbo约定的固定节点，不可更改，所有配置和服务治理规则都存储在此节点下。
    3. dubbo/application：分别用来隔离全局配置、应用级别配置：dubbo是默认group值，application对应应用名
    4. dubbo.properties：此节点的node value存储具体配置内容

- **自己加载外部化配置**

  - 所谓Dubbo对配置中心的支持：本质上就是把`.properties`从远程拉取到本地，然后和本地的配置做一次融合。理论上只要Dubbo框架能拿到需要的配置就可以正常的启动，它并不关心这些配置是自己加载到的还是应用直接塞给它的。

###### 服务治理

- Zookeeper 默认的节点结构
  - namespace：用于不同配置的环境隔离。
  - config：Dubbo约定的固定节点，不可更改，所有配置和服务治理规则都存储在此节点下。
  - dubbo：所有服务治理规则都是全局性的，dubbo为默认节点。
  - configurators/tag-router/condition-router：不同的服务治理规则类型，node value存储具体规则内容。

##### 07：配置加载流程

- 在**应用启动阶段，Dubbo 框架如何将所需要的配置采集起来**（包括应用配置、注册中心配置、服务配置等），以完成服务的暴露和引用流程。
- Dubbo的配置读取总体上遵循了以下几个原则：
  - Dubbo支持了多层级的配置，并按**预定优先级自动实现配置间的覆盖**，最终**所有配置汇总到数据总线URL**后驱动后续的服务暴露、引用等流程；
  - ApplicationConfig、ServiceConfig、ReferenceConfig可以被理解成配置来源的一种，是**直接面向用户编程的配置采集方式**；
  - 配置格式以Properties为主，在配置内容上遵循约定的`path-based`的命名规范；
- 配置来源
  - 默认有四种配置来源，配置覆盖关系的优先级从上到下依次降低：
    - JVM System Properties，-D参数
    - Externalized Configuration，外部化配置
    - ServiceConfig、ReferenceConfig等编程接口采集的配置
    - 本地配置文件dubbo.properties

##### 08：自动加载环境变量

- Dubbo会**自动从约定key中读取配置，并将配置以Key-Value的形式写入到URL中**，支持的key有以下两个：

  1. `dubbo.labels`：指定一些列配置到URL中的键值对，通常通过JVM -D或系统环境变量指定。

     ```properties
     # JVM
     -Ddubbo.labels = "tag1=value1; tag2=value2"
     
     # 环境变量
     DUBBO_LABELS = "tag1=value1; tag2=value2"
     # 最终生成的URL会包含 tag1、tag2 两个 key: dubbo://xxx?tag1=value1&tag2=value2
     ```

  2. `dubbo.env.keys`：指定环境变量key值，Dubbo会尝试从环境变量加载每个 key

     ```properties
     # JVM
     -Ddubbo.env.keys = "DUBBO_TAG1, DUBBO_TAG2"
     
     # 环境变量
     DUBBO_ENV_KEYS = "DUBBO_TAG1, DUBBO_TAG2"
     # 最终生成的URL会包含 DUBBO_TAG1、DUBBO_TAG2 两个 key:
     # dubbo://xxx?DUBBO_TAG1=value1&DUBBO_TAG2=value2
     ```







