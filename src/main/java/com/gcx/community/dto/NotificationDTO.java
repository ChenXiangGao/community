package com.gcx.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Integer type;
    private Long outerId;
    private Integer status;
    private Long gmtCreate;
    private String notifierName;
    private String outerTitle;
    private String typeName;
}
