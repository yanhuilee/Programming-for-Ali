package com.example.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * 死锁检测
 *
 * @Author: Lee
 * @Date: 2019/03/30 16:47
 */
public class DeadLock extends Thread {

    private String first;
    private String second;

    public DeadLock(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + "obtained: " + first);
            try {
                TimeUnit.SECONDS.sleep(1);
                synchronized (second) {
                    System.out.println(this.getName() + " obtained: " + second);
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String lockA = "lockA";
        String lockB = "lockB";
        DeadLock t1 = new DeadLock("thread1", lockA, lockB);
        DeadLock t2 = new DeadLock("thread2", lockB, lockA);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}