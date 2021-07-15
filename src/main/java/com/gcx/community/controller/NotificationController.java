package com.gcx.community.controller;

import com.gcx.community.dto.NotificationDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.model.User;
import com.gcx.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (notificationDTO == null) {
            return "redirect:/";
        }

        if (notificationDTO.getType() == CommentTypeEnum.COMMENT.getType() ||
                notificationDTO.getType() == CommentTypeEnum.QUESTION.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterId();
        }

        return null;
    }
}
