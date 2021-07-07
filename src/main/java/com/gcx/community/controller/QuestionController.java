package com.gcx.community.controller;

import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.model.User;
import com.gcx.community.service.CommentService;
import com.gcx.community.service.LikeService;
import com.gcx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model,
                           HttpServletRequest request) {
        // 根据传入的id获取到对应的questionDTO对象
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        // 展示当前id的评论列表
        List<CommentUserDTO> commentList = commentService.findAllByParentId(id, CommentTypeEnum.QUESTION);

        // 获取当前登录用户的id
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getId());
        for (CommentUserDTO commentUserDTO : commentList) {
            Integer likedStatus = likeService.getLikedStatus(String.valueOf(commentUserDTO.getId()), String.valueOf(user.getId()));
            Integer likedCount = likeService.getLikedCount(String.valueOf(commentUserDTO.getId()));
            System.out.println(likedStatus + "::" + likedCount);
            commentUserDTO.setLikedStatus(likedStatus);
            if (likedCount != null) {
                commentUserDTO.setLikeCount(Long.valueOf(likedCount));
            }
        }

        // 根据当前id问题的tag进行模糊查询，以展示相关问题
        List<QuestionDTO> relatedQuestionDTOs = questionService.selectRelated(questionDTO);
        // 传入到前端页面，展示
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentList);
        model.addAttribute("relatedList", relatedQuestionDTOs);
        return "question";
    }
}
