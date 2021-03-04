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

     - 指定了消费者**从服务器获取记录的最小字节数**。broker 在收到消费者的数据请求时，如果可用的数据量小于fetch.min.bytes 指定的大小，那么它会**等到有足够的可用数据时才把它返回给消费者**。

  2. fetch.max.wait.ms

     - 指定broker 的等待时间，默认是500ms。如果没有足够的数据流入Kafka，消费者获取最小数据量的要求就得不到满足，最终导致500ms 的延迟。
     - 如果fetch.max.wait.ms 被设为100ms，并且fetch.min.bytes 被设为1MB，那么Kafka 在收到消费者的请求后，要么返回1MB 数据，要么在100ms 后返回所有可用的数据，就看哪个条件先得到满足；

  3. max.partition.fetch.bytes

     - 指定了服务器从**每个分区里返回给消费者的最大字节数。它的默认值是1MB**，也就是说，KafkaConsumer.poll() 方法从每个分区里返回的记录最多不超过max.partition.fetch.bytes 指定的字节；

  4. session.timeout.ms

     - 消费者在被认为死亡之前可以与服务器断开连接的时间，默认是3s;
     - heartbeat.interval.ms 指定了poll() 方法**向协调器发送心跳的频率，session.timeout.ms 则指定了消费者可以多久不发送心跳**。所以，一般需要同时修改这两个属性，heartbeat.interval.ms 必须比session.timeout.ms 小，一般是session.timeout.ms 的**三分之一**;

  5. auto.offset.reset

     - 指定了消费者在**读取一个没有偏移量的分区或者偏移量无效的情况下（因消费者长时间失效，包含偏移量的记录已经过时并被删除）该作何处理**。它的默认值是latest，
     - latest：在偏移量无效的情况下，消费者将从最新的记录开始读取数据（在消费者启动之后生成的记录）。
     - earliest：在偏移量无效的情况下，消费者将从起始位置读取分区的记录；

  6. enable.auto.commit

     - 指定了消费者**是否自动提交偏移量**，默认值是true。
     - 如果把它设为true，还可以通过配置**auto.commit.interval.ms属性来控制提交的频率**，默认值：5s;

  7. partition.assignment.strategy

     - 决定哪些分区应该被分配给哪个消费者
     - Range
       - 该策略会把主题的若干个**连续**的分区分配给消费者；
     - RoundRobin
       - 该策略把主题的所有分区**逐个分配**给消费者，会给所有消费者分配相**同数量的分区**（或最多就差一个分区）。

  8. client.id

     - 任意字符串，broker 用它来标识从客户端发送过来的消息；

  9. max.poll.records

     -  用于控制单次调用call() 方法能够返回的记录数量，可以帮你控制在轮询里需要处

       理的数据量。

  10. receive.buffer.bytes 和 send.buffer.bytes

      - socket 在读写数据时用到的TCP 缓冲区也可以设置大小。如果它们被设为-1，就使用操

        作系统的默认值；

  ##### 06：提交和偏移量

  - 消费者可以使用 Kafka 来追踪消息在**分区里的位置（偏移量）**;
  - 更新分区当前位置的操作叫作**提交**;
  - 消费者往一个叫作**_consumer_offset** 的特殊主题发送消息，消息里包含**每个分区的偏移量**。
  - 仅当消费者发生崩溃或者有新的消费者加入群组，就会触发再均衡，完成再均衡之后，每个消费者可能分配到新的分区，而不是之前处理的那个。此时，**消费者需要读取每个分区最后一次提交的偏移量**，然后从偏移量指定的地方继续处理。
  - 如果**提交的偏移量小于客户端处理的最后一个消息的偏移量**，那么处于两个偏移量之间的消息就会被**重复消费；**
  
  ![kafka_offset](/Users/likang/Code/Git/Middleware/MQ/photos/kafka_offset.png)
  
  - 如果提**交的偏移量大于客户端处理的最后一个消息的偏移量**，那么处于两个偏移量之间的消息**将会丢失；**
  
    ![kafka_offset_lose](/Users/likang/Code/Git/Middleware/MQ/photos/kafka_offset_lose.png)
  
  ###### 自动提交
  
  - 自动提交也是在**轮询里进行**的。消费者每次在进行轮询时会检查是否该提交偏移量了，如果是，那么就会提交从上一次轮询返回的偏移量。
  - 自动提交虽然方便，不过并没有为开发者留有余地来避免在消费者异常情况下，重复处理消息。
  
  ###### 提交当前偏移量
  
  - 消费者API 提供了另一种提交偏移量的方式，开发者可以在必要的时候提交当前偏移量，而不是基于时间间隔；
  
  - 把**auto.commit.offset 设为false**，让应用程序决定何时提交偏移量。使用**commitSync()提交偏移量**最简单也最可靠。这个API 会**提交由poll() 方法返回的最新偏移量，提交成功后马上返回，如果提交失败就抛出异常**
  
  - ```java
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("topic = %s, partition = %s, offset =
                          %d, customer = %s, country = %s\n",
                          record.topic(), record.partition(),
                          record.offset(), record.key(), record.value());
      }
      try {
        // 只要没有发生不可恢复的错误，commitSync() 方法会一直尝试直至提交成功
        // 如果提交失败，我们也只能把异常记录到错误日志里，重复消费；
        consumer.commitSync();
      } catch (CommitFailedException e) {
        log.error("commit failed", e)
      }
    }
    ```

###### 异步提交

- 手动提交缺陷：在broker 对提交请求作出回应之前，应用程序会一直阻塞，这样会限制应用程序的吞吐量；

- 异步提交API

- ```java
  consumer.commitAsync(new OffsetCommitCallback() {
    public void onComplete(Map<TopicPartition,
                           OffsetAndMetadata> offsets, Exception e) {
      if (e != null)
        log.error("Commit failed for offsets {}", offsets, e);
      	// retry
    }
  });
  ```

###### 同步和异步的组合提交

- 