package juc.atguigu;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {

        System.out.println("==============以下是ABA问题的产生===============");

        new Thread(()->{
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }
        ).start();

        new Thread(() ->{
            System.out.println(atomicReference.get());
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicReference.compareAndSet(100, 2019));
            System.out.println(atomicReference.get());
        }
        ).start();

        try{ TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("==============以下是ABA问题的解决===============");

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号： " + atomicStampedReference.getStamp());
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicStampedReference.compareAndSet(100, 101, 1, 2);
            System.out.println(Thread.currentThread().getName() + "\t第2次版本号： " + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, 2, 3);
            System.out.println(Thread.currentThread().getName() + "\t第3次版本号： " + atomicStampedReference.getStamp());
        }
        ).start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号： " + stamp);
            try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean result = atomicStampedReference.compareAndSet(100, 2019, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t修改成功否： " + result + "\t当前实际版本号： " + atomicStampedReference.getStamp());

            System.out.println(Thread.currentThread().getName() + "\t当前实际最新值："+atomicStampedReference.getReference());
        }
        ).start();

    }
}
