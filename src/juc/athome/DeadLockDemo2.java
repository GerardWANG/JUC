package juc.athome;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class HoldLockThread2 {

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public void holdLock1(){
        lock1.lock();
        try{
            System.out.println(Thread.currentThread().getName() + " Hold lock1");
            //停1s是为了让Thread-B将锁lock2占住，这样的话就会形成死锁，即Thread-A占着lock1等着lock2，Thread-2B占着lock2等着lock1
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            lock2.lock();
            try{
                System.out.println(Thread.currentThread().getName() + " Hold lock2");
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                lock2.unlock();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock1.unlock();
        }
    }

    public void holdLock2(){
        lock2.lock();
        try{
            System.out.println(Thread.currentThread().getName() + " Hold lock2");
            lock1.lock();
            try{
                System.out.println(Thread.currentThread().getName() + " Hold lock1");
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                lock1.unlock();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            lock2.unlock();
        }
    }

}

public class DeadLockDemo2 {



    public static void main(String[] args) {
        HoldLockThread2 holdLockThread2 = new HoldLockThread2();
        new Thread(()->{
            holdLockThread2.holdLock1();
        },"Thread-A").start();

        new Thread(()->{
            holdLockThread2.holdLock2();
        },"Thread-B").start();

    }
}
