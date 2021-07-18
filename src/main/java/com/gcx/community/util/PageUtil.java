package com.gcx.community.util;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageUtil {

    /**
     * 分页栏点击变动问题修复
     * @param pageInfo
     * @param page
     * @return
     */
    public static List pageFunc(PageInfo pageInfo, Integer page) {
        // 处理前端分页栏变动问题
        List pageNums = new ArrayList();
        if (page > 3 && pageInfo.getPages() > 5) { // 当前端页数超过3且查询的总页数大于5
            if (page > pageInfo.getPages() - 2) {// 如果前端的页数大于总页数-2
                Collections.addAll(pageNums, pageInfo.getPages()-4, pageInfo.getPages()-3,
                        pageInfo.getPages()-2, pageInfo.getPages()-1, pageInfo.getPages());
            } else {
                Collections.addAll(pageNums, page-2,page-1,
                        page,page+1,page+2);
            }
        } else { //前端的页面没有超过3页
            if (pageInfo.getPages() < 5) { //如果总页数小于5
                for(int i=0;i<pageInfo.getPages();i++)
                    pageNums.add(i+1);
            } else {
                pageNums.add("1");
                pageNums.add("2");
                pageNums.add("3");
                pageNums.add("4");
                pageNums.add("5");
            }
        }
        return pageNums;
    }
}
