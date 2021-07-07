package com.gcx.community.dto;

import com.gcx.community.model.User;
import lombok.Data;

/**
 * 评论人的信息数据类
 */

@Data
public class CommentUserDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
    private Integer likedStatus;
}
