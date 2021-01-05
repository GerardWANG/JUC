package juc.atguigu;

class Person{
    private String name;
    public void changeString(Person p1) {
        System.out.println(p1);
        p1 = new Person();
        System.out.println(p1);
    }
}


public class test {
    public static void main(String[] args) {
        Person person = new Person();
        System.out.println(person);
        person.changeString(person);
        System.out.println(person);
    }
}
