package com.example.pojo;

import lombok.Data;

@Data
public class Essays {
    //文章id
    private long id;
    //文章标题
    private String title;
    //作者
    private String author;
    //日期
    private String date;
    //主题分类及其概率
    private String type;
    //文章内容
    private String content;
    //关键字
    private String keyWord;
}
