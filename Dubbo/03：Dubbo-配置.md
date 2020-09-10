### Dubbo 配置

------

##### 01：xml配置

###### 配置标签

| 标签                   | 用途         | 解释                                                         |
| ---------------------- | ------------ | ------------------------------------------------------------ |
| `<dubbo:service/>`     | 服务配置     | 用于**暴露一个服务**，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心 |
| `<dubbo:reference/>`   | 引用配置     | 用于创建一个**远程服务代理**，一个引用可以指向多个注册中心   |
| `<dubbo:protocol/>`    | 协议配置     | 用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受 |
| `<dubbo:application/>` | 应用配置     | 用于配置当前应用信息，不管该应用是提供者还是消费者           |
| `<dubbo:module/>`      | 模块配置     | 用于配置当前模块信息，可选                                   |
| `<dubbo:registry/>`    | 注册中心配置 | 用于**配置连接注册中心**相关信息                             |
| `<dubbo:monitor/>`     | 监控中心配置 | 用于配置连接监控中心相关信息，可选                           |
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

- 引用缺省是延迟初始化的，只有引用被注入到其它 Bean，或被 `getBean()` 获取，才会初始化。如果需要饥饿加载，即没有人引用也立即生成动态代理，可以配置：**<dubbo:reference ... init="true" />**

##### 02：属性配置（dubbo.properties文件）

- 如果你的应用足够简单，例如，不需要多注册中心或多协议，并且需要在spring容器中共享配置，那么，我们可以直接使用 `dubbo.properties`作为默认配置。
- Dubbo可以自动加载classpath根目录下的dubbo.properties，也可以使用JVM参数来指定路径：
  - -Ddubbo.properties.file=xxx.properties

###### 映射规则

- 可以将xml的tag名和属性名组合起来，用‘.’分隔。每行一个属性。

  - `dubbo.application.name=foo` 相当于 `<dubbo:application name="foo" />`

- 如果在xml配置中有超过一个的tag，那么你可以使用‘id’进行区分。如果你不指定id，它将作用于所有tag。

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

##### 03：API 配置

- http://dubbo.apache.org/zh-cn/docs/user/configuration/api.html

##### 04：注解配置