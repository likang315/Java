### QMQ

------

[TOC]

##### 01：QMQ

- Qunar 自主研发的QMQ中间件，有prefix特性，前缀是部分主题；


##### 02、特性

- **过期时间**
- qmq：qmq_expireTime:15min;
  - new qmq: two days;
  
- **消费方式**
- qmq：push：
  - new qmq：pull；
  
- **prefix**
- 消费的主题，相当于topic；
  
- **consumerGroup**
- 消费集群，按照某种负载策略选取其中一台机器进行消费；
  - 若是需要实现广播的形式，可以不同设置 consumerGroup 这样 qmq-client 会让每台机器都使用都属于一个consumerGroup；
  
- **延迟和定时消息投递** 
- 生成延迟或定时消息使用Message中如下两个方法，任选其一
    - void setDelayTime(Date date);
      - 绝对时间
    - void ``setDelayTime(``long delayTime, TimeUnit timeUnit);
      - 相对时间

##### 03：Consumer

- consumer 端注册 MessageListener 的时候使用方需要提供一个线程池，该线程池用来处理接收到的消息(qmq 2.x的线程池大小在qconfig里配置，动态生效)。如果该线程池已满则新接收的消息会被reject，qmq server收到reject异常稍后会对消息进行重试，一直重试到消息过期。

- ##### 消费者推动策略

  - 如果**该subject从来没有消费者订阅过，那么在消费者上线之前的消息不会重新推送给消费者**，不过如果这些消息是可靠消息的话，qmq会保留三个月内的历史消息，可以重放这些消息；
  - 如果该消费者订阅过这个消息，现在只是临时下线(比如故障处理等)，则消费者再次上线后消息会被重新推送给消费者；
  - 如果消费者消费过这个消息，但是之前都消费失败了，然后消费者离线，那么将根据之前的消费次数和过期时间做出重发决定:
    - 如果之前已经消费过最低消费次数(这是qmq配置的，目前是3次)，但是现在消息过期了，则不再重推这条消息；
    - 如果之前消费的次数低于最低消费次数，但是现在消息过期了，那么消费者上线后会继续推送消息，直到满足最低次数条件；
    - 如果消费者离线再上线一直在过期时间内，则消费者再次上线后会重推这些消息；

- **幂检查器**【若有两条一样的消息，只处理其中一条】

  - 默认时根据 message id 去重，就是把消费成功的消息ID 写入到redis 中；
  - insert时，消息ID作为主键插入；

- **FIlter**

  - 可以在 consumer 端注册一些filter，这些 filter 将在 listener.onMessage 之前和之后执行一些逻辑。filter需要实现下面的接口:

  - 

  - ```java
    public interface Filter {
        /**
         * 在listener.onMessage之前执行
         *
         * @param message       处理的消息，建议不要修改消息内容
         * @param filterContext 可以在这里保存一些上下文
         * @return 如果返回true则filter链继续往下执行，只要任一filter返回false，则后续的
         * filter链不会执行，并且listener.onMessage也不会执行
         */
        boolean preOnMessage(Message message, Map<String, Object> filterContext);
    
        /**
         * 在listener.onMessage之后执行，可以做一些资源清理工作
         *
         * @param message       处理的消息
         * @param e             filter链和listener.onMessage抛出的异常
         * @param filterContext 上下文
         */
        void postOnMessage(Message message, Throwable e, Map<String, Object>
                          filterContext);
    }
    ```

  - 如果使用的是 qmq 2.x 的annotation，可以采用这种方式

    ```java
    `@QmqConsumer``(prefix=``"mymessage"``, consumerGroup=``"mygroup"``, filters={``"filter1"``, ``"filter2"``})` `public``void``handleMessage(Message msg){`  `}`
    ```

- **显示ACK**

- **调整Consumer线程池**

  - 手动创建 quota.properties 配置文件
