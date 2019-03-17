package com.example.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lee
 * @Date: 2019/03/12 21:46
 */
public class SemaphoreTest implements Callable {

    final Semaphore semaphore = new Semaphore(5);


    @Override
    public Object call() throws Exception {
        semaphore.acquire();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread().getId() + ": done");
        semaphore.release();

        return Thread.currentThread().getId();
    }

    public static void main(String[] args) {
        final SemaphoreTest test1 = new SemaphoreTest();
        FutureTask futureTask1 = new FutureTask(test1);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            fixedThreadPool.submit(futureTask1); // test1
        }

        try {
            System.out.println(futureTask1.get());
            System.out.println(futureTask1.isDone());
            if (futureTask1.isDone()) {
                fixedThreadPool.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
