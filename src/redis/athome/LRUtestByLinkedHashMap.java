package redis.athome;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUtestByLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    private int capacity;

    /**
     * 最关键的就是这个accessOrder，一定要配置为true
     * 只有它为true才会把最近访问过的key放到最优先位置
     * @param capacity
     */
    public LRUtestByLinkedHashMap(int capacity){
        super(capacity,0.75F, true);
        this.capacity=capacity;
    }

    /**
     * 注意这里为什么只是返回了true或者false，请参考removeEldestEntry的javaDoc
     * 这里仅仅需要返回true或false即可，不用写怎么删的逻辑
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > capacity;
    }

    public static void main(String[] args) {
        LRUtestByLinkedHashMap lrUtest = new LRUtestByLinkedHashMap(3);
        lrUtest.put(1, "a");
        lrUtest.put(2, "b");
        lrUtest.put(3, "c");
        System.out.println(lrUtest.keySet());
        lrUtest.put(4, "d");
        System.out.println(lrUtest.keySet());

    }
}
