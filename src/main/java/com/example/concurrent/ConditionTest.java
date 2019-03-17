package com.example.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Lee
 * @Date: 2019/03/11 14:32
 */
public class ConditionTest implements Runnable {


    public static void main(String[] args) throws InterruptedException {
        // 重入锁
        ConditionTest test1 = new ConditionTest();
        ConditionTest test2 = new ConditionTest();
        Thread a = new Thread(test1);
        Thread b = new Thread(test2);
        a.start();
        TimeUnit.MILLISECONDS.sleep(2000);

        reentrantLock1.lock();
        // 需要线程先获得相关的锁，调用signal()，系统会从当前 Condition 对象的等待队列中，唤醒一个线程
        // 线程被唤醒后，会重新尝试获得与之绑定的重入锁
        condition.signal();
        reentrantLock1.unlock();
    }

    public static ReentrantLock reentrantLock1 = new ReentrantLock();
    public static Condition condition = reentrantLock1.newCondition();


    @Override
    public void run() {
        try {
            reentrantLock1.tryLock();
            // 需要线程持有相关的重入锁，然后释放这把锁
            condition.await();
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock1.unlock();
        }
    }
}
