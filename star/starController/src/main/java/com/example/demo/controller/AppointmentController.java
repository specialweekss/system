package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Appointment;
import com.example.demo.other.Result;

import com.example.demo.service.impl.AppointmentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @CrossOrigin
    @GetMapping("/AppointmentList")
    public Result AppointmentList() {
        return Result.success(appointmentService.listAll(), appointmentService.count());
    }
    @CrossOrigin
    @GetMapping("/AppointmentById")
    public Result AppointmentById(int appointmentId) {
        return Result.success(appointmentService.getById(appointmentId));
    }
    @CrossOrigin
    @GetMapping("/AppointmentByExpert")
    public Result AppointmentByExpert(int expertId) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getExpertId, expertId);
        List<Appointment> list = appointmentService.list(queryWrapper);
        return Result.success(list, list.size());
    }
    @CrossOrigin
    @GetMapping("/AppointmentByUser")
    public Result AppointmentByUser(int userId) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getUserId, userId);
        List<Appointment> list = appointmentService.list(queryWrapper);
        return Result.success(list, list.size());
    }
    @CrossOrigin
    @PostMapping("/ScoreUp")
    public Result ScoreUp(int appointmentId,int score,String feedBack) {
        Appointment appointment = appointmentService.getById(appointmentId);
        appointment.setFeedBack(feedBack );
        appointment.setScore(score);
        appointment.setState(1);
        appointmentService.updateById(appointment);
        return Result.success(appointment);
    }
    @CrossOrigin
    @PostMapping("/AppointmentEnd")
    public Result AppointmentEnd(int appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        appointment.setState(0);
        appointmentService.updateById(appointment);
        return Result.success(appointment);
    }
    @CrossOrigin
    @PostMapping("/AddAppointment")
    public Result AddAppointment(@RequestBody Appointment appointment) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getUserId, appointment.getUserId()).eq(Appointment::getExpertId, appointment.getExpertId()).ne(Appointment::getState,1);
        List<Appointment> list = appointmentService.list(queryWrapper);
        if (!list.isEmpty()) {
            return Result.fail();
        } else
        {
            appointment.setState(-1);
            appointmentService.save(appointment);
            return Result.success();
        }
    }
    @CrossOrigin
    @PostMapping("/CancelAppointment")
    public Result CancelAppointment(int appointmentId) {
        if (appointmentService.getById(appointmentId) == null) {
            return Result.fail();
        }
        appointmentService.removeById(appointmentId);
        return Result.success();
    }
}