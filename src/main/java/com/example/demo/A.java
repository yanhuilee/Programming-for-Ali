package com.example.demo;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.Properties;

/**
 * @Author: Lee
 * @Date: 9/29/2018 4:27 PM
 */
public class A {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("xookeeper.connect", "zk1.dmp.com:2181,zk2.dmp.com:2181,zk3.dmp.com:2181");
        props.put("zookeeper.session.timeout.ms", "3000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("group.id", "test_group");
        props.put("auto.commmit.interval.ms", "600");

        String topic = "test_topic";
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
//        org.apache.kafka.clients.consumer.ConsumerConfig
    }
}
