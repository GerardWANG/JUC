package juc.athome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResource{
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
                System.out.println(Thread.currentThread().getName() + "\t" + i);
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
            while(number != 2) {
                c2.await();
            }
            for (int i=1; i<=10; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 3;
            c3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void t3Print15(){
        lock.lock();
        try{
            while(number != 3) {
                c3.await();
            }
            for (int i=1; i<=15; i++){
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 1;
            c1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

public class SyncAndReentrantLockDemo {


    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();

        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.t1Print5();
            }
        }).start();

        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.t2Print10();
            }
        }).start();




        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.t3Print15();
            }
        }).start();
    }
}
