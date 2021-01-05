package juc.atguigu;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

public class SingletonDemo {


//    public static SingletonDemo instant = null;
    //这里加volatile是为了防止指令重排导致SingletonDemo对象未就绪前就被返回给其他线程使用，尽管这种情况极少出现
    public static volatile SingletonDemo instant = null;

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName() + "\t 我是SingletonDemo构造方法");
    }

    //在多线程时，instant不能保障是一个
    //可以用synchronized关键字在这里加锁，可以保障单例，但是开销大
    //可以用DCL(Double check lock双端检索机制)解决这个问题，开销小，只是部分代码块加锁
    public static SingletonDemo getInstant(){
        if(instant == null) {
            synchronized (SingletonDemo.class){
                if(instant == null) {
                    instant = new SingletonDemo();
                }
            }
        }
        return instant;
    }

//    public static SingletonDemo getInstant(){
//        if(instant == null) {
//            instant = new SingletonDemo();
//        }
//        return instant;
//    }

    public static void main(String[] args) {
//        System.out.println(SingletonDemo.getInstant() == SingletonDemo.getInstant());
//        System.out.println(SingletonDemo.getInstant() == SingletonDemo.getInstant());
//        System.out.println(SingletonDemo.getInstant() == SingletonDemo.getInstant());

        for(int i=0; i < 10; i++){
            new Thread(()->{
                SingletonDemo.getInstant();
            }
            ).start();
        }

    }

}
