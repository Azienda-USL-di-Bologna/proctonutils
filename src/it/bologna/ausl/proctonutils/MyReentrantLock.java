package it.bologna.ausl.proctonutils;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Giuseppe De Marco (gdm)
 */
public class MyReentrantLock {
    private static final ReentrantLock lock = new ReentrantLock();
//    private static volatile boolean value = false;

    public void lock() {
        lock.lock();

    }
    
    public void unlock() {
        lock.unlock();

    }
    
}
