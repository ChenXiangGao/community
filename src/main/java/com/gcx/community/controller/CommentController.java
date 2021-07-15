package com.gcx.community.controller;

import com.gcx.community.dto.CommentDTO;
import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.dto.ResultDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.event.EventProducer;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.model.Comment;
import com.gcx.community.model.Event;
import com.gcx.community.model.User;
import com.gcx.community.service.CommentService;
import com.gcx.community.service.LikeService;
import com.gcx.community.service.QuestionService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private EventProducer eventProducer;

    /*
        @RequestBody:自动将commentDTO对象中的每个属性映射为json的key
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorReturn(CustomizeErrorCode.NO_LOGIN_IN);
        }
        if (commentDTO == null || StringUtil.isEmpty(commentDTO.getContent())) {
            return ResultDTO.errorReturn(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();

        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setCommentCount(0);
        comment.setLikeCount(0L);
        commentService.insert(comment);

        //触发评论事件
        Event event = new Event();
        event.setTopic("comment");
        event.setUserId(user.getId());
        // 区分回复的是评论还是问题
        event.setEntityType(comment.getType());
        event.setEntityId(comment.getId());
        QuestionDTO questionDTO = questionService.getById(commentDTO.getParentId());
        if (questionDTO != null) {
            event.setEntityUserId(questionDTO.getCreator());
            event.setData("questionId", questionDTO.getId());
        } else {
            Comment parentComment = commentService.selectById(commentDTO.getParentId());
            event.setEntityUserId(parentComment.getCommentator());
            event.setData("questionId", parentComment.getParentId());
        }
        eventProducer.fireEvent(event);

        return ResultDTO.successReturn();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id") Long id) {
        // 从数据库中拿到二级评论列表
        List<CommentUserDTO> commentDTOs = commentService.findAllByParentId(id, CommentTypeEnum.COMMENT);
        // 返回以供展示
        return ResultDTO.successReturn(commentDTOs);
    }
}
