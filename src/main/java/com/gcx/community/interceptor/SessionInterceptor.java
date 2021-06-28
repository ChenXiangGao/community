package com.gcx.community.interceptor;

import com.gcx.community.mapper.UserMapper;
import com.gcx.community.model.User;
import com.gcx.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 此类本身不是spring接管的bean，所以自动注入不会生效
 * 因此我们可以加上一个service注解，把代码交给spring来管理
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    // 需要重写三个方法

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    // 在返回主页时，需要从token中去到user的值，并将其与数据库中的值进行比较
                    UserExample userExample = new UserExample(); // 创建一个userExample对象
                    userExample.createCriteria() //创建一个sql表达式
                            .andTokenEqualTo(token); // 拼接sql语句，这样就不用自己手写sql
                    List<User> users = userMapper.selectByExample(userExample);
                    // 当user不为null时，就可以写入到session中来展示
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
