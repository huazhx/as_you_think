package com.example.controller;

import com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mapper.EssaysMapper;
import com.example.pojo.Essays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class EssaysController {
    Gson gson = new Gson();
    @Autowired
    private EssaysMapper essayMapper;

    @GetMapping("/test01")
    public String register(@RequestParam Map<String, Object> map) {
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        int page_number, start, end, base;
        String page = (String)map.get("page");
        base = 10;

        page = page == null ? "1" : page;
        page_number = Integer.parseInt(page);
        end = base * page_number;
        start = end - base;

        queryWrapper.gt("id", start - 1);
        queryWrapper.lt("id", end + 1);

        List<Essays> res = essayMapper.selectList(queryWrapper);

        return gson.toJson(res);
    }

}
