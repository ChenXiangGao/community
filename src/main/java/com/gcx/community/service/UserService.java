package com.gcx.community.service;

import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.User;
import com.gcx.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 解决登录问题，在创建新的user对象时，需要先查询数据库中是否有这个user，无才创建
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        // 根据当前user的accountId到数据库进行查找
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        if (dbUsers.size() == 0) {
            // 否则就插入这个新的user
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            // 如果数据库中已存在了同一个accountId，只需根据accountId查到token，进行更新即可，
            User dbUser = dbUsers.get(0);
            User updateUser = new User();
            // 更新的地方
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            // updateUser表示要更新的内容
            // example对象中创建要执行的sql语句，即当与查到的userId相同时就进行更新
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }


    public User findUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
