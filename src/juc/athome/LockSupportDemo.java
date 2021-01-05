package juc.athome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupportDemo {

    static Object objectLock = new Object();
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    static Lock lock2 = new ReentrantLock();
    static Condition condition2 = lock2.newCondition();

    public static void synchronizedWaitNotify()
    {
        System.out.println(Thread.currentThread().getName()+"\t"+"=====Main process step1");

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName()+"\t"+"=====come in");
                try {
                    objectLock.wait();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t"+"=====waked up");
            }
        }
        ).start();

        System.out.println(Thread.currentThread().getName()+"\t"+"=====Main process step2");

        new Thread(()->{
            //上面的线程睡2s，这里睡1s，所以这里会先调用到，先notify了
            //于是上面的线程就死锁了，因为对于wait/notify机制来讲，必须先wait再notify
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objectLock){
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t"+"=====wake up");
            }
        }
        ).start();

        System.out.println(Thread.currentThread().getName()+"\t"+"=====Main process step3");
    }

    public static void lockAwaitSignal()
    {
        new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t"+"-----A come in");
                try{
                    condition.await();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t"+"-----A waked up");
            }
            finally {
                lock.unlock();
            }
        }
        ).start();


        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.lock();

            System.out.println(Thread.currentThread().getName()+"\t"+"-----B come in");

            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"\t"+"-----B wake up");
            }
            finally {
                lock.unlock();
            }
        }
        ).start();
    }

    public static void lockSupport(){
        Thread threadA = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"-----A come in" + System.currentTimeMillis());
            LockSupport.park();//先通知的话，这里的阻塞则不会执行
            System.out.println(Thread.currentThread().getName()+"\t"+"-----A waked up" + System.currentTimeMillis());
        }
        );
        threadA.start();



        Thread threadB = new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"-----B come in");
            LockSupport.unpark(threadA);
            System.out.println(Thread.currentThread().getName()+"\t"+"-----B wake up A");
        }
        );
        threadB.start();
    }


    public static void main(String[] args){
        //测试synchronized/wait/notify机制
//        synchronizedWaitNotify();
        //测试lock/await/signal机制
        lockAwaitSignal();
        //测试lockSupport机制
//        lockSupport();

    }
}
