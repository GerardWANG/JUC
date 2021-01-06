package juc.athome;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
    //表示生产与消费的开始与终止
    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue){
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception {
        String data = null;
        boolean retValue;
        while(FLAG){
            //String写外面，因为这里是个while
            data = atomicInteger.incrementAndGet() + "";
            //一般工作中用这个offer方法，但是失败的话，这一条就没了
            retValue = blockingQueue.offer(data,2, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + data + "成功");
            }
            else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + data + "失败");
            }
//            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t生产叫停，表示FLAG=false，生产结束");
    }

    public void myConsumer() throws Exception{
        String result = null;
        while (FLAG){
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if(null == result || result.equalsIgnoreCase("")){
                //这里写False主要是考虑到当生产者不生产或生产错误后，将FLAG置为false，通知生产者退出
                //其实也有不合理的地方，Prod环境要是直接就这么退了，肯定不行
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t 超过2s没有取到消费品，消费退出");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列" + result + "成功");
        }
    }

    public void stop() throws Exception{
        this.FLAG = false;
    }
}

public class ProdConsumer_BlockQueueDemo {
    public static void main(String[] args) throws Exception{
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
            try{
                myResource.myProd();
            }catch(Exception e){
                e.printStackTrace();
            }
        },"Prod").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
            try{
                myResource.myConsumer();
            }catch(Exception e){
                e.printStackTrace();
            }
        },"Consumer").start();

        try{ TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }

        myResource.stop();
    }
}
