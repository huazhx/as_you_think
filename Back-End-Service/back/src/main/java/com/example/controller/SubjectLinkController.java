package com.example.controller;

import com.google.gson.Gson;
import com.example.mapper.SubjectLinkMapper;
import com.example.pojo.SubjectLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class SubjectLinkController {
    Gson gson = new Gson();
    @Autowired
    private SubjectLinkMapper subjectLinkMapper;

    // 测试
    @GetMapping("/test")
    public String register() {
        List<SubjectLink> res = subjectLinkMapper.selectList(null);
        return gson.toJson(res);
    }

}
