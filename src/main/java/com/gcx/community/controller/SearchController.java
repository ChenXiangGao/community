package com.gcx.community.controller;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.model.Question;
import com.gcx.community.model.User;
import com.gcx.community.service.ElasticsearchService;
import com.gcx.community.service.QuestionService;
import com.gcx.community.service.UserService;
import com.gcx.community.util.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                         @RequestParam(name = "keyword", required = false) String keyword,
                         Model model) {

        // 搜索问题
        PageInfo pageInfo = elasticsearchService.searchQuestion(keyword, page, size);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("keyword", keyword);

        List pageNums = PageUtil.pageFunc(pageInfo, page);
        model.addAttribute("pageNums", pageNums);

        return "index";
    }

}
