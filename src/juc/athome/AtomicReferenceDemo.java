package juc.athome;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.util.concurrent.atomic.AtomicReference;


class User {

    String userName;
    int age;

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }
}

public class AtomicReferenceDemo {
    public static void main(String[] args){
        //如果是AtomicInteger，初始值是0，如果是引用类型，初始值是null
        AtomicReference<User> atomicReference =  new AtomicReference<>();

        User z3 = new User("z3", 22);
        User li4 = new User("li4", 25);
        atomicReference.set(z3);
        System.out.println(atomicReference.compareAndSet(z3, li4));
        System.out.println(atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, li4));
        System.out.println(atomicReference.get().toString());
    }
}
