package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Appointment
{
    @TableId
    int appointmentId;
    int userId;
    int expertId;
    int score;
    int state;
    String feedBack;
}