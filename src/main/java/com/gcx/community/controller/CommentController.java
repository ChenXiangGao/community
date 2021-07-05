package com.gcx.community.controller;

import com.gcx.community.dto.CommentDTO;
import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.ResultDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.model.Comment;
import com.gcx.community.model.User;
import com.gcx.community.service.CommentService;
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
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment);
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
