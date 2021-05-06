### Kafka Producer

------

[TOC]

##### 01：Kafka 使用场景

1. 是否每个消息都很重要？是否允许丢失一小部分消息？
2. 偶尔出现重复消息是否可以接受？
3. 是否有严格的延迟和吞吐量要求？

##### 02：生产者概览

1. 创建 **ProducerRecord** 对象开始，ProducerRecord 对象需要包含目标**主题和要发送的内容，还可以指定键或分区**。在发送ProducerRecord 对象时，生产者要先把**键和值对象序列化成字节数组**，这样它们才能够在网络上传输。
2. 数据被传给**分区器**。如果之前在 ProducerRecord 对象里指定了分区，那么分区器就不会再做任何事情，直接把指定的分区返回。如果没有指定分区，那么分区器会根据ProducerRecord **对象的键来选择一个分区hash(key)**。选好分区以后，生产者就知道该往哪个主题和分区发送这条记录了。紧接着，这条记录**被添加到一个记录批次里**，这个批次里的所有消息会被发送到相同的主题和分区上。有一个**独立的线程负责把这些记录批次发送到相应的broker 上**。
3. 服务器在收到这些消息时会**返回响应**。如果消息成功写入Kafka，就返回一个 **RecordMetaData** 对象，它包含了**主题和分区信息，以及记录在分区里的偏移量**。**如果写入失败，则会返回一个错误**。生产者在收到错误之后会尝试**重送**消息，几次之后如果还是失败，就返回错误信息。

##### 03：创建生产者

###### Kafka 生产者有3个必选的属性。

- bootstrap.servers
  - **指定 broker 的地址清单**，地址的格式为host:port，不需要包含所有broker地址，生产者会从给定的broker 里查找到其他broker 的信息，建议两个；
- key.serializer
  - broker 希望接收到的**消息的键和值都是字节数组**，必须被设置为一个实现了org.apache.kafka.common.serialization.Serializer 接口的类，生产者会使用这个类把键对象序列化成字节数组。
  - Kafka 客户端默认提供了ByteArraySerializer（这个只做很少的事情）、StringSerializer 和IntegerSerialize
- value.serializer
  - value.serializer 指定的类会将值序列化;

```java
private Properties kafkaProps = new Properties();
kafkaProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
kafkaProps.put("key.serializer",
"org.apache.kafka.common.serialization.StringSerializer"); 
kafkaProps.put("value.serializer",
"org.apache.kafka.common.serialization.StringSerializer");
producer = new KafkaProducer<String, String>(kafkaProps);
```

###### 发送消息的方式

- **发送并忘记（fire-and-forget）**
  - 我们把消息发送给服务器，但并**不关心它是否正常到达**。大多数情况下，消息会正常到达，因为Kafka 是高可用的，而且生产者会自动尝试重发；
- **同步重发**
  - send() 方法发送消息，它会**返回一个 Future 对象，调用 get() 方法进行等待**，就可以知道消息是否发送成功；
- **异步发送**
  - 我们调用 send() 方法，并指定一个**回调函数**，服务器在返回响应时调用该函数。

##### 04：发送消息到Kafka

- ```java
  ProducerRecord<String, String> record =
    new ProducerRecord<>("CustomerCountry", "Precision Products","France");
  try {
    producer.send(record);
  } catch (Exception e) {
    e.printStackTrace();
  }
  ```

###### 同步发送消息

- ```java
  ProducerRecord<String, String> record =
    new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
  try {
    producer.send(record).get();
  } catch (Exception e) {
    e.printStackTrace();
  }
  ```

- producer.send() 方法先返回一个Future 对象，然后调用Future 对象的get()方法等待Kafka 响应。**如果服务器返回错误，get() 方法会抛出异常。如果没有发生错误，我们会得到一个 RecordMetadata 对象，可以用它获取消息的偏移量。**

- KafkaProducer 一般会发生两类错误。其中一类是可重试错误，这类错误可以通过重发消息来解决，比如无主错误，另一类错误无法通过重试解决，比如“消息太大”异常;

###### 异步发送消息

- 大多时候我们**不需要等待响应**，尽管Kafka会把目标主题、分区信息和消息的偏移量发送回来，但对于发送端的应用程序来说不是必需的。不过在遇到消息发送失败时，我们需要抛出异常、记录错误日志，或者把消息写入“错误消息”文件以便日后分析。

- ```java
  private class DemoProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
      if (e != null) {
        e.printStackTrace();
        // retry...
      }
    }
  }
  ProducerRecord<String, String> record =
    new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");
  producer.send(record, new DemoProducerCallback());
  ```

  - 为了使用回调，需要一个**实现了org.apache.kafka.clients.producer.Callback 接口的类**，这个接口只有一个onCompletion 方法。
  - 如果 Kafka **返回一个错误，onCompletion 方法会抛出一个非空（non null）异常**；

##### 05：生产者的配置

- acks

  - 指定了必须要有多少个**分区副本收到消息**，生产者才会认为**消息写入是成功的**；
  - 如果**acks=0**，生产者在成功写入消息之前**不会等待任何来自服务器的响应**。也就是说，如果当中出现了问题，导致服务器没有收到消息，那么生产者就**无从得知，消息也就丢失了**；
  - 如果**acks=1**，只要**集群的首领节点收到消息，生产者就会收到一个来自服务器的成功响应**。如果消息**无法到达首领节点（比如首领节点崩溃，新的首领还没有被选举出来），生产者会收到一个错误响应**，为了避免数据丢失，生产者会重发消息；
  - 如果**acks=all**，只有当**所有参与复制的节点全部收到消息时**，生产者才会收到一个来自服务器的成功响应。这种模式是最安全的，它可以保证不止一个服务器收到消息，就算有服务器发生崩溃，整个集群仍然可以运行，延迟非常高；

- **buffer.memory**
- 该参数用来设置**生产者内存缓冲区的大小**，生产者用它缓冲要发送到服务器的消息。如果应用程序**发送消息的速度超过发送到服务器的速度，会导致生产者空间不足**。这个时候，send() 方法调用要么被阻塞，要么抛出异常，取决于如何设置**block.on.buffer.full** 参数；
  
- **retries**
- 重试次数，**retries 参数的值决定了生产者可以重发消息的次数，如果达到这个次数，生产者会放弃重试并返回错误**。默认情况下，生产者会在**每次重试之间等待100ms**，不过可以通过 **retry.backoff.ms** 参数来改变这个时间间隔。建议在设置重试次数和重试时间间隔之前，先测试一下恢复一个崩溃节点需要多少时间（比如所有分区选举出首领需要多长时间），让**总的重试时间比 Kafka 集群从崩溃中恢复的时间长**，否则生产者会过早地放弃重试。一般情况下，因为**生产者会自动进行重试**，所以就没必要在代码逻辑里处理那些可重试的错误。你只需要处理那些不可重试的错误或**重试次数超出上限**的情况。
  
- **batch.size**
- 当**有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里**。该参数指定了**一个批次可以使用的内存大小，按照字节数计算（而不是消息个数）**。当批次被填满，批次里的所有消息会被发送出去。**不过生产者并不一定都会等到批次被填满才发送，半满的批次，甚至只包含一个消息的批次也有可能被发送。**所以就算把批次大小设置得很大，也不会造成延迟，只是会占用更多的内存而已。但如果设置得太小，因为生产者需要更频繁地发送消息，会增加一些额外的开销。
  
- linger.ms

  - 该参数指定了**生产者在发送批次之前等待更多消息加入批次的时间**。KafkaProducer 会在**批次填满或linger.ms 达到上限时把批次发送出去**。默认情况下，只要有可用的线程，生产者就会把消息发送出去，就算批次里只有一个消息。

- client.id

  - 该参数可以是任意的字符串，服务器会用它来识别**消息的来源**。

- max.in.flight.requests.per.connection

  - 该参数指定了**生产者在收到服务器响应之前可以发送多少个消息**。它的值越高，就会占用越多的内存，不过也会提升吞吐量。把它**设为1 可以保证消息是按照发送的顺序写入服务器的，即使发生了重试**。

- timeout.ms、request.timeout.ms 和 metadata.fetch.timeout.ms

  - request.timeout.ms 指定了生产者在发送数据时等待服务器返回响应的时间，metadata.fetch.timeout.ms 指定了生产者在获取元数据（比如目标分区的首领是谁）时等待服务器返回响应的时间。如果等**待响应超时，那么生产者要么重试发送数据，要么返回一个错误**（抛出异常或执行回调）。timeout.ms 指定了broker 等待同步副本返回消息确认的时间，与asks 的配置相匹配——如果在指定时间内没有收到同步副本的确认，那么broker 就会返回一个错误。

- max.block.ms

  - 该参数指定了在调用send() 方法或使用partitionsFor() 方法获取元数据时**生产者的阻塞时间**。当生产者的发送缓冲区已满，或者没有可用的元数据时，这些方法就会阻塞。**在阻塞时间达到max.block.ms 时，生产者会抛出超时异常。**

- **max.request.size**
- 该参数用于**控制生产者发送的请求大小**。它可以指能发送的**单个消息的最大值**，也可以指**单个请求里所有消息总的大小**。另外，broker 对可**接收的消息最大值**也有自己的限制（message.max.bytes），所以两边的配置最好可以匹配，避免生产者发送的消息被broker 拒绝。
  
- receive.buffer.bytes 和 send.buffer.bytes

  - 这两个参数分别指定了 TCP socket 接收和发送数据包的缓冲区大小。如果它们被设为-1，就使用操作系统的默认值。


###### 顺序保证【重要】

- 如果某些场景要求**消息是有序的**，那么消息是否写入成功也是很关键的，所以不建议把**retries 设为0**。可以把**max.in.flight.requests.per.connection 设为1**，这样在生产者尝试发送第一批消息时，就不会有其他的消息发送给broker。不过这样会**严重影响生产者的吞吐量**，所以只有在对消息的顺序有严格要求的情况下才能这么做。

##### 06：序列化器

###### 自定义序列化器

- 不建议使用，因为生产者与消费者需要一致，因此使用已有的序列化器，例如：Json；

##### 07：分区

- Kafka 的消息是一个个**键值对**，ProducerRecord 对象**可以只包含目标主题和值，键可以设置为默认的null**，不过大多数应用程序会用到键。
- 键有两个用途：
  - 作为消息的**附加信息**，
  - 用来**决定消息该被写到主题的哪个分区**。拥有相同键的消息将被写到同一个分区。

###### 键值被设置为 null

- 如果**键值为null，并且使用了默认的分区器**，那么记录将被分区器使用**轮询（Round Robin）算法将消息均衡地分布到各个分区上**；

###### 键值不为null

- 如果键不为空，并且使用了默认的分区器，那么Kafka 会**对键进行散列，然后根据散列值把消息映射到特定的分区上**。这里的关键之处在于，**同一个键总是被映射到同一个分区上，所以在进行映射时，我们会使用主题所有的分区，而不仅仅是可用的分区；**

###### 实现自定义分区策略

- ```java
  public class BananaPartitioner implements Partitioner {
    public void configure(Map<String, ?> configs) {}
    public int partition(String topic, Object key, byte[] keyBytes, Object value,
                         byte[] valueBytes, Cluster cluster) {
      List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
      int numPartitions = partitions.size();
      if ((keyBytes == null) || (!(key instanceOf String)))
        throw new InvalidRecordException("expect all messages have customer name as key")
        if (((String) key).equals("Banana"))
          // Banana总是被分配到最后一个分区
          return numPartitions; 
      
      // 其他记录被散列到其他分区
      return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1));
    }
    public void close() {}
  }
  ```

  - 只接受字符串作为键，如果不是字符串，就抛出异常;

##### 08：Kafka 的客户端

- ```xml
  <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
  </dependency>
  ```









