package com.gcx.community.service;

import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 解决登录问题，在创建新的user对象时，需要先查询数据库中是否有这个user，无才创建
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        // 根据当前user的accountId到数据库进行查找
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            // 否则就插入这个新的user
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            // 如果数据库中已存在了同一个accountId，只需根据accountId查到token，进行更新即可，
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            userMapper.update(dbUser);
        }
    }
}
