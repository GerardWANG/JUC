package juc.atguigu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache {
    private volatile Map<String, Object> map = new HashMap<>();
//    private Lock lock = new ReentrantLock();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //写操作，一定要原子 + 独占
    public void put(String key, Object value) {

        readWriteLock.writeLock().lock();

        try{
            System.out.println(Thread.currentThread().getName() + "\t 正在写入： " + key);
            //模拟网络延迟拥堵
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t 写入完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }


    }

    public void get(String key) {

        readWriteLock.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName() + "\t 正在读取： " + key);
            //模拟网络延迟拥堵
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            Object value = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t 读取完成" + value);
        }
        catch (Exception e) {
            e.printStackTrace();

        }finally {
            readWriteLock.readLock().unlock();
        }

    }
}



public class ReadWriteLockDemo {



    public static void main(String[] args){
        MyCache myCache = new MyCache();

        for(int i = 0; i <5; i++) {
            final int fInt = i;
            new Thread(()->{
                myCache.put(fInt + "", fInt + "");
            }
            ).start();
        }

        for(int i = 0; i <5; i++) {
            final int fInt = i;
            new Thread(()->{
                myCache.get(fInt + "");
            }
            ).start();
        }


    }
}
