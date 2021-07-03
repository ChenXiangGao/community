package com.gcx.community.controller;

import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.service.CommentService;
import com.gcx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        // 根据传入的id获取到对应的questionDTO对象
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        // 展示当前id的评论列表
        List<CommentUserDTO> commentLists = commentService.findAllById(id);
        // 传入到前端页面，展示
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentLists);
        return "question";
    }
}
