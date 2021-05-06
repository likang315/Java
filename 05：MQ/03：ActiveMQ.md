### ActiveMQ

------

[TOC]

- ActiveMQ 是Apache自己编写的MQ，不过性能较差，已弃用；

##### 01：WEB管理界面介绍

![WEB面板介绍](https://github.com/likang315/Middleware/blob/master/MQ/photos/WEB%E9%9D%A2%E6%9D%BF%E4%BB%8B%E7%BB%8D.png?raw=true)

- Name：消息队列名称；
- Number Of Pending Messages：在队列中等待消费的消息；
- Number Of Consumers：消费者的数量；
- Messages Enqueued：进入队列的消息，进入队列的总数量，包括出队列的；
- Messages Dequeued：出了队列的消息，消费掉的消息数量；
- Topic 时，消息会被不同的消费者消费，**Dequeued  > Enqueued**；

##### 02：Point-To-Point 模式【Queue】

###### 生产者

```java
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Objects;

/**
 * session 通过参数设置是否已事务提交，若是批量发消息时会回滚，否则正常消息发送成功，异常消息发送失败
 *
 * @author kangkang.li@qunar.com
 * @date 2020-08-22 11:33
 */
public class Producer {
    private static void producer() throws Exception {
        Connection connection = null;
        Session session = null;
        try {
            // 默认localhost
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            // session第一个参数是否是事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queueName");
            MessageProducer producer = session.createProducer(destination);

            for (int i = 0; i < 5; i++) {
                MapMessage message = session.createMapMessage();
                message.setLong("count", System.currentTimeMillis());
                if (i == 2) {
                    throw new Exception();
                }
                producer.send(message);
            }

            // 非事务方式不用提交
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();

        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
            if (Objects.nonNull(connection)) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
            producer();
    }
}

```

###### 消费者

```java
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Objects;

/**
 * session 通过参数设置是否事务提交，若是批量pull消息时会回滚，否则从异常消息处往后的消息均消费失败
 *
 * @author kangkang.li@qunar.com
 * @date 2020-08-22 11:33
 */
public class Consumer {
    private static void consumer() throws Exception {
        Connection connection = null;
        Session session = null;
        try {
            // 默认localhost
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            // session第一个参数是否是事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queueName");
            MessageConsumer consumer = session.createConsumer(destination);

            for (int i = 0; i < 5; i++) {
                // 若是以事务方式，则会回滚... 非事务时则会异常消息处终止消费
                if (i == 3) {
                    throw new Exception();
                }
              	// 阻塞线程消费
                MapMessage mapMessage = (MapMessage) consumer.receive();
              	// 注册一个实现 MessgaeListener 接口的监听器，当有消息push时，自动调用onMessage()
              	// consumer.setMessageListener(Lisener);
              	// 创建监听器时会自动阻塞线程，让其一直处于监听状态
                System.out.println("start consume: " + mapMessage.getLong("count"));
            }

            // 非事务方式不用提交
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
          	//  finlly 关闭之后就不回阻塞了
            if (Objects.nonNull(session)) {
                session.close();
            }
            if (Objects.nonNull(connection)) {
                connection.close();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        consumer();
    }
}
```

##### 03：发布-订阅模式

- 为什么要使用发布订阅模式？【解耦】
  - 注册用户成功后发一封激活邮件，用户收到邮件后点击激活链接后才能使用该网站。一般的做法是在注册用户业务逻辑中调用发送邮件的逻辑。这样**用户业务就依赖于邮件业务**。如果以后改为短信激活，注册用户业务逻辑就必须修改为调用发送短信的逻辑。如果要注册后给用户加点积分，再加一段逻辑。经过多次修改，我们发现很**简单的注册用户业务已经越来越复杂，越来越难以维护**。这时候就需要**解耦**，将注册成功后的业务逻辑从用户业务中剥离出来，**谁需要消费我的消息，谁订阅就行**，这样Producer和Consumer互不知道对方的情况下完成了功能；

##### 04：publish-subscribe 模式【push】

- ###### Producer

```java
/**
 * 发布订阅模式
 * 订阅topic，Producer 往指定信道上发送消息，订阅该信道的 Consumer 会自动收信道 push 的消息
 *
 * @author kangkang.li@qunar.com
 * @date 2020-08-22 11:33
 */
public class Producer {

    private static void producer() throws Exception {
        Connection connection = null;
        Session session = null;

        try {
            // 默认localhost
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            // session第一个参数是否是事务
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("topicName");
            MessageProducer producer = session.createProducer(destination);

            for (int i = 0; i < 5; i++) {
                TextMessage message = session.createTextMessage();
                message.setText(Long.toString(System.currentTimeMillis()));
                producer.send(message);
            }
            // 非事务方式不用提交
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();

        } finally {
            if (Objects.nonNull(session)) {
                session.close();
            }
            if (Objects.nonNull(connection)) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        producer();
    }
}

```

- ###### Consumer

```java
public class Consumer1 {
    static void consumer() {
        Connection connection;
        Session session;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
          	// 发布模式中 订阅topic
            MessageConsumer messageConsumer = session.createConsumer(
              session.createTopic("topicName"));

            messageConsumer.setMessageListener(new MQListener());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 暂时让其处于监听状态，不关闭
        }

    }
  
  	static class MQListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            try {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Customer1 消费消息:" + textMessage.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        consumer();
    }
}
```

