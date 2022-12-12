package com.example.pojo;

import lombok.Data;

@Data
public class Essays {
    private long id;
    private String title;
    private String author;
    private String date;
    private String type;
    private String content;
    private String keyWord;
}
