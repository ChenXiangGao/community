package com.gcx.community.mapper;

import com.gcx.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);
}