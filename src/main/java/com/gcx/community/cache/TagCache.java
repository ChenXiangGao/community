package com.gcx.community.cache;

import com.gcx.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    /**
     * 设计标签库
     * @return
     */
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOs = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript", "php", "css", "html",  "java", "node.js", "python", "c++", "c",  "objective-c",  "shell",  "c#",  "bash", "less", "perl"));
        tagDTOs.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","springmvc","mybatis", "express", "django", "hadoop",  "struts"));
        tagDTOs.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "tomcat", "ubuntu", "centos",  "windows-server"));
        tagDTOs.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb",  "oracle",  "sqlserver", "postgresql", "sqlite"));
        tagDTOs.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode", "intellij-idea", "eclipse", "maven",  "svn"));
        tagDTOs.add(tool);

        return tagDTOs;
    }

    /**
     * 过滤非法标签
     * @param tags
     * @return
     */
    public static String filterInvalid(String tags) {
        String[] split = tags.split(",");
        List<TagDTO> tagDTOs = get();
        //拿到tagDTOs内部的tags列表,在组装成一个新的列表
        List<String> tagList = tagDTOs.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        //验证传入标签的合法性（非空和在标签库）
        String invalidList = Arrays.stream(split).filter(s -> StringUtils.isEmpty(s) || !tagList.contains(s)).collect(Collectors.joining(","));
        return invalidList;
    }
}
