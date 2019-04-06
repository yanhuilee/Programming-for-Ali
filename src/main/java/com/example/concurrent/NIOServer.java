package com.example.concurrent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Lee
 * @Date: 2019/03/28 00:55
 */
public class NIOServer extends Thread {

    @Override
    public void run() {

        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            serverSocket.configureBlocking(false);
            // 注册到 Selector，并说明关注点
            serverSocket.register(selector, SelectionKey.OP_ACCEPT); //新的连接请求
            while (true) {
                // 阻塞等待就绪的 Channel，关键点之一
                selector.select();
                Set<SelectionKey> selectedKeys = selector.keys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // 生产系统中一般会额外进行就绪状态检查
                    sayHello(((ServerSocketChannel) key.channel()));
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sayHello(ServerSocketChannel server) {
        try (SocketChannel client = server.accept()) {
            client.write(Charset.defaultCharset().encode("hello"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
