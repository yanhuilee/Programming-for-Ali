支持ConcurrentMap、EhCache、Caﬀeine、JCache（JSR-107）

org.springframework.cache.Cache
org.springframework.cache.CacheManager

#### Spring Boot 配置 Redis 缓存
```java
spring-boot-starter-cache
spring-boot-starter-redis

spring.cache.type=redis
spring.cache.cache-names=xx
spring.cache.redis.time-to-live=5000
spring.cache.redis.cache-null-values=false

spring.redis.host=xx

@CacheConfig(cacheNames = "xx")

@Cacheable
@EnableCaching(proxyTargetClass = true) //拦截类
```