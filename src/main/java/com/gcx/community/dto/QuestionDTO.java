package com.gcx.community.dto;

import com.gcx.community.model.User;
import lombok.Data;


/**
 * 在类与类之间传输实体类
 * 在主页面展示问题列表时，需要返回question对象的内容，并且其中的creator与User中的id相关联
 * 所以就需要创建一个数据传输对象
 */

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
