package com.gcx.community.controller;

import com.gcx.community.dto.AccessTokenDTO;
import com.gcx.community.dto.GithubUser;
import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.User;
import com.gcx.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           // 做持久化登录需要将token存入到response中
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        // 一个坑：注意命名和配置的一致性，出错就不会返回token而是一个html页面
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            User user = new User();
            // 用来代替session的功能
            // 当我们登录成功以后，cookie中有很多key-value对，
            // Java项目会默认创建一个JSession并将其request中的Session绑定在一起进行登陆验证。
            // 我们要做的是手动的写一个key-value，并且可以识别出其包含的session和user，再回到数据库进行查找进行登录验证
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            // 插入数据库
            userMapper.insert(user);
            // 写入cookie
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            // 否则，就直接进行登录
            return "redirect:/";
        }
    }
}
