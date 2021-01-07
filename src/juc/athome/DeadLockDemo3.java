package juc.athome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class HoldLockThread3 {

//    private int lock = 1;



    public void holdLock1(){

        synchronized (HoldLockThread3.class) {
            System.out.println(Thread.currentThread().getName() + "get " + HoldLockThread3.class);
            try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void holdLock2(){
        synchronized (HoldLockThread3.class) {
            System.out.println(Thread.currentThread().getName() + "get " + HoldLockThread3.class);
        }
    }
}

public class DeadLockDemo3 {
    public static void main(String[] args) {

        new Thread(()->{
            (new HoldLockThread3()).holdLock1();
        }).start();

        new Thread(()->{
            (new HoldLockThread3()).holdLock2();
        }).start();

    }
}
