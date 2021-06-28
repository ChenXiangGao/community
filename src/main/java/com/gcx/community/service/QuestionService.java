package com.gcx.community.service;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.mapper.QuestionMapper;
import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.Question;
import com.gcx.community.model.QuestionExample;
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

    /**
     * 查询到对应的分页对象
     * 用途：主页 展示问题列表
     * @param page
     * @param size
     * @return
     */
    public PageInfo list(Integer page, Integer size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 从数据库中查询数据
        List<Question> questions = questionMapper.selectByExample(null);
        // 获取分页信息
        PageInfo questionPageInfo = new PageInfo<>(questions);
        // 创建需要分页的questionDTOs
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
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

    /**
     * 根据userId查询到对应的分页对象
     * 用途：我的问题 展示问题列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PageInfo list(Integer userId,Integer page, Integer size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 从数据库中查询数据
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                        .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        // 获取分页信息
        PageInfo questionPageInfo = new PageInfo<>(questions);
        // 创建需要分页的questionDTOs
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
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

    /**
     * 根据对应的id查询到对应的questionDTO对象
     * 用途：问题详情页面
     * @param id
     * @return
     */
    public QuestionDTO getById(Integer id) {
        // 根据id从数据库中查询到question对象
        Question question = questionMapper.selectByPrimaryKey(id);
        // 根据问题的创建者id到user表中拿到该id的所有信息
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        // 然后将question中的属性赋值到questionDTO中以便展示
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 问题的创建与更新
     * @param question
     */
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 数据库中没有该问题，创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            // 数据库已有该问题，更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }
}
