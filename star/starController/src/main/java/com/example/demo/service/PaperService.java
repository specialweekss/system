package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Paper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaperService extends IService<Paper> {
    List<Paper> listAll();
}

