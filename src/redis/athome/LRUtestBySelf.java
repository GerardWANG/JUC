package redis.athome;

import java.util.HashMap;
import java.util.Map;

public class LRUtestBySelf {

    class Node<K,V>{
        K key;
        V value;
        Node<K,V> prev;
        Node<K,V> next;
        public Node(){
            this.prev=null;
            this.next=null;
        }
        public Node(K key, V value){
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    class DoubleLinkedList<K,V>{
        Node<K,V> head;
        Node<K,V> tail;
        public DoubleLinkedList(){
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev=head;
        }

        public void addHead(Node<K,V> node){
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        public void removeNode(Node<K,V> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.next = null;
            node.prev = null;
        }

        public Node getLast(){
            return tail.prev;
        }
    }

    private int cacheSize;

    Map<Integer, Node<Integer, Integer>> map;

    DoubleLinkedList<Integer,Integer> doubleLinkedList;

    public LRUtestBySelf(int cacheSize){
        this.cacheSize = cacheSize;
        this.map = new HashMap<>();
        this.doubleLinkedList = new DoubleLinkedList<>();
    }

    public int get(int key){
        if (!map.containsKey(key)){
            return -1;
        }
        Node<Integer, Integer> node = map.get(key);
        doubleLinkedList.removeNode(node);
        doubleLinkedList.addHead(node);
        return node.value;

    }

    public void put(int key, int value){
        if(map.containsKey(key)){
            Node<Integer, Integer> node = map.get(key);
            node.value = value;
            map.put(key, node);
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        }
        else{
            if (map.size() == cacheSize){
                Node<Integer,Integer> lastNode = doubleLinkedList.getLast();
                map.remove(lastNode.key);
                doubleLinkedList.removeNode(lastNode);
            }
            Node<Integer, Integer> newNode = new Node<>(key, value);
            map.put(key, newNode);
            doubleLinkedList.addHead(newNode);
        }
    }

    public static void main(String[] args) {
        LRUtestBySelf lrUtestBySelf = new LRUtestBySelf(2);
        lrUtestBySelf.put(1, 1);
        lrUtestBySelf.put(2, 2);
        System.out.println(lrUtestBySelf.get(1));
        System.out.println(lrUtestBySelf.map.keySet());
        lrUtestBySelf.put(3, 3);
        System.out.println(lrUtestBySelf.get(2));
        System.out.println(lrUtestBySelf.map.keySet());


    }

}
