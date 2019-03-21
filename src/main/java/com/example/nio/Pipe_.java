package com.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author: Lee
 * @Date: 2019/03/18 17:31
 */
public class Pipe_ {

    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();
        // 向管道写入数据
        Pipe.SinkChannel sinkChannel = pipe.sink();
        // 调用write()方法写入数据
        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        System.out.println(newData);
        byteBuffer.put(newData.getBytes());

        byteBuffer.flip();

        while (byteBuffer.hasRemaining()) {
            sinkChannel.write(byteBuffer);
        }
        sinkChannel.close();


        // 从管道读取数据
        Pipe.SourceChannel sourceChannel = pipe.source();
//        sourceChannel.
        while (byteBuffer.hasRemaining()) {
            System.out.println(sourceChannel.read(byteBuffer));
        }
    }

    public static void select() throws IOException {

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(80));

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 通过 Selector 选择 Channel
            int ready = selector.select();
            if (ready == 0) {
                continue;
            }
            // 获得可操作的 Channel
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(key.selector(), SelectionKey.OP_ACCEPT);

                } else if (key.isReadable()) {
                    System.out.println("key read : " + key);
                } else if (key.isWritable() && key.isValid()) {
                    System.out.println("key write : " + key);
                } else if (key.isConnectable()) {
                    System.out.println("isConnectable = true");
                }
                iterator.remove();
            }
        }
    }
}
