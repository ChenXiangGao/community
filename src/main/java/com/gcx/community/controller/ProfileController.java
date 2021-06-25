package com.gcx.community.controller;

import com.gcx.community.model.User;
import com.gcx.community.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "2") Integer size) {

        User user = (User)request.getSession().getAttribute("user");
        if (user == null) return "redirect:/";

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
        }

        // 从qusetionService中获取到的pageInfo对象，不同的是这次获取的是同一个User提出的问题
        PageInfo pageInfo = questionService.list(user.getId(), page, size);
        model.addAttribute("pageInfo", pageInfo);

        // 处理前端分页栏变动问题
        List pageNums = new ArrayList();
        if (page > 3 && pageInfo.getPages() > 5) { // 当前端页数超过3且查询的总页数大于5
            if (page > pageInfo.getPages() - 2) {// 如果前端的页数大于总页数-2
                Collections.addAll(pageNums, pageInfo.getPages()-4, pageInfo.getPages()-3,
                        pageInfo.getPages()-2, pageInfo.getPages()-1, pageInfo.getPages());
            } else {
                Collections.addAll(pageNums, page-2,page-1,
                        page,page+1,page+2);
            }
        } else { //前端的页面没有超过3页
            if (pageInfo.getPages() < 5) { //如果总页数小于5
                for(int i=0;i<pageInfo.getPages();i++)
                    pageNums.add(i+1);
            } else {
                pageNums.add("1");
                pageNums.add("2");
                pageNums.add("3");
                pageNums.add("4");
                pageNums.add("5");
            }
        }
        model.addAttribute("pageNums", pageNums);

        return "profile";
    }
}
