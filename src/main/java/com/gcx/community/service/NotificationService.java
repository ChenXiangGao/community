package com.gcx.community.service;

import com.alibaba.fastjson.JSONObject;
import com.gcx.community.dto.NotificationDTO;
import com.gcx.community.enums.NotificationStatusEnum;
import com.gcx.community.mapper.NotificationMapper;
import com.gcx.community.mapper.QuestionMapper;
import com.gcx.community.model.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public void createNotification(Notification notification) {
        notification.setContent(HtmlUtils.htmlEscape(notification.getContent()));
        notificationMapper.insert(notification);
//        System.out.println(isDone);
    }

    public PageInfo listByTypeName(User user, Integer page, Integer size, String typeName) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 从数据库中查询数据

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andTypeNameEqualTo(typeName)
                .andNotifierEqualTo(user.getId());
        notificationExample.setOrderByClause("gmt_create desc");//按创建时间倒序排列
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        // 获取分页信息
        PageInfo notificationPageInfo = new PageInfo<>(notifications);
        // 创建需要分页的questionDTOs
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            String content = HtmlUtils.htmlUnescape(notification.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setNotifierName(user.getName());
            long questionId = Long.parseLong(data.get("questionId").toString());//value的类型在转换的时候发生了变化
            int entityType = Integer.parseInt(data.get("entityType").toString());
            notificationDTO.setType(entityType);
            Question question = questionMapper.selectByPrimaryKey(questionId);
            notificationDTO.setOuterId(questionId);
            notificationDTO.setOuterTitle(question.getTitle());
            notificationDTOS.add(notificationDTO);
        }
        // 将封装后的列表放到分页对象中
        notificationPageInfo.setList(notificationDTOS);
        // 最后返回一个pageInfo对象
        return notificationPageInfo;
    }


    public long unReadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public long unReadCount(Long userId, String typeName) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andTypeNameEqualTo(typeName)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) return null;
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        String content = HtmlUtils.htmlUnescape(notification.getContent());
        Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setNotifierName(user.getName());
        long questionId = Long.parseLong(data.get("questionId").toString());//value的类型在转换的时候发生了变化
        int entityType = Integer.parseInt(data.get("entityType").toString());
        notificationDTO.setType(entityType);
        Question question = questionMapper.selectByPrimaryKey(questionId);
        notificationDTO.setOuterId(questionId);
        notificationDTO.setOuterTitle(question.getTitle());
        return notificationDTO;
    }
}
