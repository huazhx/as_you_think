package com.example.controller;

import com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mapper.EssaysMapper;
import com.example.pojo.Essays;
import com.example.pojo.Probability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
public class EssaysController {
    Gson gson = new Gson();
    @Autowired
    private EssaysMapper essayMapper;

    //分页查询所有的数据
    @GetMapping("/selectAll")
    public String selectAll(@RequestParam Map<String, Object> map) {
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        int page_number, start, end, base;
        String page = (String)map.get("page");
        base = 10;

        page = page == null ? "1" : page;
        page_number = Integer.parseInt(page);
        end = base * page_number;
        start = end - base;

        queryWrapper.gt("id", start);
        queryWrapper.lt("id", end + 1);

        List<Essays> res = essayMapper.selectList(queryWrapper);

        Long count = essayMapper.selectCount(null);
        LinkedHashMap<String, Object> all_data = new LinkedHashMap<>();
        all_data.put("totalCount", count);
        all_data.put("article", res);
        return gson.toJson(all_data);
    }

    //具体展示某一条数据
    @GetMapping("/selectSingle")
    public LinkedHashMap<String, Object> selectSingle(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        long id = Long.valueOf((String)map.get("id"));
        
        queryWrapper.eq("id", id);
        Essays article = essayMapper.selectOne(queryWrapper);

        LinkedHashMap<String, Object> all_data = new LinkedHashMap<>();
        all_data.put("code", 200);
        all_data.put("article", article);

        HttpSession session = request.getSession(true);
        session.setAttribute("id", id);

        return all_data;
        //return gson.toJson(all_data);
    }

    //提供各文章的各主题群概率
    @GetMapping("/showProbability")
    public LinkedHashMap<String, Object> showProbability(@RequestParam Map<String, Object> map) {
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        long id = Long.valueOf((String)map.get("id"));
        
        queryWrapper.eq("id", id);
        Essays article = essayMapper.selectOne(queryWrapper);

        String type = article.getType();

        String[] result = type.split(",");
        String[] key = type.split(",");

        LinkedHashMap<String, Object> probability  = new LinkedHashMap<>();
        LinkedHashMap<Integer, Object> all_data = new LinkedHashMap<>();

        for (int i = 1; i < 10; i++) {

            if(i == 9){
                result[i] = result[i].substring(1, result[i].length() - 1);
            }else {
                result[i] = result[i].substring(1, result[i].length());
            }
            key[i] = "topic" + (i - 1);

            Probability res = new Probability();
            res.setType(key[i]);
            res.setNumber(result[i]);
            all_data.put(i, res);
            //all_data.put(key[i], result[i]);
            

            probability.put("probability", all_data);
        }
        
        return probability;
    }
}
