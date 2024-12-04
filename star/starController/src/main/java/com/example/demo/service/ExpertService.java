package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Expert;
import com.example.demo.Class.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpertService extends IService<Expert> {
    List<Expert> listAll();
}
