package com.gcx.community.model;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String topic;
    private Long userId;
    private Integer entityType;
    private Long entityId;
    private Long entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityUserId() {
        return entityUserId;
    }

    public void setEntityUserId(Long entityUserId) {
        this.entityUserId = entityUserId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }
}
