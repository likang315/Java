### 安装Kafka

------

##### 01：Broker配置

###### 常规设置

- broker.id
  - 每个broker 唯一标识符，使用broker.id 来表示。它的默认值是0，也可以被设置成其他任意整数。这个值在整个Kafka 集群里必须是唯一的；
- port
  - 如果使用配置样本来启动Kafka，它会监听9092 端口；
- zookeeper.connect
  - 用于保存broker 元数据的Zookeeper 地址是通过zookeeper.connect 来指定的。
  - 该配置参数是用冒号分隔的一组hostname:port/path 列表，每一部分的含义如下：
    - hostname 是Zookeeper 服务器的机器名或IP 地址；
    - port 是Zookeeper 的客户端连接端口；
    - /path 是可选的Zookeeper 路径，作为Kafka 集群的chroot 环境。如果不指定，默认使用根路径。如果指定的chroot 路径不存在，broker 会在启动的时候创建它。
- log.dirs
  - Kafka 把所有消息都保存在磁盘上，**存放这些日志片段的目录是通过log.dirs 指定的**。它是一组用逗号分隔的本地文件系统路径。如果指定了多个路径，那么broker 会根据“**最少使用**”原则，把同一个分区的日志片段保存到同一个路径下;
- num.recovery.hreads.per.data.dir
  - Kafka 会使用可配置的线程池来处理日志片段：
    - 服务器正常启动，用于打开每个分区的日志片段；
    - 服务器崩溃后重启，用于检查和截短每个分区的日志片段；
    - 服务器正常关闭，用于关闭日志片段。
  - 所配置的数字对应的是log.dirs 指定的单个日志目录对应的并发度；
- auto.create.topics.enable
  - 默认情况下，Kafka 会在如下几种情形下自动创建主题，隐式创建topic；
    - 当一个生产者开始往主题写入消息时；
    - 当一个消费者开始从主题读取消息时；
    - 当任意一个客户端向主题发送元数据请求时。

###### 主题的默认配置

- 

