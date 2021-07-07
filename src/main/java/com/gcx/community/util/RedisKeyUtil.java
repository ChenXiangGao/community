package com.gcx.community.util;

/**
 * 根据规则生成key
 */
public class RedisKeyUtil {
    // 保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    // 保存用户点赞数的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";


    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。
     * @param likedUserId
     * @param userId
     * @return
     */
    public static String getEntityLikeKey(String likedUserId, String userId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(likedUserId);
        stringBuilder.append("::");
        stringBuilder.append(userId);
        return stringBuilder.toString();
    }
}