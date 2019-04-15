### Kafka
- Producer

- Consumer

- Broker

```
kafka-topics.sh --zk ip:2181 --topic test1
  --partitions 3 --replication-factor 1

kafka-topics.sh --zk ip:2181 --describe --topic test1

kafka-console-producer.sh --broker-list ip:00 --topic test1
```

#### 消息中间件
场景：异步

ActiveMQ：
- 消息模型（点对点/发布订阅）
- 调用API（ConnectionFactory, Connection, Session, Message Producer/Consumer, Destination）
- 持久化

```java
// 1、通过工厂创建连接
connectionFactory = new ActiveMQConnectionFactory(username, pass, url)
connection = connectionFactory.createConnection()
connection.start()
// 2、创建session
session = connection.createSession(true, Session.AUTO_ACKNOW..)
// 3、消息目的地
destination = session.createQueue("msgQueue")
messageProducer = session.createProducer(destination)
// 4、发送消息
for (int i = 0; i < QueueProducer.MessageNum; i++) {
    TextMessage msg = session.createTextMessage("消息内容")
    messageProducer.send(msg)
}
session.commit()
```

配置
```properties
spring.activemq.broker-url=tcp://ip:61616
spring.activemq.in-memory=false
spring.activemq.pool.max-connections=100
```

@JmsListener
```java
@EnableJms
@Bean
public Queue queue() {
    return new ActiveMQQueue("pay.request")
}

@Autowired
private JmsMessagingTemplate jmsMessagingTemplate
jmsMessagingTemplate.covertAndSend(queue, "")

```