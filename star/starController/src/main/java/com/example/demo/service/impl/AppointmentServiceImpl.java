package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Appointment;
import com.example.demo.Class.Expert;
import com.example.demo.mapper.AppointmentMapper;
import com.example.demo.service.AppointmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService
{
    @Resource
    private AppointmentMapper appointmentMapper;
    @Override
    public List<Appointment> listAll(){
        return appointmentMapper.listAll();
    }
}
