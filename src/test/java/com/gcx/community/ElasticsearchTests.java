package com.gcx.community;

import com.gcx.community.mapper.QuestionMapper;
import com.gcx.community.model.Question;
import com.gcx.community.model.QuestionExample;
import com.gcx.community.model.elasticsearch.QuestionRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Test
    public void testInsert() {
        questionRepository.save(questionMapper.selectByPrimaryKey(26L));
        questionRepository.save(questionMapper.selectByPrimaryKey(27L));
        questionRepository.save(questionMapper.selectByPrimaryKey(28L));
        questionRepository.save(questionMapper.selectByPrimaryKey(29L));
        System.out.println("执行完毕");
    }

    @Test
    public void testInsertList() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andIdBetween(1L, 24L);
        questionRepository.saveAll(questionMapper.selectByExample(questionExample));
        System.out.println("执行完毕");
    }

}
