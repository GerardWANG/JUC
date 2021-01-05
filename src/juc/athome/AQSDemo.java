package juc.atguigu;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        //A顾客就是第一个顾客，此时受理窗口没有任何人，A可以直接去办理
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----A thread come in");
                try { TimeUnit.MINUTES.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            finally {
                lock.unlock();
            }
        }
        ).start();

        //第2个顾客，第2个线程，由于受理业务的窗口只有一个（只能一个线程持有锁），此时B只能等待
        //进入候客区
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----B thread come in");
                try { TimeUnit.MINUTES.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            finally {
                lock.unlock();
            }
        }
        ).start();

        //第3个顾客，第3个线程，由于受理业务的窗口只有一个（只能一个线程持有锁），此时C只能等待
        //进入候客区
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----B thread come in");
                try { TimeUnit.MINUTES.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            finally {
                lock.unlock();
            }
        }
        ).start();
    }
}
