### Dubbo-schema

------

[TOC]

所有配置项分为三大类

- **服务发现**：表示该配置项用于**服务的注册与发现**，目的是让消费方找到提供方。
- **服务治理**：表示该配置项用于**治理服务间的关系**，或为开发测试提供便利条件。
- **性能调优**：表示该配置项用于**调优性能**，不同的选项对性能会产生影响。
- **所有配置最终都将转换为 URL 表示，并由服务提供方生成，经注册中心传递给消费方，各属性对应 URL 的参数。**
  - 注意：**只有 group，interface，version 是服务的匹配条件，三者决定是不是同一个服务**，其它配置项均为调优和治理参数；
  - URL 格式：protocol://username:password@host:port/path?key=value&key=value

##### 01：dubbo:service

- 服务提供者**暴露服务**配置。对应的配置类：`org.apache.dubbo.config.ServiceConfig`

- ```xml
  <dubbo:service interface="com.crm.data.cube.api.CrmDataDubboService" 
                 group="app-group-name"
                 ref="queryDataDubboService"
                 version="1.0.0"
                 timeout="500"
                 registry="provider-registry" 
                 protocol="provider-protocol"/>
  ```
  
- | 属性          | 对应URL参数       | 类型           | 是否必填 | 缺省值【默认值】           | 作用     | 描述                                                         |
  | ------------- | ----------------- | -------------- | -------- | -------------------------- | -------- | ------------------------------------------------------------ |
  | **interface** |                   | class          | **必填** |                            | 服务发现 | 服务接口名                                                   |
  | **ref**       |                   | object         | **必填** |                            | 服务发现 | **服务对象实例引用**                                         |
  | **version**   | version           | string         | 可选     | 0.0.0                      | 服务发现 | 服务版本，建议使用**两位数字版本**，如：1.0，通常在接口不兼容时版本号才需要升级 |
  | group         | group             | string         | 可选     |                            | 服务发现 | 服务分组，当一个接口有多个实现，可以用分组区分               |
  | path          | <path>            | string         | 可选     | 缺省为接口名               | 服务发现 | 服务路径 (注意：1.0不支持自定义路径，总是使用接口名，如果有1.0调2.0，配置服务路径可能不兼容) |
  | delay         | delay             | int            | 可选     | 0                          | 性能调优 | **延迟注册服务时间(毫秒)** ，设为-1时，表示延迟到Spring容器初始化完成时暴露服务 |
  | **timeout**   | timeout           | int            | 可选     | 1000                       | 性能调优 | 远程服务调用超时时间(毫秒)                                   |
  | **retries**   | retries           | int            | 可选     | 2                          | 性能调优 | 远程服务**调用重试次数**，不包括第一次调用，不需要重试请设为0 |
  | connections   | connections       | int            | 可选     | 100                        | 性能调优 | 对每个提供者的**最大连接数**，rmi、http、hessian等短连接协议表示限制连接数，dubbo等长连接协表示建立的长连接个数 |
  | loadbalance   | loadbalance       | string         | 可选     | random                     | 性能调优 | **负载均衡策略**，可选值：random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用 |
  | **registry**  |                   | string         | 可选     | 缺省向所有registry注册     | 配置关联 | 向指定注册中心注册，在多个注册中心时使用，值为<dubbo:registry>的id属性，多个注册中心ID用逗号分隔，如果不想将该服务注册到任何registry，可将值设为N/A |
  | provider      |                   | string         | 可选     | 缺省使用第一个provider配置 | 配置关联 | 指定provider，值为<dubbo:provider>的id属性                   |
  | dynamic       | dynamic           | boolean        | 可选     | true                       | 服务治理 | 服务是否动态注册，如果设为false，注册后将显示后disable状态，需人工启用，并且服务提供者停止时，也不会自动取消册，需人工禁用。 |
  | **accesslog** | accesslog         | string/boolean | 可选     | false                      | 服务治理 | 设为true，将向logger中输出访问日志，也可填写访问日志文件路径，直接把访问日志输出到指定文件 |
  | weight        | weight            | int            | 可选     |                            | 性能调优 | 服务权重                                                     |
  | executes      | executes          | int            | 可选     | 0                          | 性能调优 | 服务提供者每服务每方法最大可并行执行请求数                   |
  | cluster       | cluster           | string         | 可选     | failover                   | 性能调优 | 集群方式，可选：failover/failfast/failsafe/failback/forking  |
  | filter        | service.filter    | string         | 可选     | default                    | 性能调优 | 服务提供方远程调用过程拦截器名称，多个名称用逗号分隔         |
  | listener      | exporter.listener | string         | 可选     | default                    | 性能调优 | 服务提供方导出服务监听器名称，多个名称用逗号分隔             |
  | **protocol**  |                   | string         | 可选     |                            | 配置关联 | 使用**指定的协议暴露服务**，在多协议时使用，值为<dubbo:protocol>的id属性，多个协议ID用逗号分隔 |

##### 02：dubbo:reference

- 服务消费者**引用服务**配置，对应的配置类： `org.apache.dubbo.config.ReferenceConfig`

- ```xml
  <dubbo:reference interface="com.crm.data.cube.api.CrmDataDubboService"
                   registry="h_crm_data_merchantman_registry"
                   version="1.0"
                   id="crmDataDubboService"
                   check="false" />
  ```
  
- | 属性          | 对应URL参数      | 类型           | 是否必填 | 缺省值                                   | 作用     | 描述                                                         |
  | ------------- | ---------------- | -------------- | -------- | ---------------------------------------- | -------- | ------------------------------------------------------------ |
  | **id**        |                  | string         | **必填** |                                          | 配置关联 | 服务引用BeanId                                               |
  | **interface** |                  | class          | **必填** |                                          | 服务发现 | 服务接口名                                                   |
  | **version**   | version          | string         | 可选     |                                          | 服务发现 | 服务版本，与服务提供者的版本一致                             |
  | group         | group            | string         | 可选     |                                          | 服务发现 | **服务分组**，当一个接口有多个实现，可以用分组区分, 调用哪个是实现类的服务 |
  | timeout       | timeout          | long           | 可选     | 缺省使用<dubbo:consumer>的timeout        | 性能调优 | 服务方法调用超时时间(毫秒)                                   |
  | retries       | retries          | int            | 可选     | 缺省使用<dubbo:consumer>的retries        | 性能调优 | 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0    |
  | connections   | connections      | int            | 可选     | 缺省使用<dubbo:consumer>的connections    | 性能调优 | 对每个提供者的最大连接数，rmi、http、hessian等短连接协议表示限制连接数，dubbo等长连接协表示建立的长连接个数 |
  | loadbalance   | loadbalance      | string         | 可选     | 缺省使用<dubbo:consumer>的loadbalance    | 性能调优 | 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：**随机，轮询，最少活跃调用** |
  | **check**     | check            | boolean        | 可选     | 缺省使用<dubbo:consumer>的check          | 服务治理 | **启动时检查提供者是否存在，true报错，false忽略**            |
  | **url**       | url              | string         | 可选     |                                          | 服务治理 | **点对点直连服务提供者地址，将绕过注册中心**                 |
  | cache         | cache            | string/boolean | 可选     |                                          | 服务治理 | **以调用参数为key，缓存返回结果**，可选：lru, threadlocal, jcache等 |
  | **registry**  |                  | string         | 可选     | 缺省将从所有注册中心获服务列表后合并结果 | 配置关联 | 从**指定注册中心注册获取服务列表，在多个注册中心时使用，值为<dubbo:registry>的id属性，多个注册中心ID用逗号分隔** |
  | actives       | actives          | int            | 可选     | 0                                        | 性能调优 | 每服务消费者每服务每方法最大并发调用数                       |
  | cluster       | cluster          | string         | 可选     | failover                                 | 性能调优 | 集群方式，可选：failover/failfast/failsafe/failback/forking  |
  | filter        | reference.filter | string         | 可选     | default                                  | 性能调优 | 服务消费方远程调用过程拦截器名称，多个名称用逗号分隔         |
  | listener      | invoker.listener | string         | 可选     | default                                  | 性能调优 | 服务消费方引用服务监听器名称，多个名称用逗号分隔             |
  | **protocol**  | protocol         | string         | 可选     |                                          | 服务治理 | **只调用指定协议的服务提供方，其它协议忽略。**               |

##### 03：dubbo:protocol

- **服务提供者协议配置**。对应的配置类： `org.apache.dubbo.config.ProtocolConfig`

- 如果需要支持多协议，可以声明多个 `<dubbo:protocol>` 标签，并在 `<dubbo:service>` 中通过 `protocol` 属性指定使用的协议。

- ```xml
  <dubbo:protocol id="provider-protocol" name="dubbo" port="20209" 
                  serialization="hessian2"/>
  ```

- | 属性              | 对应URL参数   | 类型           | 是否必填 | 缺省值                                                       | 作用     | 描述                                                         |
  | ----------------- | ------------- | -------------- | -------- | ------------------------------------------------------------ | -------- | ------------------------------------------------------------ |
  | **id**            |               | string         | 可选     | dubbo                                                        | 配置关联 | 协议BeanId，可以在<dubbo:service protocol="">中引用此ID，如果ID不填，缺省和name属性值一样，重复则在name后加序号。 |
  | **name**          | <protocol>    | string         | **必填** | dubbo                                                        | 性能调优 | **协议名称**                                                 |
  | **port**          | <port>        | int            | 可选     | dubbo协议缺省端口为20880，rmi协议缺省端口为1099，http和hessian协议缺省端口为80；如果**没有**配置port，则自动采用默认端口，如果配置为**-1**，则会分配一个没有被占用的端口。 | 服务发现 | 服务端口                                                     |
  | **serialization** | serialization | string         | 可选     | dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json | 性能调优 | **协议序列化方式**，当协议支持多种序列化方式时使用，比如：dubbo协议的dubbo,hessian2,java,compactedjava，以及http协议的json等 |
  | threadpool        | threadpool    | string         | 可选     | fixed                                                        | 性能调优 | 线程池类型，可选：fixed/cached                               |
  | threads           | threads       | int            | 可选     | 200                                                          | 性能调优 | 服务线程池大小(固定大小)                                     |
  | iothreads         | threads       | int            | 可选     | cpu个数+1                                                    | 性能调优 | io线程池大小(固定大小)                                       |
  | accepts           | accepts       | int            | 可选     | 0                                                            | 性能调优 | 服务提供方最大可接受连接数                                   |
  | payload           | payload       | int            | 可选     | 8388608(=8M)                                                 | 性能调优 | 请求及响应数据包大小限制，单位：字节                         |
  | accesslog         | accesslog     | string/boolean | 可选     |                                                              | 服务治理 | 设为true，将向logger中输出访问日志，也可填写访问日志文件路径，直接把访问日志输出到指定文件 |
  | path              | <path>        | string         | 可选     |                                                              | 服务发现 | 提供者上下文路径，为服务path的前缀                           |
  | **register**      | register      | boolean        | 可选     | true                                                         | 服务治理 | **该协议的服务是否注册到注册中心**                           |

##### 04：dubbo:registry

- **注册中心配置**。对应的配置类： `org.apache.dubbo.config.RegistryConfig`。

- 如果有多个不同的注册中心，可以声明多个 `<dubbo:registry>` 标签，并在 `<dubbo:service>` 或 `<dubbo:reference>` 的 `registry` 属性指定使用的注册中心。

- ```xml
  <dubbo:registry id="provider-registry" group="h_crm_data_provider" adress="zk.xxx"/>
  ```

- | 属性          | 对应URL参数          | 类型    | 是否必填 | 缺省值 | 作用     | 描述                                                         |
  | ------------- | -------------------- | ------- | -------- | ------ | -------- | ------------------------------------------------------------ |
  | **id**        |                      | string  | 可选     |        | 配置关联 | 注册中心引用BeanId，可以在<dubbo:service registry="">或<dubbo:reference registry="">中引用此ID |
  | **address**   | <host:port>          | string  | **必填** |        | 服务发现 | **注册中心服务器地址**，如果地址没有端口**缺省为9090**，同一集群内的多个地址用逗号分隔，如：ip:port,ip:port，不同集群的注册中心，请配置多个<dubbo:registry>标签 |
  | **protocol**  | <protocol>           | string  | 可选     | dubbo  | 服务发现 | **注册中心地址协议**，支持`dubbo`, `multicast`, `zookeeper`, `redis`, `consul(2.7.1)`, `sofa(2.7.2)`, `etcd(2.7.2)`, `nacos(2.7.2)`等协议 |
  | port          | <port>               | int     | 可选     | 9090   | 服务发现 | 注册中心缺省端口，当address没有带端口时使用此端口做为缺省值  |
  | **username**  | <username>           | string  | 可选     |        | 服务治理 | 登录注册中心用户名，如果注册中心不需要验证可不填             |
  | **password**  | <password>           | string  | 可选     |        | 服务治理 | 登录注册中心密码，如果注册中心不需要验证可不填               |
  | transport     | registry.transporter | string  | 可选     | netty  | 性能调优 | 网络传输方式，可选mina,netty                                 |
  | timeout       | registry.timeout     | int     | 可选     | 5000   | 性能调优 | 注册中心请求超时时间(毫秒)                                   |
  | **file**      | registry.file        | string  | 可选     |        | 服务治理 | **使用文件缓存注册中心地址列表及服务提供者列表，应用重启时将基于此文件恢复，注意：两个注册中心不能使用同一文件存储** |
  | **register**  | register             | boolean | 可选     | true   | 服务治理 | 是否向此注册中心注册服务**，如果设为false，将只订阅，不注册** |
  | **subscribe** | subscribe            | boolean | 可选     | true   | 服务治理 | 是否向此注册中心订阅服务，**如果设为false，将只注册，不订阅** |
  | dynamic       | dynamic              | boolean | 可选     | true   | 服务治理 | **服务是否动态注册**，如果设为false，注册后将显示为disable状态，需人工启用，并且服务提供者停止时，也不会自动取消注册，需人工禁用。 |
  | **group**     | group                | string  | 可选     | dubbo  | 服务治理 | 服务注册分组，**跨组的服务不会相互影响，也无法相互调用，适用于环境隔离**。 |

##### 05：dubbo:monitor

- **监控中心配置**。对应的配置类： `org.apache.dubbo.config.MonitorConfig`

- | 属性     | 对应URL参数 | 类型   | 是否必填 | 缺省值 | 作用     | 描述                                                         |
  | -------- | ----------- | ------ | -------- | ------ | -------- | ------------------------------------------------------------ |
  | protocol | protocol    | string | 可选     | dubbo  | 服务治理 | 监控中心协议，如果为protocol="registry"，表示**从注册中心发现监控中心地址**，否则直连监控中心。 |
  | address  | <url>       | string | 可选     | N/A    | 服务治理 | 直连监控中心服务器地址，address="10.20.130.230:12080"        |

##### 06：dubbo:application

- 应用信息配置。对应的配置类：`org.apache.dubbo.config.ApplicationConfig`

- ```xml
  <dubbo:application name="data-cube-provider"/>
  ```

- | 属性         | 对应URL参数         | 类型   | 是否必填 | 缺省值    | 作用     | 描述                                                         |
  | ------------ | ------------------- | ------ | -------- | --------- | -------- | ------------------------------------------------------------ |
  | name         | application         | string | **必填** |           | 服务治理 | 当前应用名称，用于注册中心计算应用间依赖关系，注意：消费者和提供者应用名不要一样，此参数不是匹配条件，你当前项目叫什么名字就填什么 |
  | version      | application.version | string | 可选     |           | 服务治理 | 当前应用的版本                                               |
  | organization | organization        | string | 可选     |           | 服务治理 | 组织名称(BU或部门)，用于注册中心区分服务来源                 |
  | architecture | architecture        | string | 可选     |           | 服务治理 | 用于服务分层对应的架构。                                     |
  | environment  | environment         | string | 可选     |           | 服务治理 | 应用环境，如：develop/test/product，不同环境使用不同的缺省值 |
  | compiler     | compiler            | string | 可选     | javassist | 性能优化 | Java字节码编译器，用于动态类的生成，可选：jdk或javassist     |
  | **logger**   | logger              | string | 可选     | slf4j     | 性能优化 | 日志输出方式，可选：slf4j,jcl,log4j,log4j2,jdk               |

##### 07：dubbo:provider

- 服务提供者缺省值配置。对应的配置类： `org.apache.dubbo.config.ProviderConfig`。

- 同时该标签为 `<dubbo:service>` 和 `<dubbo:protocol>` 标签的缺省值设置。

  ```xml
  <dubbo:service proivder=""> 
  ```

##### 08：dubbo:consumer

- 服务消费者缺省值配置。配置类： `org.apache.dubbo.config.ConsumerConfig` 。
- 同时该标签为 `<dubbo:reference>` 标签的缺省值设置。

##### 09：dubbo:method

- 方法级配置。对应的配置类： `org.apache.dubbo.config.MethodConfig`。

- 同时该标签为 `<dubbo:service>` 或 `<dubbo:reference>` 的子标签，用于控制到方法级。

- ```xml
  <dubbo:reference interface="com.xxx.XxxService">
  	<dubbo:method name="findXxx" timeout="3000" retries="2" />
  </dubbo:reference>
  ```

##### 10：dubbo:argument

- 方法参数配置。对应的配置类： `org.apache.dubbo.config.ArgumentConfig`。
- 该标签为 `<dubbo:method>` 的子标签，用于方法参数的特征描述。

```xml
<dubbo:method name="findXxx" timeout="3000" retries="2">
  	<!--如果为callback接口，服务提供方将生成反向代理，可以从服务提供方反向调用消费方，用于事件推送-->
    <dubbo:argument index="0" callback="true" />
</dubbo:method>
```

##### 11：dubbo:config-center

- **配置中心**。对应的配置类：`org.apache.dubbo.config.ConfigCenterConfig`

- | 属性             | 对应URL参数            | 类型    | 是否必填 | 缺省值           | 描述                                                         |
  | ---------------- | ---------------------- | ------- | -------- | ---------------- | ------------------------------------------------------------ |
  | protocol         | config.protocol        | string  | 可选     | zookeeper        | 使用哪个配置中心：apollo、zookeeper、nacos等。 以zookeeper为例 1. 指定protocol，则address可以简化为`127.0.0.1:2181`； |
  | address          | config.address         | string  | 必填     |                  | 配置中心地址。                                               |
  | highest-priority | config.highestPriority | boolean | 可选     | true             | 来自配置中心的配置项具有最高优先级，即会覆盖本地配置项。     |
  | namespace        | config.namespace       | string  | 可选     | dubbo            | 通常用于多租户隔离，实际含义视具体配置中心而不同。 如： zookeeper - 环境隔离，默认值`dubbo`； |
  | cluster          | config.cluster         | string  | 可选     |                  | 含义视所选定的配置中心而不同。 如Apollo中用来区分不同的配置集群 |
  | check            | config.check           | boolean | 可选     | true             | 当配置中心连接失败时，是否终止应用启动。                     |
  | config-file      | config.configFile      | string  | 可选     | dubbo.properties | 全局级配置文件所映射到的key zookeeper - 默认路径/dubbo/config/dubbo/dubbo.properties |
  | timeout          | config.timeout         | integer |          | 3000ms           | 获取配置的超时时间                                           |













