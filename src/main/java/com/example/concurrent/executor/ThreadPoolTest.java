package com.example.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lee
 * @Date: 2019/03/13 01:03
 */
public class ThreadPoolTest {

    public static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ": Thread ID: " + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyTask myTask = new MyTask();
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 10; i++) {
//            fixedThreadPool.submit(myTask);
//        }
//        fixedThreadPool.shutdown();
        System.out.println("-----------------");
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            cachedThreadPool.submit(myTask);
        }
        cachedThreadPool.shutdown();
    }
}
