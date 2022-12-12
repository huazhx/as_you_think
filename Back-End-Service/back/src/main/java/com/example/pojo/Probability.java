package com.example.pojo;

import lombok.Data;

@Data
public class Probability {
    //主题号
    private String name;
    //主题概率
    private double value;
    //包含主题词
    private String other;
}
