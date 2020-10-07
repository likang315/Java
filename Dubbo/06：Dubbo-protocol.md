###  Dubbo-protocol

------

```xml
<dubbo:protocol id="provider-protocol" name="dubbo" port="20209"
                serialization="hessian2"/>
```

##### 01：dubbo://【使用】

###### 特性

- 缺省协议，使用基于 mina `1.1.7` 和 hessian `3.2.1` 的 tbremoting 交互。
  - 连接个数：单连接
  - 连接方式：长连接
  - 传输协议：TCP
  - 传输方式：NIO 异步传输
  - 序列化：Hessian 二进制序列化
  - 适用范围：传入传出参数**数据包较小（建议小于100K），消费者比提供者个数多**，单一消费者无法压满提供者，尽量不要用 dubbo 协议传输大文件或超大字符串。
  - 适用场景：常规远程服务方法调用

###### 约束

- 参数及返回值**需实现 `Serializable` 接口**

- 参数及返回值**不能自定义实现 `List`, `Map`, `Number`, `Date`, `Calendar` 等接口**，只能用 JDK 自带的实现，因为 hessian 会做特殊处理，自定义实现类中的属性值都会丢失。

- Hessian 序列化，只传成员属性值和值的类型。

- | 数据通讯 | 情况                                           | 结果                                           |
  | -------- | ---------------------------------------------- | ---------------------------------------------- |
  | A->B     | 类A多一种 属性                                 | 不抛异常，A多的那 个属性的值，B没有， 其他正常 |
  | A->B     | 枚举A多一种 枚举，A使用多 出来的枚举进行传输   | 抛异常                                         |
  | A->B     | 枚举A多一种 枚举，A不使用 多出来的枚举进行传输 | 不抛异常，B正常接 收数据                       |
  | A->B     | A和B的属性 名相同，但类型不相同                | 抛异常                                         |
  | A->B     | serialId 不相同                                | 正常传输                                       |

- 服务器端和客户端对领域对象并不需要完全一致，而是按照最大匹配原则。

###### 配置

- 配置协议：

  ```xml
  <dubbo:protocol name="dubbo" port="20880" />
  ```

- 多连接配置：

  - Dubbo 协议缺省**每服务每提供者每消费者使用单一长连接**，如果数据量较大，可以使用多个连接。

  ```xml
  <dubbo:service connections="1"/>
  <dubbo:reference connections="1"/>
  ```

  - `<dubbo:service connections="0">` 或 `<dubbo:reference connections="0">` 表示该服务使用 JVM 共享长连接。**缺省**
  - `<dubbo:service connections="1">` 或 `<dubbo:reference connections="1">` 表示该服务使用独立长连接。
  - `<dubbo:service connections="2">` 或`<dubbo:reference connections="2">` 表示该服务使用独立两条长连接。

###### 常见问题

- 为什么要消费者比提供者个数多?
  - 因 dubbo 协议采用单一长连接，理论上 1 个服务提供者需要 20 个服务消费者才能压满网卡。
- 为什么不能传大包?
  - 因 dubbo 协议采用单一长连接，会撑爆服务，如果能接受，可以考虑使用，否则网络将成为瓶颈。
- 为什么采用异步单一长连接?
  - 因为服务的现状大都是服务提供者少，通常只有几台机器，而服务的消费者多，可能整个网站都在访问该服务，服务提供者很容易就被压跨，通过单一连接，保证单一消费者不会压死提供者，长连接，减少连接握手验证等，并使用异步 IO，复用线程池，防止 C10K 问题。

##### 02：hessian://

- hessian协议：用于集成 Hessian 的服务，Hessian 底层**采用 Http 通讯，采用 Servlet 暴露服务**，Dubbo 缺省内嵌 Jetty 作为服务器实现。
- Dubbo 的 Hessian 协议可以和原生 Hessian 服务互操作【等价】。

###### 特性

- 连接个数：多连接
- 连接方式：短连接
- 传输协议：HTTP
- 传输方式：同步传输
- 序列化：Hessian二进制序列化
- 适用范围：传入传出参数**数据包较大，提供者比消费者个数多**，提供者压力较大，可传文件。
- 适用场景：页面传输，文件传输，或与原生hessian服务互操作

###### 依赖

```xml
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
    <version>4.0.7</version>
</dependency>
```

###### 约束

- 参数及返回值需实现 `Serializable` 接口
- 参数及返回值不能自定义实现 `List`, `Map`, `Number`, `Date`, `Calendar` 等接口，只能用 JDK 自带的实现，因为 hessian 会做特殊处理，自定义实现类中的属性值都会丢失。

###### 配置

- 定义 hessian 协议：

- ```xml
  <dubbo:protocol name="hessian" port="8080" server="jetty" />
  ```

- 直连

- ```xml
  <dubbo:reference id="helloService" interface="HelloWorld"
                   url="hessian://10.20.153.10:8080/helloWorld" />
  ```

##### 03：http://

- 基于 HTTP 表单的远程调用协议，采用 Spring 的 HttpInvoker 实现

###### 特性

- 连接个数：多连接
- 连接方式：短连接
- 传输协议：HTTP
- 传输方式：同步传输
- 序列化：表单序列化
- 适用范围：传入传出参数**数据包大小混合，提供者比消费者个数多**，可用浏览器查看，可用表单或URL传入参数，暂不支持传文件。
- 适用场景：需同时给应用程序和浏览器 JS 使用的服务。

###### 约束

- 参数及返回值需符合 Bean 规范

##### 配置

- 配置协议：

- ```xml
  <dubbo:protocol name="http" port="8080" />
  ```

