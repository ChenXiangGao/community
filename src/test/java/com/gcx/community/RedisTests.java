package com.gcx.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes() {
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    public void testLists() {
        String redisKey = "test:classroom";
        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 1));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSets() {
        String redisKey = "test:teacher";
        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "诸葛亮", "赵云");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));//随即弹出一个值
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testZSets() {
        String redisKey = "test:student";
        redisTemplate.opsForZSet().add(redisKey, "小红", 70);
        redisTemplate.opsForZSet().add(redisKey, "小蓝", 60);
        redisTemplate.opsForZSet().add(redisKey, "小金", 80);
        redisTemplate.opsForZSet().add(redisKey, "小白", 90);
        redisTemplate.opsForZSet().add(redisKey, "小黑", 50);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "小白"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "小白"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey, "小白"));
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 2));
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:student", 10, TimeUnit.SECONDS);
    }

    @Test
    public void testBoundOperation() {
        String redisKey = "test:count";
        BoundValueOperations boundValueOps = redisTemplate.boundValueOps(redisKey);
        boundValueOps.increment();
        boundValueOps.increment();
        boundValueOps.increment();
        boundValueOps.increment();
        boundValueOps.increment();
        System.out.println(boundValueOps.get());
    }

    /**
     * 编程式事务
     * redis的事务中不要执行查询
     */
    @Test
    public void testTransactional() {
        Object object = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey = "test:tx";
                //启动事务
                redisOperations.multi();

                redisOperations.opsForSet().add(redisKey, "zhangsan");
                redisOperations.opsForSet().add(redisKey, "lisi");
                redisOperations.opsForSet().add(redisKey, "wangwu");

                System.out.println(redisOperations.opsForSet().members(redisKey));

                return redisOperations.exec();
            }
        });
        System.out.println(object);
    }

    @Test
    public void testSB() {
        StringBuilder sb = new StringBuilder();
        sb.append("abc");
        sb.append(11L);
        sb.append("mnb");
        System.out.println(sb.toString());
    }
}
