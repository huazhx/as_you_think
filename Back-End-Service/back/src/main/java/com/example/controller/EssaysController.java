package com.example.controller;

import com.google.gson.Gson;
import com.example.mapper.EssaysMapper;
import com.example.pojo.Essays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class EssaysController {
    Gson gson = new Gson();
    @Autowired
    private EssaysMapper essayMapper;

    // 测试
    @GetMapping("/test01")
    public String register() {
        List<Essays> res = essayMapper.selectList(null);
        return gson.toJson(res);
    }

}
