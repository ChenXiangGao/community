package com.gcx.community.controller;

import com.gcx.community.service.ElasticsearchService;
import com.gcx.community.service.QuestionService;
import com.gcx.community.util.PageUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/")
    public String index(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        if (keyword == null || keyword.equals("")) {
            // 在返回主页面时，需要获取到问题列表中的信息
            PageInfo pageInfo = questionService.list(page, size);

            // 然后再把问题列表映射到前端
            model.addAttribute("pageInfo", pageInfo);

            List pageNums = PageUtil.pageFunc(pageInfo, page);
            model.addAttribute("pageNums", pageNums);
        } else {
            // 搜索问题
            PageInfo pageInfo = elasticsearchService.searchQuestion(keyword, page, size);

            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("keyword", keyword);

            List pageNums = PageUtil.pageFunc(pageInfo, page);
            model.addAttribute("pageNums", pageNums);
        }




        return "index";
    }
}
