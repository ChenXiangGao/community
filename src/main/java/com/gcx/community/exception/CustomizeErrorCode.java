package com.gcx.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001, "您找的问题不存在，要不换一个？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN_IN(2003, "您还没有登录，不能进行该操作"),
    SYSTEM_ERROR(2004, "服务器冒烟了，请稍后再试"),
    TYPE_PARAM_WRONG(2005, "评论的类型错误或者不存在"),
    COMMENT_NOT_FOUND(2006, "评论不存在，要不换一个？"),
    COMMENT_IS_EMPTY(2007, "评论不能为空！"),
    ;

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
