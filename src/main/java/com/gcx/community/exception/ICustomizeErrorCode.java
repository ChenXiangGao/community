package com.gcx.community.exception;

/**
 * 如果很多地方存在问题，那么就需要重写很多次抛出异常信息的代码
 * 那么我们可以定义一个通用的接口，用其具体的实现枚举类来记录不同业务的异常信息，然后供开发者处理(仿照HttpStatus)
 */
public interface ICustomizeErrorCode {
    String getMessage();
}
