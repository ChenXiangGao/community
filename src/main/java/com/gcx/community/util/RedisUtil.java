//package com.gcx.community.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Set;
//
///**
// * Redis工具类，由于redis时非关系型数据库，其数据是存储在内存上的，因此并不用在数据访问层中定义方法
// * 直接在util包中定义我们的逻辑方法即可。
// */
//
//@Component
//public class RedisUtil {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    /**
//     * 普通的缓存获取
//     * @param key
//     * @return
//     */
//    public Long get(String key) {
//        return key == null ? null : (Long) redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 普通的缓存的放入
//     * @param key
//     * @param value
//     * @return
//     */
//    public boolean set(String key, Object value) {
//        try {
//            redisTemplate.opsForValue().set(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 实现递增操作
//     * @param key
//     * @param delta
//     * @return
//     */
//    public long incr(String key, long delta) {
//        if (delta < 0) {
//            throw new RuntimeException("递增因子必须大于0");
//        }
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    public boolean hasKey(String key) {
//        try {
//            return redisTemplate.hasKey(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * zet增加操作
//     * @param key
//     * @param value  属性值
//     * @param map    具体分数
//     * @return
//     */
//    public Boolean zsAdd(String key, String value, HashMap<String, Object> map){
//        try {
////            redisTemplate.opsForZSet().add("viewNum", "h1", Double.valueOf(h1.get("viewNum").toString()));
//
//            redisTemplate.opsForZSet().add(key, value, Double.valueOf(map.get(key).toString()));
//
//            return true;
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    /**
//     * zset给某个key某个属性增值操作
//     * @param key
//     * @param value  属性值
//     * @param delta  增加值
//     * @return
//     */
//    public Boolean zsIncr(String key, String value, Integer delta){
//        try {
//            redisTemplate.opsForZSet().incrementScore(key, value, delta);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    /**
//     * zset逆向排序
//     * @param key
//     * @return
//     */
//    public Set<Object> zsReverseRange(String key){
//        Set viewNum = redisTemplate.opsForZSet().reverseRange(key,0,-1);
//
//        return viewNum;
//
//    }
//
//    /**
//     * zscore 返回属性值
//     * @param key  key值
//     * @param value 属性值
//     * @return
//     */
//    public Double zscore(String key,String value){
//        Double score = redisTemplate.opsForZSet().score(key, value);
//        return score;
//    }
//
//}
