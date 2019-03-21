package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Lee
 * @Date: 2019/03/20 10:42
 */
public class ReentrantLock_ implements Lock {

    private final Sync sync;

    abstract static class Sync {}

    /**
     * 非公平锁
     */
    static final class NonfairSync extends Sync {}

    public ReentrantLock_() {
        sync = new NonfairSync();
    }

    @Override
    public void lock() {

    }

    /**
     * 中断响应
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    /**
     * 申请等待限时
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
