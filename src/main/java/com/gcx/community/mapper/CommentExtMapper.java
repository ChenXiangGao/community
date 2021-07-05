package com.gcx.community.mapper;


import com.gcx.community.model.Comment;

public interface CommentExtMapper {

    int incCommentCount(Comment record);
}