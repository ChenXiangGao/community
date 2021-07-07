package com.gcx.community.service;

import com.gcx.community.enums.LikeTypeEnum;
import com.gcx.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞数+1操作
     * @param likedUserId
     */
    public void incLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId,1);
    }

    /**
     * 点赞数-1操作
     * @param likedUserId
     */
    public void decLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId, -1);
    }

    /**
     * 将点赞操作记录到redis数据库中
     * @param likedUserId
     * @param userId
     */
    public void putLike2Redis(String likedUserId, String userId) {
        String likedKey = RedisKeyUtil.getEntityLikeKey(likedUserId, userId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, likedKey, LikeTypeEnum.LIKE.getCode());
    }

    /**
     * 将取消点赞操作记录到redis数据库中
     * @param likedUserId
     * @param userId
     */
    public void putUnlike2Redis(String likedUserId, String userId) {
        String likedKey = RedisKeyUtil.getEntityLikeKey(likedUserId, userId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, likedKey, LikeTypeEnum.UNLIKE.getCode());
    }

    /**
     * 获取某用户被用户点赞的状态
     * @param likedUserId
     * @param userId
     * @return
     */
    public Integer getLikedStatus(String likedUserId, String userId) {
        String likedKey = RedisKeyUtil.getEntityLikeKey(likedUserId, userId);
        return (Integer)redisTemplate.opsForHash().get(RedisKeyUtil.MAP_KEY_USER_LIKED, likedKey);
    }

    /**
     * 获取当前被点赞用户的点赞数量
     * @param likedUserId
     * @return
     */
    public Integer getLikedCount(String likedUserId) {
        return (Integer) redisTemplate.opsForHash().get(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId);
    }


//    /**
//     * 从redis数据库中拿到所有点赞数据并封装成列表
//     * 用于定时任务处理redis内的数据
//     * @return
//     */
//    public List<LikeDTO> getLikedDataFromRedis() {
//        List<LikeDTO> list = new ArrayList<>();
//        // 使用Cursor在key的hash中迭代，相当于迭代器。
//        Cursor<Map.Entry<String, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
//        while (cursor.hasNext()) {
//            Map.Entry<String, Object> entry = cursor.next();
//            String key = entry.getKey();
//            String[] split = key.split("::");
//            // 从key中分离出被点赞者和点赞者的id
//            String likedUserId = split[0];
//            String userId = split[1];
//            Integer status = (Integer) entry.getValue();
//            LikeDTO likeDTO = new LikeDTO(likedUserId, userId, status);
//            list.add(likeDTO);
//        }
//        return list;
//    }

//    /**
//     * 从redis数据库中拿到所有点赞数量数据并封装成列表
//     * 用于定时任务处理redis内的数据
//     * @return
//     */
//    public List<LikeCountDTO> getLikedCount() {
//        List<LikeDTO> list = new ArrayList<>();
//        // 使用Cursor在key的hash中迭代，相当于迭代器。
//        Cursor<Map.Entry<String, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
//        while (cursor.hasNext()) {
//            Map.Entry<String, Object> entry = cursor.next();
//            String key = entry.getKey();
//            Integer count = (Integer) entry.getValue();
//            LikeCountDTO likeCountDTO = new LikeCountDTO(likedUserId, count);
//            list.add(likeCountDTO);
//        }
//        return list;
//    }

}
