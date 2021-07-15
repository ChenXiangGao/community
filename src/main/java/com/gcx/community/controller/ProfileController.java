package com.gcx.community.controller;

import com.gcx.community.model.User;
import com.gcx.community.service.NotificationService;
import com.gcx.community.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User)request.getSession().getAttribute("user");
        if (user == null) return "redirect:/";

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            // 从qusetionService中获取到的pageInfo对象，不同的是这次获取的是同一个User提出的问题
            PageInfo pageInfo = questionService.list(user.getId(), page, size);
            model.addAttribute("pageInfo", pageInfo);
        } else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
            PageInfo pageInfo = notificationService.listByTypeName(user, page, size,"comment" );
            long cUnReadCount = notificationService.unReadCount(user.getId(), "comment");
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("cUnReadCount", cUnReadCount);
        } else if ("likes".equals(action)) {
            model.addAttribute("section", "likes");
            model.addAttribute("sectionName", "我收到的点赞");
            PageInfo pageInfo = notificationService.listByTypeName(user, page, size,"like" );
            long lUnReadCount = notificationService.unReadCount(user.getId(), "like");
            model.addAttribute("lUnReadCount", lUnReadCount);
            model.addAttribute("pageInfo", pageInfo);
        }

        return "profile";
    }
}
