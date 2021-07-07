package com.gcx.community.service;

import com.gcx.community.dto.CommentUserDTO;
import com.gcx.community.enums.CommentTypeEnum;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.exception.CustomizeException;
import com.gcx.community.mapper.*;
import com.gcx.community.model.Comment;
import com.gcx.community.model.CommentExample;
import com.gcx.community.model.Question;
import com.gcx.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 细分：被评论的问题或者评论是否还存在？
 * 需要再次进行捕捉异常
 * 所以需要创建一个枚举类对评论的父类id---问题和评论进行区分
 * 再进行type判断时需要用到多个mapper，因此需要再service中编写代码
 **/

@Service
public class CommentService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private UserMapper userMapper;


    @Transactional
    public void insert(Comment comment) {
        // 校验操作
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            //抛出异常
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isSame(comment.getType())) {
            //抛出异常
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        // 判断是回复的问题还是评论
        if (comment.getType().equals(CommentTypeEnum.QUESTION.getType())) {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //可能存在事务失败的问题，即插入评论成功但评论数未增加
            //所以需要设置事务回滚：@Transactional注解
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        } else {
            //回复评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        }
    }

    /**
     * 展示id的所有评论数据，包括评论人的name，头像，发起评论的时间，评论内容等
     * 查询question中id=？的type=1的所有评论，表示评论为评论问题，而不是二级评论
     *
     * @param id
     * @param type
     * @return 返回评论问题id=？并且是一级评论的所有评论信息及评论人信息
     */
    public List<CommentUserDTO> findAllByParentId(Long id, CommentTypeEnum type) {
        List<CommentUserDTO> commentUserDTOS = new ArrayList<>();
        //拿到评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        //错误校验
        if (comments == null || comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取评论中的数据并将其转换为CommentUserDTO对象
        for (Comment comment : comments) {
            User user = userMapper.selectByPrimaryKey(comment.getCommentator());
            CommentUserDTO commentUserDTO = new CommentUserDTO();
            BeanUtils.copyProperties(comment, commentUserDTO);
            commentUserDTO.setUser(user);
            commentUserDTOS.add(commentUserDTO);
        }
        return commentUserDTOS;
    }
}
