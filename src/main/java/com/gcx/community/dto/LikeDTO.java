package com.gcx.community.dto;

import com.gcx.community.enums.LikeTypeEnum;

public class LikeDTO {
    //被点赞用户的id
    private String likedUserId;
    //点赞用户的id
    private String userId;
    //点赞的状态，默认未点赞
    private Integer status = LikeTypeEnum.UNLIKE.getCode();

    public LikeDTO() {
    }

    public LikeDTO(String likedUserId, String userId, Integer status) {
        this.likedUserId = likedUserId;
        this.userId = userId;
        this.status = status;
    }
}
