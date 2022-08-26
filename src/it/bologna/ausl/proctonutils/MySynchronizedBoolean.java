package it.bologna.ausl.proctonutils;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Giuseppe De Marco (gdm)
 */
public class MySynchronizedBoolean {
    private static final ReentrantLock lock = new ReentrantLock();
    private static volatile boolean value = false;

    public boolean testAndSet(boolean testValue, boolean setValue) {
        lock.lock();
        try {
            if (value == testValue) {
                value = setValue;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }
    
    public boolean getValue() {
        lock.lock();
        try {
            return value;
        }
        finally {
            lock.unlock();
        }
    }
}
