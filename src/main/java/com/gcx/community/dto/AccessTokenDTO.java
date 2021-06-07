package com.gcx.community.dto;

import lombok.Data;

/**
 * 定义从github授权验证api 拿到的 accesstoken 数据传输对象
 */

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
