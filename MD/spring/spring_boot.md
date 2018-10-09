> 维护人员：**Lee**  
> 创建时间：2018-10-03

#### Spring Boot 最重要的4大核心特性
自动配置、起步依赖、Actuator、命令行界面(CLI)

#### SpringApplication.run()

#### Spring Boot Redis Cluster
```yaml
spring.redis:
  database: 0 # Redis数据库索引（默认为0）
  #host: 192.168.1.8
  #port: 6379
  password: 123456
  timeout: 10000 # 连接超时时间（毫秒）  
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

---
言必行，行必果
赏罚分明，令行禁止
韩信：53 55 56 60 62 69 70
