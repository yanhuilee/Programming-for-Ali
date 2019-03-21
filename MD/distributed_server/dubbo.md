SOA: 面向服务

#### Dubbo 特点
- 远程通讯: 提供对多种基于长连接的 NIO 框架抽象封装（非阻塞 I/O 的通信方式，Mina/Netty/Grizzly），包括多种线程模型，序列化（Hessian2/ProtoBuf），以及“请求-响应”模式的信息交换方式。

- 集群容错: 提供基于接口方法的透明远程过程调用（RPC），包括多协议支持（自定义 RPC 协议），以及软负载均衡（Random/RoundRobin），失败容错（Failover/Failback），地址路由，动态配置等集群支持。

- 自动发现: 基于注册中心目录服务，使服务消费方能动态的查找服务提供方

[《注册中心参考手册》](http://dubbo.apache.org/zh-cn/docs/user/references/registry/introduction.html)

Multicast 注册中心不需要启动任何中心节点，只要广播地址一样，就可以互相发现。
	
#### dubbo: rpc-远程过程调用协议-netty-nio

	服务注册表
- 四大组件
- 三大领域模型
- 两大设计原则
- 一个整体架构

##### 声明式缓存

结果缓存

问题：为什么 Dubbo 比 Spring Cloud 性能要高一些？

回答：因为 Dubbo 采用单一长连接和 NIO 异步通讯（保持连接/轮询处理），使用自定义报文的 TCP 协议，并且序列化使用定制 Hessian2 框架，适合于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况，但不适用于传输大数据的服务调用。而 Spring Cloud 直接使用 HTTP 协议（但也不是强绑定，也可以使用 RPC 库，或者采用 HTTP 2.0 + 长链接方式（Fegin 可以灵活设置））。