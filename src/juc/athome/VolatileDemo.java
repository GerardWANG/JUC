package juc.athome;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyData {
    //这里如果不加volatile关键字的话，main线程就得不到变量被更改的通知
    volatile int number = 0;
    public void addTo60(){
        this.number = 60;
    }
    public void addPlusPlus(){
        number++;
    }

    //如果不用lock或者synchronized关键字的话，用下面这种方式也能保障原子性，性能会比加锁高一些
    AtomicInteger atomicInteger = new AtomicInteger();

    public void addAtomic(){
        atomicInteger.getAndIncrement();
    }
}


//验证volatile的可见性
public class VolatileDemo {
    public static void main(String[] args){
        //Volatile保障可见性
        seeOkByVolatile();

//        MyData myData = new MyData();
//
//        for(int i = 0; i < 20; i++) {
//            new Thread(()->{
//                for(int j = 0; j < 1000; j++) {
//                    myData.addPlusPlus();
//                    myData.addAtomic();
//                }
//            }
//            ).start();
//        }
//
//        //等待线程全部完成计算用这个方法，>2是因为默认后台有两个线程，main和gc
//        while(Thread.activeCount() > 2){
//            //礼让线程，main线程不继续执行
//            Thread.yield();
//        }
//
//        System.out.println(Thread.currentThread().getName() + "\t int type, finally number value: " + myData.number);
//        System.out.println(Thread.currentThread().getName() + "\t atomicInteger type, finally number value: " + myData.atomicInteger);

    }

    //可以保障可见性，及时通知其他线程，主内存的值已被修改
    public static void seeOkByVolatile(){
        //在主内存中创建变量
        MyData myData = new MyData();

        //Thread1读取主内存变量并修改
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            myData.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t updated number value: " + myData.number);
        }).start();

        System.out.println(Thread.currentThread().getName() + "\t come in");
        //看main线程是否被通知到，没有则死循环
        while(myData.number == 0){

        }

        System.out.println(Thread.currentThread().getName() + "\t mission complated, main get number value: " + myData.number);
    }

}
