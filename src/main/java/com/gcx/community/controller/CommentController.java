package com.gcx.community.controller;

import com.gcx.community.dto.CommentDTO;
import com.gcx.community.dto.ResultDTO;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.exception.CustomizeException;
import com.gcx.community.mapper.CommentMapper;
import com.gcx.community.model.Comment;
import com.gcx.community.model.User;
import com.gcx.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
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
        Comment comment = new Comment();

        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.successReturn();
    }
}
