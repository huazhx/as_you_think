package com.example.controller;

import com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mapper.EssaysMapper;
import com.example.mapper.TopicMapper;
import com.example.pojo.Essays;
import com.example.pojo.Probability;
import com.example.pojo.Topic;

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

    @Autowired
    private TopicMapper topicMapper;

    //分页查询所有的数据
    @GetMapping("/selectAll")
    public String selectAll(@RequestParam Map<String, Object> map) {
        //获取文章的页码
        String page = (String)map.get("page");

        //确定文章页码
        page = page == null ? "1" : page;
        int page_number = Integer.parseInt(page);

        //每一页的文章数
        int base = 10;
        //每一页的结束文章
        int end = base * page_number;
        //每一页的起始文章
        int start = end - base;

        //获取每一页的文章内容
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", start);
        queryWrapper.lt("id", end + 1);
        List<Essays> res = essayMapper.selectList(queryWrapper);
        
        //计算总文章数
        Long count = essayMapper.selectCount(null);

        //创建输出对象
        LinkedHashMap<String, Object> all_data = new LinkedHashMap<>();
        all_data.put("totalCount", count);
        all_data.put("article", res);

        return gson.toJson(all_data);
    }


    //具体展示某一条数据
    @GetMapping("/selectSingle")
    public LinkedHashMap<String, Object> selectSingle(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        //根据传入的id获取文章的相关信息
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        long id = Long.valueOf((String)map.get("id"));
        
        queryWrapper.eq("id", id);
        Essays article = essayMapper.selectOne(queryWrapper);

        //创建输出对象
        LinkedHashMap<String, Object> all_data = new LinkedHashMap<>();
        all_data.put("code", 200);
        all_data.put("article", article);

        //创建一个session对象，将id传到session中
        HttpSession session = request.getSession(true);
        session.setAttribute("id", id);

        return all_data;
        //return gson.toJson(all_data);
    }

    
    //提供各文章的各主题概率
    @GetMapping("/showProbability")
    public LinkedHashMap<String, Object> showProbability(@RequestParam Map<String, Object> map) {
        //根据传入的id获取文章的相关信息
        QueryWrapper<Essays> queryWrapper = new QueryWrapper<>();
        long id = Long.valueOf((String)map.get("id"));
        
        queryWrapper.eq("id", id);
        Essays article = essayMapper.selectOne(queryWrapper);

        //获取文章的各主题的概率
        String type = article.getType();

        //文章的主题名
        String[] key = type.split(",");
        //文章的主题概率
        String[] result = type.split(",");
        double[] value = {0,0,0,0,0,0,0,0,0,0};
        //文章的具体主题
        String[] other = type.split(",");

        LinkedHashMap<Integer, Object> all_data = new LinkedHashMap<>();
        LinkedHashMap<String, Object> end_result  = new LinkedHashMap<>();

        for (int i = 1; i < 10; i++) {
            //将拆分后的数据进行格式化处理
            key[i] = "Topic #" + (i - 1);
            result[i] = result[i].substring(1, 7);
            value[i] = (Double.valueOf(result[i]) * 100);
            value[i] = (double) Math.round(value[i] * 100) / 100;

            //获取具体的主题名信息
            QueryWrapper<Topic> topicWrapper = new QueryWrapper<>();
            topicWrapper.eq("topic_no", key[i]);
            Topic topic = topicMapper.selectOne(topicWrapper);
            other[i] = topic.getDescrip();

            //将主题名和概率封装在一个类中
            Probability res = new Probability();
            res.setName(key[i]);
            res.setValue(value[i]);
            res.setOther(other[i]);

            all_data.put(i - 1, res);
            end_result.put("probability", all_data);
        }
        
        return end_result;
    }

    
    //展示主题号及其对应主题
    @GetMapping("/showTopic")
    public LinkedHashMap<Integer, Object> showTopic() {
        //文章的主题名
        String[] key = {"a","a","a","a","a","a","a","a","a"};
        //文章的具体主题
        String[] other = {"a","a","a","a","a","a","a","a","a"};

        LinkedHashMap<Integer, Object> all_data = new LinkedHashMap<>();

        for (int i = 0; i < 9; i++) {
            key[i] = "Topic #" + i;
           
            //获取具体的主题名信息
            QueryWrapper<Topic> topicWrapper = new QueryWrapper<>();
            topicWrapper.eq("topic_no", key[i]);
            Topic topic = topicMapper.selectOne(topicWrapper);
            other[i] = topic.getDescrip();

            //将主题名和概率封装在一个类中
            Probability res = new Probability();
            res.setName(key[i]);
            res.setOther(other[i]);

            all_data.put(i, res);
        }
        
        return all_data;
    }
}
