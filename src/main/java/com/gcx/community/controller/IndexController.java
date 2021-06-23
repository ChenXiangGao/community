package com.gcx.community.controller;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.User;
import com.gcx.community.service.QuestionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "2") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    // 在返回主页时，需要从token中去到user的值，并将其与数据库中的值进行比较
                    User user = userMapper.findByToken(token);
                    // 当user不为null时，就可以写入到session中来展示
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        // 在返回主页面时，需要获取到问题列表中的信息
        PageInfo pageInfo = questionService.list(page, size);

        // 然后再把问题列表映射到前端
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

        return "index";
    }
}
