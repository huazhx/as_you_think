package com.example.controller;

import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mapper.EssaysMapper;
import com.example.pojo.Essays;
import com.google.gson.Gson;

@CrossOrigin("*")
@RestController
public class SessionController {
    Gson gson = new Gson();
    @Autowired
    private EssaysMapper essayMapper;

    //具体展示某一条数据
    @GetMapping("/getArticle")
    public String selectSingle(HttpServletRequest request) {
        HttpSession session = request.getSession();
        long id = (long) session.getAttribute("id");

        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();  
        queryWrapper.eq("id", id);
        Essays article = essayMapper.selectOne(queryWrapper);

        LinkedHashMap<String, Object> all_data = new LinkedHashMap<>();
        all_data.put("article", article);
        all_data.put("code", 200);

        return gson.toJson(all_data);
    }
}
