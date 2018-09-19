package com.example.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author: Lee
 * @Date: 9/19/2018 12:21 AM
 */
public class CountDownLatchDemo implements Runnable {

    static final CountDownLatch end = new CountDownLatch(10);
    static final CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run() {
        // 模拟检查任务
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>());
        System.out.println("-------------------");
        for (int i = 0; i < 10; i++) {
            pool.submit(demo);
        }
        // 等待检查
        end.await();
        // 发射火箭
        System.out.println("Fire!");
        pool.shutdown();
    }
}
