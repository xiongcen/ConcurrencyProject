package com.xiongcen.test;

import java.util.*;

/**
 * Created by xiongcen on 17/1/22.
 */
public class HashMapIteratorTest {

    public static void main(String[] args) {
        int val = 0;
        String key = null;
        Integer value = null;
//        Random r = new Random();
        HashMap map = new HashMap();

        for (int i=0; i<1000; i++) {
            // 随机获取一个[0,100)之间的数字
//            val = r.nextInt(100);

            key = String.valueOf(i);
            value = i+1;
            // 添加到HashMap中
            map.put(key, value);
            System.out.println(" key:"+key+" value:"+value);
        }
        // 通过entrySet()遍历HashMap的key-value
        iteratorHashMapByEntryset(map) ;

        // 通过keySet()遍历HashMap的key-value
        iteratorHashMapByKeyset(map) ;

        // 单单遍历HashMap的value
        iteratorHashMapJustValues(map);
    }

    /*
     * 通过entry set遍历HashMap
     * 效率高!
     */
    private static void iteratorHashMapByEntryset(HashMap map) {
        if (map == null)
            return ;

        System.out.println("\niterator HashMap By entryset");
        String key = null;
        Integer integ = null;
        long l = System.currentTimeMillis();
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();

            key = (String)entry.getKey();
            integ = (Integer)entry.getValue();
            System.out.println(key+" -- "+integ.intValue());
        }
        System.out.println("通过entry set遍历HashMap花费时间:"+(System.currentTimeMillis()-l));
    }

    /*
     * 通过keyset来遍历HashMap
     * 效率低!
     */
    private static void iteratorHashMapByKeyset(HashMap map) {
        if (map == null)
            return ;

        System.out.println("\niterator HashMap By keyset");
        String key = null;
        Integer integ = null;
        long l = System.currentTimeMillis();
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            key = (String)iter.next();
            integ = (Integer)map.get(key);
            System.out.println(key+" -- "+integ.intValue());
        }
        System.out.println("通过keyset来遍历HashMap花费时间:"+(System.currentTimeMillis()-l));
    }


    /*
     * 遍历HashMap的values
     */
    private static void iteratorHashMapJustValues(HashMap map) {
        if (map == null)
            return ;

        Collection c = map.values();
        Iterator iter= c.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}