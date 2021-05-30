### Spring Kafka

------

##### 01：producer config

- ```xml
  <dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
  </dependency>
  ```

```java
/**
 * kafka 生产者配置
 *
 * @author kangkang.li@qunar.com
 * @date 2021-03-08 14:45
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${zookeeper.connect}")
    private String zkConnect;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.retries}")
    private String kafkaProducerRetries;

    @Value("${kafka.producer.batch.size}")
    private String kafkaProducerBatchSize;

    @Value("${kafka.producer.linger.ms}")
    private String kafkaProducerLingerMs;

    @Value("${kafka.producer.buffer.memory}")
    private String kafkaProducerBufferMemory;

    /**
     * 生产者配置
     *
     * @return
     */
    private Map<String, Object> senderProps (){
        Map<String, Object> props = new HashMap<>(16);
        props.put("zookeeper.connect", zkConnect);
        // broker address
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerRetries);
        // 控制批处理大小,单位字节数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerBatchSize);
        // 批量发送延迟时间
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerLingerMs);
        // 生产者内存缓冲区
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerBufferMemory);
        // 键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    /**
     * 创建生产者工厂
     */
    @Bean(name = "producerFactory")
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(senderProps());
    }

    /**
     * Kafka 发送接收等功能
     *
     * @return
     */
    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        return kafkaTemplate;
    }

    /**
     * kafka admin 用于自定义主题属性配置
     *
     * @return
     */
    @Bean(name = "kafkaAdmin")
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>(1);
        //配置Kafka实例的连接地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(props);
    }

    /**
     * kafkaClient 自定义主题属性
     *
     * @return
     */
    @Bean(name = "adminClient")
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }
}
```

##### 02：producer

```java
/**
 * kafka producer

 * @author kangkang.li@qunar.com
 * @date 2021-03-08 15:17
 */
@Slf4j
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private AdminClient adminClient;

    @Resource(name = "kafkaSendResultHandler")
    private ProducerListener<String, String> producerListener;

    /**
     *  使用kafkaTemplate发送消息，默认创建单个分区
     */
    public void sendMsg1() {
        kafkaTemplate.send("custom.topic.partition.one", "this is my first demo!!!");
    }

    /**
     * 使用adminClient创建多个分区的Topic，在使用模板发送即可
     */
    public void sendMsg2() {
        NewTopic topic = new NewTopic("custom.topic.partition.more", 10, (short) 2);
        adminClient.createTopics(Collections.singletonList(topic));
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send("custom.topic.partition.more", "this is my first demo!!!");
    }

    /**
     * 通过 ProducerRecord 发送消息
     *
     * @param topic
     * @param key
     * @param data
     */
    public void sendMsg3(String topic, String key, String data) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, data);
        kafkaTemplate.send(record);
    }

    /**
     * 通过 ProducerRecord 发送消息, 回调专用
     *
     * @param record
     */
    public void sendMsg3(ProducerRecord<String, String> record) {
        kafkaTemplate.send(record);
    }
}

/**
 * Kafka 消息发送成功回调处理器
 *
 * @author kangkang.li@qunar.com
 * @date 2021-03-08 21:15
 */
@Slf4j
@Component
public class KafkaSendResultHandler implements ProducerListener<String, String> {

    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord,
                          RecordMetadata recordMetadata) {
        log.info("Message send success : " + producerRecord.toString());
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord,
                        Exception exception) {
        // 回调重试
        log.error("Message send error : " + producerRecord.toString());
        kafkaProducer.sendMsg3(producerRecord);
    }
}
```

##### 03：Consumer config

```java
/**
 * Kafka 消费者配置
 *
 * @author kangkang.li@qunar.com
 * @date 2021-03-08 14:32
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${zookeeper.connect}")
    private String zkConnect;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.enable.auto.commit}")
    private String kafkaConsumerEnableAutocommit;

    @Value("${kafka.consumer.session.timeout}")
    private String kafkaConsumerSessionTimeout;

    @Value("${kafka.consumer.auto.commit.interval.ms}")
    private String kafkaConsumerAutocommitIntervalMs;

    @Value("${kafka.consumer.max.poll.records}")
    private String kafkaConsumerMaxPollRecords;

    @Value("${kafka.consumer.auto.offset.reset}")
    private String kafkaConsumerAutoOffsetReset;

    /**
     * 消费者配置
     *
     * @return
     */
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put("zookeeper.connect", zkConnect);
        // broker address
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerEnableAutocommit);
        // 自动提交间隔时间
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerAutocommitIntervalMs);
        // 拉取最大消息量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerMaxPollRecords);
        // 偏移量无效时如何处理
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerAutoOffsetReset);
        // 心跳时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerSessionTimeout);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;
    }

    /**
     * 消费者工厂
     */
    @Bean(name = "consumerFactory")
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    /**
     * 消费者监听器容器工厂，一个消费者线程单条处理消息【三个互斥】
     *
     * @return
     */
    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String>
        kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> container =
            new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory());
        return container;
    }

    /**
     * 消费者监听器容器工厂，启用多个消费者线程批量处理消息
     *
     * @return
     */
    @Bean(name = "batchKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> 
        batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> container =
            new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory());
        // 容器中启多少个消费者实例【相当于线程数】，小于或等于Topic的分区数
        container.setConcurrency(4);
        // 设置为批量监听
        container.setBatchListener(true);
        return container;
    }

    /**
     * 消费者监听器容器工厂，启用多个消费者线程批量处理消息，自定义确认提交机制
     *
     * @return
     */
    @Bean(name = "ackKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> ackKafkaListenerContainerFactory() 
    {
        ConcurrentKafkaListenerContainerFactory<String, String> container =
            new ConcurrentKafkaListenerContainerFactory<>();
        container.setConsumerFactory(consumerFactory());
        // 禁用自动提交，设置ack模式
        container.getContainerProperties()
            .setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // 容器中启动多少个线程，小于或等于Topic的分区数
        container.setConcurrency(4);
        // 设置为批量监听
        container.setBatchListener(true);
        return container;
    }
}
```

##### 04：Consumer

```java
/**
 * kafka消费者
 *
 * @author kangkang.li@qunar.com
 * @date 2021-03-08 15:17
 */
@Slf4j
@Component
public class KafkaConsumerListener {

  	/**
     * 多个消费者线程拉取自己所属的 partition msg
     *
     * @param record
     */
  @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group.id}", 
                 containerFactory = "batchKafkaListenerContainerFactory")
  public void onMessage(List<ConsumerRecord<String, String>> record) {
    log.error("KafkaConsumerListener: ", record.toString());
  }
}
```

##### 05：propesties 文件

```properties
zookeeper.connect=l-s1.com:2181,l-s2.com:2181
kafka.bootstrap.servers=l-s1.cn2:9092,l-s2.cn2:9092
# consumer config
kafka.consumer.enable.auto.commit=true
kafka.consumer.session.timeout=5000
kafka.consumer.auto.commit.interval.ms=100
kafka.consumer.auto.offset.reset=latest
kafka.consumer.max.poll.records=5
# producer config
kafka.producer.retries=1
kafka.producer.batch.size=1024 * 3
kafka.producer.linger.ms=100
kafka.producer.buffer.memory=1024 * 1024 * 3
# topic
kafka.group.id=beta_data_mq
kafka.topic=custom.topic.partition.more
```

