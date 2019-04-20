> Eureka 1.8.X 

#### 项目结构

Eureka：分为 Client 和 Server

- Eureka-Server：通过 REST 协议暴露服务，提供应用服务的注册和发现
- Provider：服务提供者，内嵌 Eureka-Client ，通过它向 Eureka-Server 注册自身服务
- Consumer：服务消费者，内嵌 Eureka-Client ，通过它从 Eureka-Server 获取服务列表

#### Eureka-Server 启动
```java
// EurekaClientServerRestIntegrationTest.java
private static void startServer() throws Exception {
   server = new Server(8080);

   // TODO Thread.currentThread().getContextClassLoader() 获取不到路径
   WebAppContext webAppCtx = new WebAppContext(new File("./eureka-server/src/main/webapp").getAbsolutePath(), "/");
   webAppCtx.setDescriptor(new File("./eureka-server/src/main/webapp/WEB-INF/web.xml").getAbsolutePath());
   webAppCtx.setResourceBase(new File("./eureka-server/src/main/resources").getAbsolutePath());
   webAppCtx.setClassLoader(Thread.currentThread().getContextClassLoader());
   server.setHandler(webAppCtx);
   server.start();

   eurekaServiceUrl = "http://localhost:8080/v2";
}
```

自我保护：三次心跳，只读写，不可删除

##### EurekaServerConfig: Eureka-Server 配置接口
- 安全认证：security
- 请求限流： 基于令牌桶算法的 RateLimiter

获取注册信息： 应用注册发现

#### 1、注册： Eureka-Client 向 Eureka-Server 注册应用实例过程
appName, instanceName, ipAddr, port

```
this.scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-InstanceInfoReplicator-%d")
                        .setDaemon(true)
                        .build());
```

续租 下线 自我保护机制 过期 全量获取 增量获取 覆盖状态
---

resttemplate
openfeign