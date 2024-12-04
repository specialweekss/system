package com.example.demo.controller;

import com.example.demo.Class.Paper;
import com.example.demo.other.Result;
import com.example.demo.service.impl.PaperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@RestController
public class PaperController {

    @Autowired
    private PaperServiceImpl paperService;

    @CrossOrigin
    @GetMapping("/PaperList")
    public Result PaperList() {
        return Result.success(paperService.listAll(),paperService.count());
    }
    @CrossOrigin
    @GetMapping("/RandomPaper")
    public Result RandomPaper() {
        List<Paper> paperList = paperService.listAll();
        Random random = new Random();
        int index = random.nextInt(paperList.size());
        return Result.success(paperList.get(index));
    }
}