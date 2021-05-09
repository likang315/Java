### OPS

------

[TOC]

##### 01：Telnet 命令

- 从 `2.0.5` 版本开始，dubbo 开始支持通过 telnet 命令来进行服务治理。

##### 02：使用

```sh
# 查看某个机器上某个端口是否能访问
telnet localhost 22222
```

或者：

```sh
echo status | nc -i 1 localhost 20880
```

##### 03：端口

- 新版本的 telnet 端口 与 dubbo 协议的端口是不同的端口，默认为 `22222`，可通过配置文件`dubbo.properties` 修改:dubbo.application.qos.port=33333

##### 04：安全

- 默认情况下，dubbo 接收任何主机发起的命令，可通过配置文件`dubbo.properties` 修改:

```properties
# 拒绝远端主机发出的命令，只允许服务本机执行
dubbo.application.qos.accept.foreign.ip=false
```

##### 05：ls 列出消费者和提供者

- 列出 dubbo 所提供的服务和消费的服务，以及消费的服务地址数

##### 06：Online 上线服务命令

- 当线上的 QPS 比较高的时候，当刚重启机器的时候，由于没有进行JIT 预热或相关资源没有预热，可能会导致大量超时，这个时候，可通过**分批发布服务，逐渐加大流量**

- ```sh
  // 上线所有服务
  dubbo>online
  OK
  // 根据正则，上线部分服务
  dubbo>online com.*
  OK
  ```

##### 07：Offline

- 下线服务；

##### 08：启动参数

QoS提供了一些启动参数，来对启动进行配置，他们主要包括：

| 参数               | 说明              | 默认值 |
| ------------------ | ----------------- | ------ |
| qosEnable          | 是否启动QoS       | true   |
| qosPort            | 启动QoS绑定的端口 | 22222  |
| qosAcceptForeignIp | 是否允许远程访问  | false  |

###### 使用XML

```xml
<dubbo:application name="demo-provider">
  <dubbo:parameter key="qos.enable" value="true"/>
  <dubbo:parameter key="qos.accept.foreign.ip" value="false"/>
  <dubbo:parameter key="qos.port" value="33333"/>
</dubbo:application>
```

###### 使用springboot自动装配

- 可以在`application.properties`或者`application.yml`上配置:

```
dubbo.application.qosEnable=true
dubbo.application.qosPort=33333
dubbo.application.qosAcceptForeignIp=false
```