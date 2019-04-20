服务发现（Eureka），断路器（Hystrix），智能路由（Zuul）和客户端负载平衡（Ribbon）

#### 服务发现：Eureka客户端
注册：当客户端注册Eureka时，它提供关于自身的元数据，例如主机和端口，健康指示符URL，主页等。Eureka从属于服务的每个实例接收心跳消息。如果心跳失败超过可配置的时间表，则通常将该实例从注册表中删除。

/info /health

阈值时（Hystrix中的默认值为5秒内的20次故障）

@HystrixCommand(fallbackMethod = "defaultStores")