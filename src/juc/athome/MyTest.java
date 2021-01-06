package juc.athome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResources{
    public int number = 1;
    private Lock lock  = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void t1Print5(){
        lock.lock();
        try{
            while(number != 1) {
                c1.await();
            }
            for (int i=1; i<=5; i++){
//                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 2;
            c2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void t2Print10(){
        lock.lock();
        try{
//            System.out.println(number);
            while(number != 2) {
                c2.await();
            }
//            System.out.println(number);
            for (int i=1; i<=10; i++){
//                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 3;
            c3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }


    }

}

public class MyTest {


    public static void main(String[] args) {
        ShareResources shareResources = new ShareResources();

//        System.out.println("before");



        new Thread(()->{
            try{ TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            shareResources.t1Print5();
        },"Thread-A").start();

        new Thread(()->{
            shareResources.t2Print10();
//            System.out.println("after");
        },"Thread-B").start();



    }
}
