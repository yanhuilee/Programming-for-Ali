### 第11讲 | Java提供了哪些IO方式？ NIO如何实现多路复用？

在 Java 7 中，NIO2，引入了异步非阻塞 IO 方式，也有很多人叫它 AIO（Asynchronous IO）。

异步 IO 操作基于事件和回调机制，简单理解为，应用操作直接返回，不会阻塞，当后台处理完成，操作系统会通知相应线程进行后续工作。

#### 1、区别一些概念：同步或异步，阻塞与非阻塞

IO：文件IO，网络IO

Closeable 接口，try-with-resources


#### 2、NIO 主要组成部分：Buffer，Channel，Selector

- Buffer，高效的数据容器，除了布尔类型，所有原始数据类型都有相应的 Buffer 实现。

- Channel，类似在 Linux 之类操作系统上看到的文件描述符，是 NIO 中被用来支持批量式 IO 操作的一种抽象。

File 或者 Socket，通常被认为是比较高层次的抽象，而 Channel 则是更加操作系统底层的一种抽象，这也使得 NIO 得以充分利用现代操作系统底层机制，获得特定场景的性能优化，例如，DMA（Direct Memory Access）等。不同层次的抽象是相互关联的，我们可以通过 Socket 获取 Channel，反之亦然。

- Selector，是 NIO 实现多路复用的基础，它提供了一种高效的机制，可以检测到注册在 Selector 上的多个 Channel 中，是否有 Channel 处于就绪状态，进而实现了单线程对多 Channel 的高效管理。

epoll 多路复用机制

#### 3、NIO 能解决什么问题？

设想，我们需要实现一个服务器应用，只简单要求能够同时服务多个客户端请求即可。

Java 语言目前的线程实现是比较重量级的，启动或者销毁一个线程是有明显开销的，每个线程都有单独的线程栈等结构，需要占用非常明显的内存，所以，每一个 Client 启动一个线程似乎都有些浪费。

```java
// 引入线程池机制来避免浪费
serverSocket = new ServerSocket(0);
// 通过一个固定大小的线程池，来负责管理工作线程
executor = Executors.newFixedThreadPool(8);
while (true) {
    Socket socket = serverSocket.accept();
    RequestHandler requestHandler = new RequestHandler(socket);
    executor.execute(requestHandler);
}
```

但是，如果连接数量急剧上升，这种实现方式就无法很好地工作了，因为线程上下文切换开销会在高并发时变得很明显，这是同步阻塞方式的低扩展性劣势。

NIO 引入的多路复用机制，提供了另外一种思路
```java
public class NIOServer extends Thread {
    public void run() {
    	// 创建 Selector 和 Channel
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open();) {
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            // 非阻塞模式
            serverSocket.configureBlocking(false);
            // 注册到 Selector，并说明关注点(关注新的连接请求)
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
            	// 阻塞等待就绪的 Channel，这是关键点之一
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // 生产系统中一般会额外进行就绪状态检查
                    sayHelloWorld((ServerSocketChannel) key.channel());
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sayHelloWorld(ServerSocketChannel server) throws IOException {
        try (SocketChannel client = server.accept();) {          
        	client.write(Charset.defaultCharset().encode("Hello world!"));
        }
    }
   // 省略了与前面类似的 main
}
```

在同步阻塞模式，需要多线程以实现多任务处理。而 NIO 则是利用了单线程轮询事件的机制，通过高效地定位就绪的 Channel，来决定做什么，仅仅 select 阶段是阻塞的，可以有效避免大量客户端连接时，频繁线程切换带来的问题，应用的扩展能力有了非常大的提高。

在 Java 7 引入的 NIO2，又增添了一种额外的异步 IO 模式，利用事件和回调，处理 Accept、Read 等操作：
```java
AsynchronousServerSocketChannel serverSock = AsynchronousServerSocketChannel.open().bind(sockAddr);
serverSock.accept(serverSock, new CompletionHandler<>() { 
	// 为异步操作指定 CompletionHandler 回调函数
    @Override
    public void completed(AsynchronousSocketChannel sockChannel, AsynchronousServerSocketChannel serverSock) {
        serverSock.accept(serverSock, this);
        // 另外一个 write（sock，CompletionHandler{}）
        sayHelloWorld(sockChannel, Charset.defaultCharset().encode
                ("Hello World!"));
    }
    // ...
});
```

