package juc.athome;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData{
    //这里为何不加volatile?
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try{
            //先判断，多线程的判断用while
            while (number != 0) {
                condition.await();
            }
            //干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t" + number);
            //这里之所以要signalAll是因为如果是多个生产者和多个消费者
            //因为1个生产者+1后，再进来的又是个生产者，触发await，然后就再也等不到被唤醒了，因为消费者被锁在外面了
            //如果是1个生产者，1个消费者用signal就没有问题
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try{
            //先判断，多线程的判断用while
            while (number == 0) {
                condition.await();
            }
            //干活
            number--;
            System.out.println(Thread.currentThread().getName()+"\t" + number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class ProdConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        //生产者1
        new Thread(()->{
            for(int i=0; i<5; i++){
                try{
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //消费者1
        new Thread(()->{
            for(int i=0; i<5; i++){
                try{
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //生产者1
        new Thread(()->{
            for(int i=0; i<5; i++){
                try{
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //消费者2
        new Thread(()->{
            for(int i=0; i<5; i++){
                try{
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
