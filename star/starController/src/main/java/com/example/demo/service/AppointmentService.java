package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService extends IService<Appointment> {
    List<Appointment> listAll();
}
