package com.example.controller;

import com.google.gson.Gson;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class UserController {
    Gson gson = new Gson();
    @Autowired
    private UserMapper userMapper;

    // 测试
    @GetMapping("/test")
    public String register() {
        List<User> res = userMapper.selectList(null);
        return gson.toJson(res);
    }

}
