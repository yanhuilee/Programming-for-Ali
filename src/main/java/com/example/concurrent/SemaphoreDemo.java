package com.example.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Author: Lee
 * @Date: 9/18/2018 11:25 PM
 * 信号量: 多个线程同时访问一个资源
 */
public class SemaphoreDemo implements Runnable {

    final Semaphore semaphore = new Semaphore(5);

    @Override
    public void run() {
        try {
            semaphore.acquire();
            // 模拟耗时操作
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + ":done!");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /// ExecutorService pool = Executors.newFixedThreadPool(20);
        ExecutorService pool = new ThreadPoolExecutor(20, 20,0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        final SemaphoreDemo demo = new SemaphoreDemo();
        for (int i = 0; i < 20; i++) {
            pool.submit(demo);
        }
        pool.shutdown();
    }
}
