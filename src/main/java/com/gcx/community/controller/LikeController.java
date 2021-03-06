package com.gcx.community.controller;

import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.LikeDTO;
import com.gcx.community.dto.ResultDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.event.EventProducer;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.model.Comment;
import com.gcx.community.model.Event;
import com.gcx.community.model.User;
import com.gcx.community.service.CommentService;
import com.gcx.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 点赞操作
     * @param likedUserId
     * @param request
     * @return
     */
    @RequestMapping(path = "/like/{likedUserId}")
    @ResponseBody
    public ResultDTO saveLikedRedis(@PathVariable("likedUserId") String likedUserId,
                                      HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorReturn(CustomizeErrorCode.NO_LOGIN_IN);
        }
        likeService.putLike2Redis(likedUserId, String.valueOf(user.getId()));
        likeService.incLikedCount(likedUserId);

        // 触发点赞事件
        if (likeService.getLikedStatus(likedUserId, user.getId().toString()) == 1) {
            Event event = new Event();
            event.setTopic("like");
            event.setUserId(user.getId());
            event.setEntityType(CommentTypeEnum.COMMENT.getType());
            event.setEntityId(Long.valueOf(likedUserId));
            Comment comment = commentService.selectById(Long.valueOf(likedUserId));
            event.setEntityUserId(comment.getCommentator());
            event.setData("questionId", comment.getParentId());
            eventProducer.fireEvent(event);
        }

        return ResultDTO.successReturn();
    }

    /**
     * 取消点赞操作
     * @param likedUserId
     * @param request
     * @return
     */
    @RequestMapping(path = "/unlike/{likedUserId}")
    @ResponseBody
    public ResultDTO saveUnLikedRedis(@PathVariable("likedUserId") String likedUserId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorReturn(CustomizeErrorCode.NO_LOGIN_IN);
        }
        likeService.putUnlike2Redis(likedUserId, String.valueOf(user.getId()));
        likeService.decLikedCount(likedUserId);
        return ResultDTO.successReturn();
    }

//    /**
//     * 从redis数据库中获取点赞数据，并封装返回
//     * @return
//     */
//    @GetMapping(path = "/getLikedData")
//    @ResponseBody
//    public ResultDTO getLikedData() {
//        List<LikeDTO> list = likeService.getLikedDataFromRedis();
////        System.out.println(list);
//        return ResultDTO.successReturn(list);
//    }
}
