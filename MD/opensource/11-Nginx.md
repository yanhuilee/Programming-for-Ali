
工作原理：进程模型
高可用：keepalived

#### 配置
```
rewrite
server {
	listen 80;
	server_name localhost;

	location / {
		proxypass xxx;
		root html;
		index index.html;
	}
}

upstream xxx {
	server
}
```

#### 负载均衡（Load Balance）
策略：

(1) 请求轮询：和DNS轮询类似，请求依次路由到各个web-server；

(2) 最少连接路由：哪个web-server的连接少，路由到哪个web-server；

(3) ip哈希：按照访问用户的ip哈希值来路由web-server，只要用户的ip分布是均匀的，请求理论上也是均匀的，ip哈希均衡方法可以做到，同一个用户的请求固定落到同一台web-server上，此策略适合有状态服务，例如session；

画外音：站点层可以存储session，但强烈不建议这么做，站点层无状态是分布式架构设计的基本原则之一，session最好放到数据层存储。

#### 虚拟主机

---

随着SOA、微服务越来越流行，注册发现服务已经成为架构里的标配。无论是在选择Dubbo、Dubbox、Spring Cloud都提供了对应的方案，我们不需要每次新增一个节点，就去修改对应配置。那么在使用Nginx的时候我们该怎么做呢？
参考类似服务发现的方案，我们选择了微博开源的nginx插件：
https://github.com/weibocom/nginx-upsync-module
