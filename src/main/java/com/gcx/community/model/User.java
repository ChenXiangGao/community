package com.gcx.community.model;

import lombok.Data;

/**
 * 负责管理数据库中的user表对象
 */

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String bio;
    private String avatarUrl;
}
