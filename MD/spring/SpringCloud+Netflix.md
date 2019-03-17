> 微服务架构一站式解决方案

#### 1、Spring Cloud 主要子项目
- Spring Cloud Config: 统一配置中心，文件存储于Git，依赖Spring Cloud Bus
- Spring Cloud Security
- Spring Cloud Consul/Zookeeper: 服务发现，注册，配置，类似于Dubbo
- Spring Cloud Bus: 服务间通信的分布式消息事件总线，主要用来在集群中传播状态改变（如配置改动）
- Spring Cloud Sleuth: 分布式追踪系统，单次请求的链路轨迹以及耗时等信息

Spring Cloud Netflix
- Zuul: API网关
	- 认证授权和安全
	- 监控
	- 动态路由
	- 压力测试
	- 限流
	- 静态响应控制：对于某些请求直接在边缘返回而不转发到后端集群
	- 多区域弹性：在AWS的多个Region中请求路由
- Eureka: 服务注册发现，包括负载均衡和容错
- Hystrix: 容错，降级，隔离等


#### Hystrix 服务熔断
```
@HystrixCommand(fallbackMethod = "findByIdFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
}, threadPoolProperties = {
        @HystrixProperty(name = "coreSize", value = "1"),
        @HystrixProperty(name = "maxQueueSize", value = "10"),
})
```

##### 请求超时，断路器
- 包裹请求：HystrixCommand
- 跳闸机制
- 资源隔离：线程池
- 监控：实时监控运行指标和配置变化
- 回退机制
- 自我修复

##### 线程隔离策略与传播上下文
线程隔离和信号量隔离
