
```
spring-boot-starter-activemq

# 配置
spring.activemq.broker-url=tcp://192.168.1.210:9876
spring.activemq.user=admin
spring.activemq.password=secret

# 集群 activemq-pool
spring.activemq.broker-url=failover(tcp://192.168.1.210:9876, ...)
spring.activemq.pool.enabled=true
spring.activemq.pool.max-connections=50

# 注解
@EnableJms
```
