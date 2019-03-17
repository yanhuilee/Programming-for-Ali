#### BIO：Block-IO
> 阻塞 + 同步的通信模式，通过 Socket 和 ServerSocket 实现套接字通道的通信。

##### 原理

- 服务器通过一个 Acceptor 线程，负责监听客户端请求和为每个客户端创建一个新的线程进行链路处理。典型的一请求一应答模式。
- 若客户端数量增多，频繁地创建和销毁线程会给服务器打开很大的压力。后改良为用线程池的方式代替新增线程，被称为伪异步 IO。

#### NIO：Non-Block IO
> 基于缓冲区（Buffer）的非阻塞IO。非阻塞 + 同步的通信模式

Java NIO 由如下三个核心组件组成：

- Channel
- Buffer
- Selector：判断这些注册的 Channel 是否有已就绪的 IO 事件( 例如可读，可写，网络连接已完成 )

##### 非阻塞
- 当一个线程执行从 Channel 执行读取 IO 操作时，当此时有数据，则读取数据并返回；当此时无数据，则直接返回而**不会阻塞当前线程**。
- 当一个线程执行向 Channel 执行写入 IO 操作时，**不需要阻塞等待它完全写入**，这个线程同时可以做别的事情。

也就是说，线程可以将非阻塞 IO 的空闲时间用于在其他 Channel 上执行 IO 操作。所以，一个单独的线程，可以管理多个 Channel 的读取和写入 IO 操作。


[环境搭建](http://svip.iocoder.cn/Netty/build-debugging-environment/)