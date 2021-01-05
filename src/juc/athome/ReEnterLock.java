package juc.atguigu;

import sun.rmi.runtime.NewThreadAction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEnterLock {

    static Lock lock = new ReentrantLock();

    //隐式同步代码块锁
    static Object objectLockA = new Object();

    //隐式同步代码块
    public static void m1()
    {
        new Thread(()-> {
            synchronized (objectLockA){
                System.out.println(Thread.currentThread().getName()+"\t"+"-----外层调用");
                synchronized (objectLockA){
                    System.out.println(Thread.currentThread().getName()+"\t"+"-----中层调用");
                    synchronized (objectLockA){
                        System.out.println(Thread.currentThread().getName()+"\t"+"-----内层调用");
                    }
                }
            }
        }).start();
    }

    //隐式同步方法
    public synchronized void m11() {
        System.out.println(Thread.currentThread().getName()+"\t"+"=====外层调用");
        this.m12();
    }

    public synchronized void m12() {
        System.out.println(Thread.currentThread().getName()+"\t"+"=====中层调用");
        this.m13();
    }

    public synchronized void m13() {
        System.out.println(Thread.currentThread().getName()+"\t"+"=====内层调用");
    }

    public static void main(String[] args){
        //隐式同步代码块，隐式其实说的就是synchronized关键字
        m1();

        //隐式同步方法
        new ReEnterLock().m11();

        //显式锁，显式其实表达的就是ReentrantLock
        new Thread(() -> {
            //加几次锁，就要释放几次锁
            lock.lock();
            lock.lock();
            try
            {
                System.out.println(Thread.currentThread().getName()+"\t"+"+++++外层");
                lock.lock();
                try
                {
                    System.out.println(Thread.currentThread().getName()+"\t"+"+++++内层");
                }
                finally {
                    lock.unlock();
                }
            }
            finally {
                lock.unlock();
                lock.unlock();
            }
        }
        ).start();

        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName()+"\t"+"|||||测试锁是否被正常释放");
            }
            finally {
                lock.unlock();
            }
        }
        ).start();
    }
}
