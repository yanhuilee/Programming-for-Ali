Eureka：Client 和 Server

#### 项目结构
Provider
Consumer

#### Eureka-Server 启动
```java
// EurekaClientServerRestIntegrationTest.java
private static void startServer() throws Exception {
   server = new Server(8080);

   // TODO Thread.currentThread().getContextClassLoader() 获取不到路径，先暂时这样；
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

---

resttemplate
openfeign