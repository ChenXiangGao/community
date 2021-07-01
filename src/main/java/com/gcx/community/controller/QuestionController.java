package com.gcx.community.controller;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {
        // 根据传入的id获取到对应的questionDTO对象
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        // 传入到前端页面，展示
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
