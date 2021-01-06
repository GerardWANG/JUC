package juc.athome;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

//获得线程的方式之二，之一是继承Thread类，但是那个几乎没人用
class MyThread1 implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

class MyThread2 implements Callable<Integer>
{
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "**********come in Callable");
        try{ TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        return 200;
    }
}

public class CallableDemo {
    public static void main(String[] args) throws Exception{
        //FutureTask和Runnable那个不一样，一个FutureTask对象只能启动一个线程
        //但是Runnable那个类对象可以new出来多个线程
        //这里泛型填的是Integer，跟Callable类中call的返回值一致
        FutureTask<Integer> futureTask1 = new FutureTask<>(new MyThread2());

        new Thread(futureTask1, "Thread-A").start();

        int myValue = 400;
        //不用担心这个get()在线程未完成之前就到这里了，如果线程A未完成，会阻塞在这里
        //但是这个get()最好在程序最后，因为如果主线程阻塞了，就无法继续往下走了（本来下面可以干更多的事）
        int calValue = futureTask1.get();
        System.out.println("*********result: " + (myValue+calValue));
    }
}
