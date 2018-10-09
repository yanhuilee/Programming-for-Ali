package com.example.concurrent;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Lee
 * @Date: 9/22/2018 4:24 AM
 */
public class ThreadLocalDemo_Gc {

    static volatile ThreadLocal<SimpleDateFormat> tl = new ThreadLocal() {
        @Override
        protected void finalize() throws Throwable {
            System.out.println(this.toString() + " is gc");
        }
    };

    static volatile CountDownLatch cd = new CountDownLatch(1000);

    public static class ParseDate implements Runnable {
        int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            es.execute(new ParseDate(i));
        }
        cd.await();
        System.out.println("mission complete!");
        tl = null;
        System.gc();
        System.out.println("first GC complete!");
        // 在设置 ThreadLocal 的时候，会清除 ThreadLocalMap 中的无效对象
        tl = new ThreadLocal<SimpleDateFormat>();
        cd = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            es.execute(new ParseDate(i));
        }
        cd.await();
        Thread.sleep(1000);
        System.gc();
        System.out.println("secode GC complete!");
    }
}
