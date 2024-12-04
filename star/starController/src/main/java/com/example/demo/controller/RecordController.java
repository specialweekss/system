package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Class.Appointment;
import com.example.demo.Class.Record;
import com.example.demo.other.Result;
import com.example.demo.service.impl.RecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecordController {
    @Autowired
    private RecordServiceImpl recordService;
    @CrossOrigin

    @GetMapping("/RecordByUser")
    public Result RecordByUser(int userId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getUserId, userId);
        List<Record> list = recordService.list(queryWrapper);
        return Result.success(list, list.size());
    }
    @CrossOrigin
    @PostMapping("/AddRecord")//插入
    public Result AddRecord(int userId,int paperId,int score) {
        Record record = new Record();
        record.setUserId(userId);
        record.setPaperId(paperId);
        record.setScore(score);
        recordService.save(record);
        return Result.success(record);
    }

    //不知道是否需要更新操作
    @CrossOrigin
    @PostMapping("/DelRecord")//先查找后删除，不需要使用RecordId 若结果不止一条，用GetOne
    public Result DelRecord(int userId,int paperId,int score) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getUserId, userId).eq(Record::getPaperId, paperId).eq(Record::getScore, score);
        recordService.remove(queryWrapper);
        return Result.success();
    }

}
