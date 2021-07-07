package com.gcx.community.enums;

/**
 * 点赞状态枚举类
 * 区别当前被操作用户是否已被点赞
 */

public enum LikeTypeEnum {
    LIKE(1,"已点赞"),
    UNLIKE(0,"未点赞/取消赞"),
    ;
    private Integer code;
    private String message;

    LikeTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
