package com.gcx.community.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gcx.community.enums.NotificationStatusEnum;
import com.gcx.community.model.Event;
import com.gcx.community.model.Notification;
import com.gcx.community.model.Question;
import com.gcx.community.service.ElasticsearchService;
import com.gcx.community.service.NotificationService;
import com.gcx.community.service.QuestionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @KafkaListener(topics = {"comment", "like"})
    public void handleNotification(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            System.out.println("通知的内容为空！");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            System.out.println("通知的格式错误！");
            return;
        }
        // 发送站内通知
        Notification notification = new Notification();
        notification.setNotifier(event.getUserId());
        notification.setReceiver(event.getEntityUserId());
        notification.setTypeName(event.getTopic());
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        notification.setContent(JSON.toJSONString(content));

        notificationService.createNotification(notification);
//        System.out.println("插入成功");
    }


    @KafkaListener(topics = {"publish"})
    public void handlePublish(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            System.out.println("发布的内容为空！");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            System.out.println("发布的格式错误！");
            return;
        }

        Question question = questionService.findById(event.getEntityId());

        elasticsearchService.saveQuestion(question);
        System.out.println("存入成功");
    }

}
