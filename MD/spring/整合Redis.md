
spring-boot-starter-data-redis

##### Spring Boot Redis Cluster

```yaml
spring.redis:
  database: 0 # Redis数据库索引（默认为0）
  #host: 192.168.1.8
  #port: 6379
  password: 123
  timeout: 3000 # 连接超时时间（毫秒）  
  pool:
    max-active: 8 # 连接池最大连接数（使用负值表示没有限制）
    max-idle: 8 # 连接池中的最大空闲连接
    max-wait: -1 # 连接池最大阻塞等待时间（使用负值表示没有限制）
    min-idle: 0 # 连接池中的最小空闲连接
  cluster:
    nodes:
      - 192.168.1.8:9001
      - 192.168.1.8:9002
      - 192.168.1.8:9003
```

##### Example

```java
public class Example {

    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;

    public void addLink(String userId, URL url) {
        listOps.leftPush(userId, url.toExternalForm());
        // or use template directly
        redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
    }
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    public void addLink(String userId, URL url) {
        redisTemplate.opsForList().leftPush(userId, url.toExternalForm());
    }
}
```