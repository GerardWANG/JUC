package juc.athome;

import java.util.concurrent.atomic.AtomicInteger;

public class CAS {
    public static void main(String[] args){
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data: " + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024) + "\t current data: " + atomicInteger.get());

    }
}
