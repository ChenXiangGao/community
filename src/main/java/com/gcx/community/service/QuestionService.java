package com.gcx.community.service;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.mapper.QuestionMapper;
import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.Question;
import com.gcx.community.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 中间层的作用，负责组装、调用、使用数据库对象以及其对象的mapper
 */

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageInfo list(Integer page, Integer size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 从数据库中查询数据
        List<Question> questions = questionMapper.list();
        // 获取分页信息
        PageInfo questionPageInfo = new PageInfo<>(questions);
        // 创建需要分页的questionDTOs
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            // 将question对象中的属性全部赋值到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        // 将封装后的列表放到分页对象中
        questionPageInfo.setList(questionDTOS);

        // 最后返回一个pageInfo对象
        return questionPageInfo;
    }

    public PageInfo list(Integer userId,Integer page, Integer size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 从数据库中查询数据
        List<Question> questions = questionMapper.listByUserId(userId);
        // 获取分页信息
        PageInfo questionPageInfo = new PageInfo<>(questions);
        // 创建需要分页的questionDTOs
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            // 将question对象中的属性全部赋值到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        // 将封装后的列表放到分页对象中
        questionPageInfo.setList(questionDTOS);

        // 最后返回一个pageInfo对象
        return questionPageInfo;
    }


    public QuestionDTO getById(Integer id) {
        // 根据id从数据库中查询到question对象
        Question question = questionMapper.getById(id);
        // 根据问题的创建者id到user表中拿到该id的所有信息
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        // 然后将question中的属性赋值到questionDTO中以便展示
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
}
