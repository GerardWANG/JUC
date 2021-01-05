package juc.atguigu;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception{
        closeDoor();

    }

    public static void closeDoor() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for(int i=0; i<6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "\t上完自习，离开教室");
                countDownLatch.countDown();
            }
            ).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t班长最后关门走人");
    }

}
