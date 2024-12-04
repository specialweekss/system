package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Paper;
import com.example.demo.mapper.PaperMapper;
import com.example.demo.service.PaperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Resource
    private PaperMapper paperMapper;
    @Override
    public List<Paper> listAll(){
        return paperMapper.listAll();
    }
}
