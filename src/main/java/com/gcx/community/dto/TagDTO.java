package com.gcx.community.dto;

import lombok.Data;

import java.util.List;

/**
 * 标签数据传输对象
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
