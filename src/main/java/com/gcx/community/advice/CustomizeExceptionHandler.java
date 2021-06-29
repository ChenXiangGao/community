package com.gcx.community.advice;

import com.gcx.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)//处理可拦截的所有类型的异常，那么不能拦截的异常该如何处理？（4xx，5xx等）
    ModelAndView handle(Throwable e, Model model) {
        if (e instanceof CustomizeException) {
            // 客户端问题的错误返回信息？
            model.addAttribute("message", e.getMessage());
        } else {
            // 服务器问题的错误返回信息
            model.addAttribute("message", "服务器冒烟了，请稍后再试");
        }

        return new ModelAndView("error");//出现错误后直接跳转到error页面
    }
}
