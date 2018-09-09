#### session 跨域解决方案
- session sticky: 同一客户端请求，落在同一服务器上
- session同步
- redis
- cookie：不安全
- token: 令牌

服务端根据Cookie中的JSESSIONID取到了对应的session

<artifactId>spring-session-data-redis</artifactId>
@EnableRedisHttpSession

#### Nginx
master进程，worker

负载：随机，轮询，权重，iphash

高可用：keepalved
