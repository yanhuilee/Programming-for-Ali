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
