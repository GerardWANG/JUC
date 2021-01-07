package juc.athome;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolDemo {

    public static void fixedThreadPoolTest() {
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        try {
            for (int i = 1; i <= 16; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }


    public static void singleThreadPoolTest() {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        try {
            for (int i = 1; i <= 16; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    public static void multiThreadPoolTest() {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try {
            for (int i = 1; i <= 16; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
                //这里让主线程睡50ms，表示不是那么多线程并行进来
                //这样测试就永远都是线程1在运行
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }





    public static void main(String[] args) {
        //1池8个处理线程test
//        fixedThreadPoolTest();

        //1池1个处理线程test
//        singleThreadPoolTest();

        //1池N个处理线程test
//        multiThreadPoolTest();

        /*

         */

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            for (int i = 1; i <= 20; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");

                });
            }

            try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

            for (int i = 1; i <= 5; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务2");
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }





    }
}

