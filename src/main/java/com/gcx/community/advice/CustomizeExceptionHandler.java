package com.gcx.community.advice;

import com.gcx.community.dto.ResultDTO;
import com.gcx.community.exception.CustomizeErrorCode;
import com.gcx.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)//处理可拦截的所有类型的异常，那么不能拦截的异常该如何处理？（4xx，5xx等）
    @ResponseBody
    Object handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            // 特殊的业务处理：返回JSON格式的结果，再跳转到错误页面
            ResultDTO resultDTO;
            // 返回json
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorReturn(((CustomizeException) e));
            } else {
                resultDTO = ResultDTO.errorReturn(CustomizeErrorCode.SYSTEM_ERROR);
            }
            return resultDTO;
        } else {
            // 页面级的错误页面跳转
            if (e instanceof CustomizeException) {
                // 客户端问题的错误返回信息
                model.addAttribute("message", e.getMessage());
            } else {
                // 服务器问题的错误返回信息
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }

            return new ModelAndView("error");//出现错误后直接跳转到error页面
        }
    }
}
