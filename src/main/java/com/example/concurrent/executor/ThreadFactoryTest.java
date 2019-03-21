package com.example.concurrent.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用guava提供的ThreadFactoryBuilder来创建线程池
 * @Author: Lee
 * @Date: 2019/03/18 15:48
 */
public class ThreadFactoryTest implements Runnable {

    public static void main(String[] args) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            pool.execute(new ThreadFactoryTest());
        }
    }

    @Override
    public void run() {
        System.out.println("--------");
    }
}
