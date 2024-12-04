package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Expert;
import com.example.demo.mapper.ExpertMapper;
import com.example.demo.service.ExpertService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExpertServiceImpl extends ServiceImpl<ExpertMapper, Expert> implements ExpertService
{
    @Resource
    private ExpertMapper expertMapper;
    @Override
    public List<Expert> listAll(){
        return expertMapper.listAll();
    }

}
