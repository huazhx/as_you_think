package com.example.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Essays {
    private long id;
    private String title;
    private String author;
    private LocalDateTime time;
    private String type;
    private String content;
}
