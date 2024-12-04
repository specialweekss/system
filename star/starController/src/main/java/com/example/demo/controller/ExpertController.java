package com.example.demo.controller;

import com.example.demo.Class.Expert;
import com.example.demo.other.Result;


import com.example.demo.service.impl.ExpertServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
public class ExpertController {

    @Autowired
    private ExpertServiceImpl expertService;

    @CrossOrigin
    @GetMapping("/ExpertList")
    public Result ExpertList() {
        return Result.success(expertService.listAll(),expertService.count());
    }
    @CrossOrigin
    @GetMapping("/ExpertById")
    public Result getExpertById(int expertId){
       Expert expert = expertService.getById(expertId);
       return Result.success(expert);
    }

}