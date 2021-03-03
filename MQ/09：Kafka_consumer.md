##### Kafka Consumer

------

##### 01：kafka Consumer 概述

###### 消费者和消费者群组

- Kafka 消费者从属于消费者群组。一个群组里的消费者订阅的是同一个主题，每个消费者

  接收主题一部分分区的消息。

- 在数据进行比较耗时的计算的情况下，**单个消费者无法跟上数据生成的速度，所以可以增加更多的消费者，让它们分担负载，每个消费者只处理部分分区的消息**，这就是**横向伸缩**的主要手段。我们有必要为主题创建大量的分区，在负载增长时可以加入更多的消费者；

- ![ConsumerGroup](/Users/likang/Code/Git/Middleware/MQ/photos/ConsumerGroup.jpg)

###### 消费者群组和分区在均衡

- 一个**新的消费者加入群组时，它读取的是原本由其他消费者读取的消息**。当一个消费者被关闭或发生崩溃时，它就离开群组，原本**由它读取的分区将由群组里的其他消费者来读取**。在主题发生变化时，比如管理员添加了新的分区，会**发生分区重分配**；
- **再均衡：**分区的所有权从一个消费者转移到另一个消费者；
- 当分区被重新分配给另一个消费者时，**消费者当前的读取状态会丢失**，它有可能还需要去刷新缓存，在它重新恢复状态之前会拖慢应用程序；
- 消费者通过向被指派为**群组协调器的broker（不同的群组可以有不同的协调器）发送心跳**来维持它们和**群组的从属关系以及它们对分区的所有权关系**。只要消费者以正常的时间间隔发送心跳，就被认为是活跃的，说明它还在读取分区里的消息。消费者会在**轮询消息（为了获取消息）或提交偏移量时发送心跳**。如果消费者停止发送心跳的时间足够长，会话就会过期，**群组协调器认为它已经死亡，就会触发一次再均衡**。如果一个消费者发生崩溃，并停止读取消息，群组协调器会等待几秒钟，确认它死亡了才会触发再均衡。在这几秒钟时间里，死掉的消费者不会读取分区里的消息。在清理消费者时，消费者会通知协调器它将要离开群组，协调器会立即触发一次再均衡，尽量降低处理停顿；

###### 分配分区是怎样的一个过程

- 当消费者要加入群组时，它会向群组协调器发送一个JoinGroup 请求。第一个加入群组的消费者将成为**“群主”**。群主从协调器那里获得群组的成员列表（列表中包含了所有最近发送过心跳的消费者，它们被认为是活跃的），并负责给每一个消费者分配分区。它使用一个实现了PartitionAssignor 接口的类来决定哪些分区应该被分配给哪个消费者；
- 分配完毕之后，群主把分配情况列表发送给群组协调器，协调器再把这些信息发送给所有消费者。每个消费者只能看到自己的分配信息，只有群主知道群组里所有消费者的分配信息。

##### 02：创建Kafka消费者

- 在读取消息之前，需要先创建一个KafkaConsumer 对象，只需要使用 3 个必要的属性：

  - bootstrap.servers：broker 地址；
  - key.deserializer 和value.deserializer：将字节数组转换为java对象；
  - group.id：指定了KafkaConsumer 属于哪一个消费者群组；

- ```java
  Properties props = new Properties();
  props.put("bootstrap.servers", "broker1:9092,broker2:9092");
  props.put("group.id", "CountryCounter");
  props.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
  props.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
  KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
  ```

##### 03：订阅主题

- subscribe() 方法接受一个主题列表作为参数，使用起来很简单；

- ```java
  consumer.subscribe(Collections.singletonList("customerCountries"));
  ```

- 也可以在调用subscribe() 方法时传入一个正则表达式，在Kafka 和其他系统之间复制数据时，使用正则表达式的方式订阅多个主题是很常见的做法；

- ```java
  consumer.subscribe("test.*");
  ```

##### 04：轮询

- 一旦消费者订阅了主题，轮询（向服务器请求数据）就会处理所有的细节，包括**群组协调、分区再均衡、发送心跳和获取数据**，开发者只需要使用一组简单的API 来处理从分区返回的数据；

- ```java
  try {
    // 轮询
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        log.debug("topic = %s, partition = %s, offset = %d, customer = %s,
                  country = %s\n",
                  record.topic(), record.partition(), record.offset(),
                  record.key(), record.value());
        int updatedCount = 1;
        if (custCountryMap.countainsKey(record.value())) {
          updatedCount = custCountryMap.get(record.value()) + 1;
        }
        custCountryMap.put(record.value(), updatedCount);
        JSONObject json = new JSONObject(custCountryMap);
        System.out.println(json.toString(4))
      }
    }
  } finally {
    consumer.close();
  }
  ```

  - 这是一个无限循环。消费者实际上是一个长期运行的应用程序，它通过持续轮询向Kafka 请求数据;
  - 消费者必须**持续对Kafka 进行轮询，否则会被认为已经死亡，它的分区会被移交给群组里的其他消费者**。传给，poll() 方法的参数是一个超时时间，用于控制poll() 方法的阻塞时间（在消费者的缓冲区里没有可用数据时会发生阻塞）。如果该参数被设为0，poll() 会立即返回，否则它会在指定的毫秒数内一直等待broker 返回数据；
  - 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对；
  - 在退出应用程序之前使用**close() 方法关闭消费者**。网络连接和socket 也会随之关闭，并立即**触发一次再均衡；**

  ###### 线程安全

  - 按照规则，一个消费者使用一个线程。如果要在同一个消费者群组里运行多个消费者，需要让每个消费者运行在自己的线程里；
  - 例：**一次拉取50条消息，把消息的处理解析的过程，并发去处理，相当于起多个消费者去处理**；

  ##### 05：消费者的配置

  1. fetch.min.bytes
     - 















