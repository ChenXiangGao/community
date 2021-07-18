package com.gcx.community.service;

import com.gcx.community.dto.QuestionDTO;
import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.Question;
import com.gcx.community.model.User;
import com.gcx.community.model.elasticsearch.QuestionRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private UserMapper userMapper;

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    public PageInfo searchQuestion(String keyword, Integer page, Integer size) {

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "description"))
                .withSort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC))
                .withPageable(PageRequest.of(page-1, size))
                .build();

        SearchHits<Question> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Question.class);
        List<Question> questions = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        long total = searchHits.getTotalHits();

        PageInfo pageInfo = new PageInfo();

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        pageInfo.setList(questionDTOS);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(size);
        pageInfo.setPages(total== 0 ? 0: (int) (total % size == 0 ? total / size : (total / size) + 1));
        pageInfo.setHasPreviousPage(pageInfo.getPageNum() > 1);
        pageInfo.setHasNextPage(pageInfo.getPageNum() < pageInfo.getPages());


        return pageInfo;
    }
}
