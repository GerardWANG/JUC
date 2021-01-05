package juc.atguigu;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        //模拟3个资源
        Semaphore semaphore = new Semaphore(1);
        //6个线程抢3个资源
        for(int i = 1; i<=6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位");
                    //暂停一会儿线程，表示做一些内部逻辑
                    try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName() + "\t离开车位");
                    semaphore.release();
                }
            }).start();
        }
    }
}
