package com.gcx.community.controller;

import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.dto.LikeDTO;
import com.gcx.community.dto.ResultDTO;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.model.User;
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
