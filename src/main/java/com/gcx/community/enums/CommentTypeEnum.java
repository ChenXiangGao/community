package com.gcx.community.enums;

/**
 * 问题以及评论的枚举类
 * 便于parentId的标注此评论是评论还是二级评论
 */

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    /**
     * 验证type是否与我们设定的一致
     * @param type
     * @return
     */
    public static boolean isSame(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) return true;
        }
        return false;
    }
}
